package org.solace.event.impl;

import mint.event.Event;

import org.solace.game.entity.mobile.player.Player;

public class PlayerLoginEvent implements Event {
	
	private Player player;
	
	public PlayerLoginEvent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

}
