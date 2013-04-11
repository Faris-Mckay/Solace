package org.solace.event.impl;

import mint.event.Event;

import org.solace.game.entity.mobile.player.Player;

public class SpecialAttackEvent implements Event {
	
	private final Player player;
	
	public SpecialAttackEvent(Player player) {
		this.player = player;
	}
	
	public Player getPlayer() {
		return player;
	}

}
