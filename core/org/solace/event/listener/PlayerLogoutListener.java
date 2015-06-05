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
package org.solace.event.listener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mint.event.EventHandler;
import mint.event.EventListener;

import org.solace.Server;
import org.solace.event.events.PlayerLogoutEvent;
import org.solace.event.events.PlayerSaveEvent;
import org.solace.game.Game;

/**
 * Handles the player logging out
 *
 * @author Arithium
 *
 */
public class PlayerLogoutListener implements EventListener {

    @EventHandler
    public void handleLogout(PlayerLogoutEvent event) {
        if (event.getPlayer().channelContext().channel() == null) {
            Game.getSingleton().deregister(event.getPlayer());
            return;
        }
        event.getPlayer().getPrivateMessaging().refresh(true);
        Server.getEventManager().dispatchEvent(new PlayerSaveEvent(event.getPlayer()));
        event.getPlayer().getPacketDispatcher().sendLogout();
        try {
            event.getPlayer().channelContext().channel().close();
        } catch (IOException ex) {
            Logger.getLogger(PlayerLogoutListener.class.getName()).log(Level.SEVERE, null, ex);
        }
        Game.getSingleton().deregister(event.getPlayer());
        Server.logger.info("[Deregistry]: connection terminated for player: " + event.getPlayer().getAuthentication().getUsername());
    }

}
