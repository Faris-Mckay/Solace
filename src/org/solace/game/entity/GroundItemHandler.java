package org.solace.game.entity;

import org.solace.game.entity.GroundItem;
import java.util.concurrent.CopyOnWriteArrayList;

import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Location;

/**
 * Ground items manager.
 * 
 * @author kLeptO <http://www.rune-server.org/members/klepto/>
 */
public class GroundItemHandler {

	private static final CopyOnWriteArrayList<GroundItem> groundItems = new CopyOnWriteArrayList<GroundItem>();

	/**
	 * Registers a new ground item.
	 * 
	 * @param groundItem
	 *            the ground item
	 */
	public static void register(GroundItem groundItem) {
		groundItems.add(groundItem);
	}

	/**
	 * Registers a new global ground item.
	 * 
	 * @param groundItem
	 *            the ground item
	 */
	public static void registerGlobalItem(GroundItem groundItem) {
		groundItems.add(groundItem);
		sendGroundItem(groundItem);
	}

	/**
	 * Unregisters an existing ground item.
	 * 
	 * @param groundItem
	 *            the ground item
	 */
	public static void deregister(GroundItem groundItem) {
		groundItems.remove(groundItem);
	}

	/**
	 * Updates all visible ground items.
	 */
	public static void updateGroundItems() {
		for (GroundItem groundItem : groundItems) {
			if (groundItem == null) {
				continue;
			} else {
				groundItem.decreaseTimer();
			}
			if (groundItem.timer() == GroundItem.LIFE_SPAN / 2) {
				sendGroundItem(groundItem);
			} else if (groundItem.timer() == 0) {
				sendRemoveGroundItem(groundItem);
				deregister(groundItem);
			}
		}
	}

	/**
	 * Finds the ground item by a given location.
	 * 
	 * @param location
	 *            the item location
	 * 
	 * @param itemIndex
	 *            the item index
	 */
	public static GroundItem find(Location location, int itemIndex) {
		for (GroundItem groundItem : groundItems) {
			if (groundItem == null) {
				continue;
			}
			if (groundItem.getLocation().sameAs(location)
					&& groundItem.item().getIndex() == itemIndex) {
				return groundItem;
			}
		}
		return null;
	}

	/**
	 * Sends a ground item to all players except it's owner.
	 * 
	 * @param groundItem
	 *            the ground item
	 */
	public static void sendGroundItem(GroundItem groundItem) {
		for (Player player : Game.playerRepository.values()) {
			if (player == null) {
				continue;
			}
			if (groundItem.getLocation().withinDistance(player.getLocation(),
					32)) {
				continue;
			}
			if ((!player.getAuthentication().getUsername()
					.equals(groundItem.dropper()) && player.getLocation()
					.withinDistance(groundItem.getLocation(), 32))) {
				player.getPacketDispatcher().sendGroundItem(groundItem);
			}
		}
	}

	/**
	 * Sends a remove ground item packet to all players.
	 * 
	 * @param groundItem
	 *            the ground item
	 */
	public static void sendRemoveGroundItem(GroundItem groundItem) {
		for (Player player : Game.playerRepository.values()) {
			if (player == null) {
				continue;
			}
			if (groundItem.getLocation().withinDistance(player.getLocation(),
					32)) {
				continue;
			}
			player.getPacketDispatcher().sendRemoveGroundItem(groundItem);
		}
	}

	/**
	 * Sends all visible ground items to the player.
	 * 
	 * @param player
	 *            the player
	 */
	public static void sendGroundItems(Player player) {
		for (GroundItem groundItem : groundItems) {
			if (groundItem == null) {
				continue;
			}
			if (groundItem.getLocation().withinDistance(player.getLocation(),
					32)) {
				continue;
			}
			if (groundItem.timer() <= GroundItem.LIFE_SPAN / 2
					|| player.getAuthentication().getUsername()
							.equals(groundItem.dropper())) {
				player.getPacketDispatcher().sendGroundItem(groundItem);
			}
		}
	}

	/**
	 * Sends remove packet for all visible ground items to the player.
	 * 
	 * @param player
	 *            the player
	 */
	public static void sendRemoveGroundItems(Player player) {
		for (GroundItem groundItem : groundItems) {
			if (groundItem == null) {
				continue;
			}
			if (groundItem.getLocation().withinDistance(player.getLocation(),
					32)) {
				continue;
			}
			if (groundItem.timer() <= GroundItem.LIFE_SPAN / 2
					|| player.getAuthentication().getUsername()
							.equals(groundItem.dropper())) {
				player.getPacketDispatcher().sendRemoveGroundItem(groundItem);
			}
		}
	}

}