package org.solace.world.game.entity.mobile.player;

import java.io.IOException;
import java.nio.ByteBuffer;
import org.solace.event.impl.PlayerLoadEvent;
import org.solace.network.NIODecoder;
import org.solace.network.RSChannelContext;
import org.solace.network.packet.PacketBuilder;
import org.solace.network.packet.RSPacketDecoder;
import org.solace.util.ISAAC;
import org.solace.util.ProtocolUtils;
import org.solace.world.World;
import org.solace.world.game.entity.mobile.player.Player;

/**
 * RuneScape login procedure decoder.
 * @author Faris
 */
public class PlayerLoginDecipher implements NIODecoder {

	private PlayerLoginDecipher.State state = PlayerLoginDecipher.State.READ_USERNAME_HASH;
	private ByteBuffer buffer = ByteBuffer.allocateDirect(126);

	@Override
	public void decode(RSChannelContext channelContext) throws IOException {
		/*
		 * Read the incoming data.
		 */
		channelContext.channel().read(buffer);
		buffer.flip();

		/*
		 * Handle login procedure.
		 */
		switch (state) {

		case READ_USERNAME_HASH:
			/*
			 * Check if buffer has enough readable data.
			 */
			if (buffer.remaining() < 2) {
				buffer.compact();
				break;
			}

			/*
			 * Login packet opcode.
			 */
			int loginType = buffer.get() & 0xFF;
			if (loginType != 14) {
				System.out.println("Invalid login type.");
				channelContext.channel().close();
				break;
			}

			/*
			 * Name hash which is probably used to select proper login server.
			 */
			@SuppressWarnings("unused")
			int nameHash = buffer.get() & 0xFF;

			/*
			 * Write the response after first state.
			 */
			PacketBuilder out = PacketBuilder.allocate(17);
			out.putBytes(0, 17);
			out.sendTo(channelContext.channel());

			/*
			 * Switch to the next login procedure state.
			 */
			state = PlayerLoginDecipher.State.READ_LOGIN_HEADER;
			buffer.compact();
			break;

		case READ_LOGIN_HEADER:
			/*
			 * Check if buffer has enough readable data.
			 */
			if (buffer.remaining() < 2) {
				buffer.compact();
				break;
			}

			/*
			 * Login request. 16 means that it's normal connection and 18 means
			 * that it's reconnection.
			 */
			int loginRequest = buffer.get() & 0xFF;
			if (loginRequest != 16 && loginRequest != 18) {
				System.out.println("Invalid login request.");
				channelContext.channel().close();
				break;
			}

			/*
			 * Login payload size.
			 */
			loginPacketLength = buffer.get() & 0xFF;

			/*
			 * Switching to the last login state.
			 */
			state = PlayerLoginDecipher.State.READ_LOGIN_PAYLOAD;

		case READ_LOGIN_PAYLOAD:
			/*
			 * Check if buffer has enough readable data.
			 */
			if (buffer.remaining() < loginPacketLength) {
				buffer.flip();
				buffer.compact();
				break;
			}

			/*
			 * Opcode of the last login state.
			 */
			@SuppressWarnings("unused")
			int loginOpcode = buffer.get() & 0xFF;
			/*
			 * Version of the client, in this case 317.
			 */
			@SuppressWarnings("unused")
			int clientVersion = buffer.getShort();

			/*
			 * Client memory version, indicates if client is on low or high
			 * detail mode.
			 */
			@SuppressWarnings("unused")
			int memoryVersion = buffer.get() & 0xFF;

			/*
			 * Skipping the RSA packet.
			 */
			for (int i = 0; i < 9; i++) {
				buffer.getInt();
			}

			/*
			 * The actual payload size, just another indicator to check if it's
			 * correct login packet.
			 */
			int expectedPayloadSize = buffer.get() & 0xFF;
			if (expectedPayloadSize != loginPacketLength - 41) {
				System.out.println("Invalid payload size.");
				channelContext.channel().close();
				break;
			}

			/*
			 * The RSA packet opcode.
			 */
			int rsaOpcode = buffer.get() & 0xFF;
			if (rsaOpcode != 10) {
				System.out.println("Invalid RSA operation code.");
				channelContext.channel().close();
				break;
			}

			/*
			 * Skipping the ISAAC seeds as we are not using it.
			 */
			long clientSeed = buffer.getLong();
			long serverSeed = buffer.getLong();

			int sessionSeed[] = new int[4];
			sessionSeed[0] = (int) (clientSeed >> 32);
			sessionSeed[1] = (int) clientSeed;
			sessionSeed[2] = (int) (serverSeed >> 32);
			sessionSeed[3] = (int) serverSeed;
			channelContext.decryption(new ISAAC(sessionSeed));

			for (int i = 0; i < 4; i++) {
				sessionSeed[i] += 50;
			}
			channelContext.encryption(new ISAAC(sessionSeed));

			/*
			 * The user id.
			 */
			@SuppressWarnings("unused")
			int userId = buffer.getInt();

			/*
			 * The user identify.
			 */
			String username = ProtocolUtils.formatString(ProtocolUtils.getRSString(buffer));
			String password = ProtocolUtils.getRSString(buffer);
			/*
			 * Create the player object for this channel.
			 */
			Player player = new Player(username, password, channelContext);
			boolean loaded = PlayerLoadEvent.loadGame(player);

			/*
			 * Generate response opcode.
			 */
			int response = 2;

			if (!loaded) {
                                    System.out.println("Connection refused from not loaded");
				/*
				 * Invalid username or password.
				 */
				response = 3;
			} else {
                                    System.out.println("Connection recieved from loaded ");
				channelContext.player(player);
				World.getSingleton().register(player);
			}

			if (player.getIndex() == -1) {
                            System.out.println("Connection recieved from index ");
				/*
				 * World is full.
				 */
				response = 10;
			}
                        System.out.println("idle ");

			/*
			 * Write the login procedure response.
			 */
			out = PacketBuilder.allocate(3);
			out.putByte(response);
			out.putByte(player.getPlayerRights());
			out.putByte(0);
			out.sendTo(channelContext.channel());

			/*
			 * Since login procedure is finished, switching to packet decoder.
			 */
			channelContext.decoder(new RSPacketDecoder());

			/*
			 * And finally sending the initialization packet to the client.
			 */
			player.getPacketSender().sendInitPacket();
			break;
		}
	}

	/*
	 * We use this to store login packet length to check if all data has
	 * arrived.
	 */
	private int loginPacketLength;

	/**
	 * Login procedure states.
	 */
	private enum State {
		READ_USERNAME_HASH, READ_LOGIN_HEADER, READ_LOGIN_PAYLOAD
	}

}