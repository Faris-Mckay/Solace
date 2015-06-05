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
package org.solace.game.item.container.impl;

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
