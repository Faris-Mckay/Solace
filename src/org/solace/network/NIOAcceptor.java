package org.solace.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import org.solace.Server;
import org.solace.world.game.entity.mobile.player.PlayerLoginDecipher;
import org.solace.world.World;

/**
 *
 * @author Faris
 */
public class NIOAcceptor extends Thread {   
    
    @Override
    public void run() {
        while (NIOServer.channel().isOpen()) {
            try {
                SocketChannel channel = NIOServer.channel().accept();
                channel.configureBlocking(false);
                RSChannelContext channelContext = new RSChannelContext(channel, new PlayerLoginDecipher());
                for (int i = 0; i < 3; i++) {
                    try {
                        channelContext.decoder().decode(channelContext);
                        Thread.sleep(50);
                    } catch (Exception e) {
                        e.printStackTrace();
                        channel.close();
                    }
                }
                if (!channel.isOpen()) {
                    if (channelContext.player() != null) {
                        World.getSingleton().deregister(channelContext.player());
                    }
                    continue;
                }
                channel.register(NIOSelector.selector(), SelectionKey.OP_READ,channelContext);
            } catch (IOException e) {
                    e.printStackTrace();
            }
        }
    }

}
