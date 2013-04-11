package org.solace.event.impl;

import org.solace.event.Event;
import org.solace.game.entity.grounded.GroundItem;
import org.solace.game.entity.grounded.GroundItemHandler;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.ItemDefinition;
import org.solace.game.map.Location;

public class PickupGroundItemService extends Event {

	private Location location;

	private int index;

	private Player player;

	public PickupGroundItemService(Player player, Location location, int index) {
		this.location = location;
		this.index = index;
		this.player = player;
	}

	@Override
	public void execute() {
		/*
		 * Let's check if ground item exists.
		 */
		GroundItem groundItem = GroundItemHandler.find(location, index);
		if (groundItem == null) {
			return;
		}

		/*
		 * Check if it is stackable and to pick it up or discard if it exceeds
		 * max stack size.
		 */
		if (ItemDefinition.get(index).stackable()) {
			int itemSlot = player.getInventory().itemSlot(index);

			/*
			 * Check if we have enough space in the inventory.
			 */
			if (itemSlot == -1 && player.getInventory().freeSpace() == 0) {
				player.getInventory().noSpace();
				return;
			}

			if (itemSlot != -1) {
				/*
				 * If we already have the same item in the inventory let's
				 * calculate the total amount and discard if it exceeds max
				 * stack.
				 */
				long totalAmount = (long) player.getInventory().items()[itemSlot]
						.getAmount() + (long) groundItem.item().getAmount();
				if (totalAmount > Integer.MAX_VALUE) {
					player.getInventory().noSpace();
					return;
				}
			}

			/*
			 * Pickup the stackable item.
			 */
			player.getInventory().add(groundItem.item());
			GroundItemHandler.deregister(groundItem);
			if (groundItem.timer() <= GroundItem.LIFE_SPAN / 2) {
				GroundItemHandler.sendRemoveGroundItem(groundItem);
			} else {
				player.getPacketDispatcher().sendRemoveGroundItem(groundItem);
			}
		} else {
			/*
			 * Else if item is non-stackable let's check if we have enough
			 * space.
			 */
			if (player.getInventory().freeSpace() == 0) {
				player.getInventory().noSpace();
				return;
			}

			/*
			 * And finally just pick it up.
			 */
			player.getInventory().add(groundItem.item());
			GroundItemHandler.deregister(groundItem);
			if (groundItem.timer() <= GroundItem.LIFE_SPAN / 2) {
				GroundItemHandler.sendRemoveGroundItem(groundItem);
			} else {
				player.getPacketDispatcher().sendRemoveGroundItem(groundItem);
			}
		}
		this.stop();
	}

}
