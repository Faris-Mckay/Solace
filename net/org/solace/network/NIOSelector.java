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
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import org.solace.game.Game;

/**
 *
 * @author KleptO
 */
public class NIOSelector {
    
    private static Selector selector;

    public void select() {
        try {
            selector().selectNow();
            for (SelectionKey key : selector().selectedKeys()) {
                if (key.attachment() == null) {
                    continue;
                }
                RSChannelContext channelContext = (RSChannelContext) key.attachment();
                try {
                    channelContext.decoder().decode(channelContext);
                } catch (Exception e) {
                    Game.getSingleton().deregister(channelContext.player());
                    key.channel().close();
                    key.cancel();
                }
            }
        } catch (IOException e) {
        }
    }

    /**
     * Sets the associated selector.
     * @param selector the selector
     */
    public static void selector(Selector selector) {
        NIOSelector.selector = selector;
    }

    /**
     * Gets the associated selector.
     * @return the selector
     */
    public static Selector selector() {
        return selector;
    }

}
