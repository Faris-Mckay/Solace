/**
 * This file is part of Zap Framework.
 *
 * Zap is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Zap is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Zap. If not, see <http://www.gnu.org/licenses/>.
 */
package org.solace.network.decoders;

import java.security.SecureRandom;
import net.burtlebutle.bob.rand.isaac.ISAAC;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;
import org.solace.event.impl.PlayerLoadService;
import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;
import org.solace.network.LoginState;
import static org.solace.network.LoginState.CONNECTED;
import static org.solace.network.LoginState.LOGGING_IN;
import org.solace.network.packet.PacketBuilder;
import org.solace.network.util.NetUtilities;
import org.solace.util.Constants;

/**
 *
 * @author Faris
 */
public class RS2LoginProtocolDecoder extends FrameDecoder {

    private LoginState state = LoginState.CONNECTED;

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
        if (!channel.isConnected()) {
            return null;
        }
        switch (state) {
            case CONNECTED:
                if (buffer.readableBytes() < 2) {
                    return null;
                }
                int request = buffer.readUnsignedByte();
                if (request != 14) {
                    System.out.println("Invalid login request: " + request);
                    channel.close();
                    return null;
                }
                buffer.readUnsignedByte();
                channel.write(new PacketBuilder().putLong(0).put((byte) 0).putLong(new SecureRandom().nextLong()).toPacket());
                state = LoginState.LOGGING_IN;
                return null;
            case LOGGING_IN:
                if (buffer.readableBytes() < 2) {
                    return null;
                }
                int loginType = buffer.readByte();
                if (loginType != 16 && loginType != 18) {
                    System.out.println("Invalid login type: " + loginType);
                    channel.close();
                    return null;
                }
                int blockLength = buffer.readByte() & 0xff;
                if (buffer.readableBytes() < blockLength) {
                    return null;
                }
                buffer.readByte();
                int clientRevision = buffer.readShort();
                buffer.readByte();
                for (int i = 0; i < 9; i++) {
                    buffer.readInt();
                }
                buffer.readByte();
                int rsaOpcode = buffer.readByte();
                if (rsaOpcode != 10) {
                    System.out.println("Unable to decode RSA block properly!");
                    channel.close();
                    return null;
                }
                final long clientHalf = buffer.readLong();
                final long serverHalf = buffer.readLong();
                final int[] isaacSeed = {(int) (clientHalf >> 32), (int) clientHalf, (int) (serverHalf >> 32), (int) serverHalf};
                final ISAAC inCipher = new ISAAC(isaacSeed);
                for (int i = 0; i < isaacSeed.length; i++) {
                    isaacSeed[i] += 50;
                }
                final ISAAC outCipher = new ISAAC(isaacSeed);
                final int userId = buffer.readInt();
                final String name = NetUtilities.formatPlayerName(NetUtilities.getRS2String(buffer));
                final String pass = NetUtilities.getRS2String(buffer);
                channel.getPipeline().replace("decoder", "decoder", new RS2ProtocolDecoder(inCipher));
                return login(channel,outCipher, userId, name, pass);
        }
        return null;
    }

    private static Player login(Channel channel,ISAAC outCipher, int userId, String name, String pass) {
        int returnCode = 2;
        if (!name.matches("[A-Za-z0-9 ]+")) {
            returnCode = 4;
        }
        if (name.length() > 12) {
            returnCode = 8;
        }
        Player player = new Player(channel, name, pass);
        player.getOutStream().packetEncryption = outCipher;
        boolean loaded = new PlayerLoadService(player).load();
        if (Game.getPlayerByName(name) != null) {
            returnCode = 5;
        }
        if (Game.getPlayerRepository().size() >= Constants.SERVER_MAX_PLAYERS) {
            returnCode = 7;
        }
        if (returnCode == 2) {
            if (!loaded) {
		returnCode = 3;
	} else {
            Game.getSingleton().register(player);
        }
            final PacketBuilder bldr = new PacketBuilder();
            bldr.put((byte) 2);
            if (player.getAuthentication().getPlayerRights() == 3) {
                bldr.put((byte) 2);
            } else {
                bldr.put((byte) player.getAuthentication().getPlayerRights());
            }
            bldr.put((byte) 0);
            channel.write(bldr.toPacket());
            player.getPacketDispatcher().sendInitPacket();
        } else {
            System.out.println("returncode:" + returnCode);
            sendReturnCode(channel, returnCode);
            return null;
        }
        return player;
    }

    public static void sendReturnCode(final Channel channel, final int code) {
        channel.write(new PacketBuilder().put((byte) code).toPacket()).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(final ChannelFuture arg0) throws Exception {
                arg0.getChannel().close();
            }
        });
    }
}
