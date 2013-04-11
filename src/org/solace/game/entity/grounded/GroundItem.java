package org.solace.game.entity.grounded;

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