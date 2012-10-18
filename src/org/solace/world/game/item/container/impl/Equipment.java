package org.solace.world.game.item.container.impl;

import org.solace.world.game.entity.mobile.player.Player;
import org.solace.world.game.item.container.Container;

/**
 * 
 * @author Faris
 */
public class Equipment extends Container {

	private Player player;

	public Equipment(Player player) {
		super(player);
	}

	public static final int HAT_SLOT = 0;
	public static final int CAPE_SLOT = 1;
	public static final int AMULET_SLOT = 2;
	public static final int WEAPON_SLOT = 3;
	public static final int BODY_SLOT = 4;
	public static final int SHIELD_SLOT = 5;
	public static final int LEGS_SLOT = 7;
	public static final int HANDS_SLOT = 9;
	public static final int FEET_SLOT = 10;
	public static final int RING_SLOT = 12;
	public static final int AMMUNITION_SLOT = 13;

	@Override
	public int capacity() {
		// TODO Auto-generated method stub
		return 14;
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
