package org.solace.event.impl;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.solace.Server;
import org.solace.event.Event;
import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;

public class PlayerLogoutEvent extends Event {

	private Player player;

	public PlayerLogoutEvent(Player player) {
            super(EventType.Standalone, 0 , false);
            this.player = player;
	}

	@Override
	public void execute() {
            if (player.channelContext().channel() == null){
                Game.getSingleton().deregister(player);
                return;
            }
            new PlayerSaveEvent(player).execute();
            player.getPacketDispatcher().sendLogout();
            try {
                player.channelContext().channel().close();
            } catch (IOException ex) {
                Logger.getLogger(PlayerLogoutEvent.class.getName()).log(Level.SEVERE, null, ex);
            }
            Game.getSingleton().deregister(player);
            Server.logger.info("[Deregistry]: connection terminated for player: "+player.getAuthentication().getUsername());
	}

}
