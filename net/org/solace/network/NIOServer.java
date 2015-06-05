/*
 * This file is part of Solace Framework.
 * Solace is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Solace is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Solace. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.solace.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 *
 * @author KleptO
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
