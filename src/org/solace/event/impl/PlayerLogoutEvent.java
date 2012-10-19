package org.solace.event.impl;

import java.io.IOException;
import org.solace.Server;
import org.solace.event.Event;
import org.solace.util.IndexManager;
import org.solace.world.World;
import org.solace.world.game.entity.mobile.player.Player;
import org.solace.world.map.Location;

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
                    World.getSingleton().deregister(player);
                    return;
                }
                player.channelContext().channel().close();  
                World.getSingleton().deregister(player);
                Server.logger.info("[Deregistry]: connection terminated for player: "+player.getAuthentication().getUsername());
            } catch (IOException e) {
                Server.logger.warning("Logout event failed to execute for player "+player.getAuthentication().getUsername());
            }
	}

}
