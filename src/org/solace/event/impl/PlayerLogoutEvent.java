package org.solace.event.impl;

import mint.event.Event;

import org.solace.game.entity.mobile.player.Player;

/**
 * Represents an event taken place when a player logs out
 * @author Tim
 *
 */
public class PlayerLogoutEvent implements Event {
	
	private Player player;
	
	public PlayerLogoutEvent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

}
