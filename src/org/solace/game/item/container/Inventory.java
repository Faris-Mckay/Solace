package org.solace.game.item.container;

import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.container.Container;

/**
 * 
 * @author Faris
 */
public class Inventory extends Container {

	public Inventory(Player player) {
		super(player);
		resetItems();
	}

	@Override
	public int capacity() {
		return 28;
	}

	@Override
	public boolean stack() {
		return false;
	}

	@Override
	public Container refreshItems() {
		player().getPacketDispatcher().sendItemContainer(this, INVENTORY_INTERFACE);
		return this;
	}

	@Override
	public Container noSpace() {
		player().getPacketDispatcher().sendMessage("Not enough space in your inventory.");
		return this;
	}

	public static final int INVENTORY_INTERFACE = 3214;

}
