package org.solace.event.impl;

import java.io.IOException;
import org.solace.Server;
import org.solace.event.Event;
import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;

public class PlayerLogoutEvent extends Event {

	private Player player;

	public PlayerLogoutEvent(Player player) {
            super(EventType.Standalone);
            this.player = player;
	}

	@Override
	public void execute() {
            try {
                if (player.channelContext().channel() == null){
                    Game.getSingleton().deregister(player);
                    return;
                }
                player.channelContext().channel().close();  
                Game.getSingleton().deregister(player);
                Server.logger.info("[Deregistry]: connection terminated for player: "+player.getAuthentication().getUsername());
            } catch (IOException e) {
                Server.logger.warning("Logout event failed to execute for player "+player.getAuthentication().getUsername());
            }
	}

}
