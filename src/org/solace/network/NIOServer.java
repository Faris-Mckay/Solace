package org.solace.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 *
 * @author Faris
 */
public class NIOServer {
    
    private static ServerSocketChannel channel;

    /**
     * Binds the server on a given port.
     * @param port the server port
     */
    public static void bind(int port) throws IOException {
        NIOSelector.selector(Selector.open());
        channel(ServerSocketChannel.open());
        channel().configureBlocking(true);
        channel().socket().bind(new InetSocketAddress(port));
        new NIOAcceptor().start();
    }

    /**
     * Sets the server socket channel.
     * @param channel the server channel
     */
    public static void channel(ServerSocketChannel channel) {
        NIOServer.channel = channel;
    }

    /**
     * Gets the server socket channel.
     * @return the server channel
     */
    public static ServerSocketChannel channel() {
        return channel;
    }

}
