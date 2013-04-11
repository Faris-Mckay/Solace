package org.solace.event.listener;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import mint.event.EventHandler;
import mint.event.EventListener;

import org.solace.Server;
import org.solace.event.impl.PlayerLogoutEvent;
import org.solace.event.impl.PlayerSaveEvent;
import org.solace.game.Game;

/**
 * Handles the player logging out
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
