/*
 * This file is part of Solace Framework.
 * Solace is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Solace is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Solace. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.solace.game.entity.ground;

import org.solace.game.entity.Entity;
import org.solace.game.entity.Entity;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.game.map.Location;

/**
 * Represents a single ground item.
 * @author kLeptO <http://www.rune-server.org/members/klepto/>
 */
public class GroundItem extends Entity {

	private Item item;
	private int timer = LIFE_SPAN;
	private String dropper;

	/**
	 * Creates a new ground item.
	 * @param index the item index
	 * @param amount the item amount
	 * @param player the associated player
	 */
	public GroundItem(int index, int amount, Player player, Location location) {
		super(location);
		item(new Item(index, amount));
		dropper(player.getAuthentication().getUsername());
	}

	/**
	 * Sets the associated item.
	 * 
	 * @param item
	 *            the item
	 */
	public GroundItem item(Item item) {
		this.item = item;
		return this;
	}

	/**
	 * Gets the associated item.
	 * 
	 * @return the associated item
	 */
	public Item item() {
		return item;
	}

	/**
	 * Decreases ground item timer by one.
	 */
	public GroundItem decreaseTimer() {
		timer--;
		return this;
	}

	/**
	 * Gets the ground item timer.
	 * 
	 * @return the ground item timer
	 */
	public int timer() {
		return timer;
	}

	/**
	 * Sets the dropper of this item.
	 * 
	 * @param dropper
	 *            the owner of this item
	 */
	public GroundItem dropper(String dropper) {
		this.dropper = dropper;
		return this;
	}

	/**
	 * Gets the item owner's username.
	 * 
	 * @return the droppers username
	 */
	public String dropper() {
		return dropper;
	}

	public static final int LIFE_SPAN = 200;

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}