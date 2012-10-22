package org.solace.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import org.solace.game.Game;

/**
 *
 * @author Faris
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
