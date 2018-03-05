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
package org.solace.network.packet.impl;

import org.solace.game.content.combat.Combat;
import org.solace.game.entity.ground.GroundItem;
import org.solace.game.entity.ground.GroundItemHandler;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;

public class DropItemPacket implements PacketHandler {
	
	public static final int DROP_ITEM_OPCODE = 87;

	@Override
	public void handlePacket(Player player, Packet packet) {
		@SuppressWarnings("unused")
		int itemIndex = packet.getUnsignedShortA();
		@SuppressWarnings("unused")
		int interfaceIndex = packet.getUShort();
		int itemSlot = packet.getUShortA();

		Item item = player.getInventory().items()[itemSlot];
		
		if (item.getIndex() > -1) {
			Combat.resetCombat(player);
			/*
			 * Define ground item.
			 */
			GroundItem groundItem = new GroundItem(item.getIndex(), item.getAmount(), player, player.getLocation());

			/*
			 * Drop the item.
			 */
			GroundItemHandler.register(groundItem);
			player.getPacketDispatcher().sendGroundItem(groundItem);
			player.getInventory().set(itemSlot, -1, 0);
			player.getInventory().refreshItems();
		}
	}

}
