package org.solace.event.impl;

import mint.event.Event;

import org.solace.game.entity.mobile.player.Player;

public class ProcessCommandEvent implements Event {
	
	private final String command;
	
	private final Player player;
	
	public ProcessCommandEvent(Player player, String command) {
		this.command = command;
		this.player = player;
	}
	
	public String getCommand() {
		return command;
	}
	
	public Player getPlayer() {
		return player;
	}

}
