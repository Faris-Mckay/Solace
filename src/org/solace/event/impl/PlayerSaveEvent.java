package org.solace.event.impl;

import mint.event.Event;

import org.solace.game.entity.mobile.player.Player;

public class PlayerSaveEvent implements Event {

	private Player player; 
	
	public PlayerSaveEvent(Player player) {
		this.player = player;
	}

	public Player getPlayer() {
		return player;
	}

	/**
	 * Saves all of the data for the player
	 * 
	 * @param player
	 *            The player being saved
	 * @throws Exception
	 *             Catches any exception that may be sent
	 */
	

}
