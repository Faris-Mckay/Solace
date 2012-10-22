package org.solace.game.item.container.impl;

import org.solace.util.Constants;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.container.Container;

/**
 * 
 * @author Faris
 */
public class Bank extends Container {

	public Bank(Player player) {
		super(player);
	}

	@Override
	public int capacity() {
		return Constants.PLAYER_BANK_SLOT_ALLOWANCE;
	}

	@Override
	public boolean stack() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Container refreshItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Container noSpace() {
		// TODO Auto-generated method stub
		return null;
	}

}
