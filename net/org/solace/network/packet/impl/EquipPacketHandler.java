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

import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.ItemDefinition;
import org.solace.game.item.container.impl.Inventory;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;

/**
 * Equip packet handler.
 * 
 * @author kLeptO <http://www.rune-server.org/members/klepto/>
 */
public class EquipPacketHandler implements PacketHandler {

	@Override
	public void handlePacket(Player player, Packet packet) {
		int itemIndex = packet.getUShort();
		int itemSlot = packet.getUShortA();
		int interfaceIndex = packet.getUShortA();

		/*
		 * Check if this is equipment packet.
		 */
		if (ItemDefinition.get(itemIndex).equipmentSlot() > -1
				&& interfaceIndex == Inventory.INVENTORY_INTERFACE) {
			/*
			 * Attempt to equip the item.
			 */
			player.getEquipment().equip(itemIndex, itemSlot);
			//player.setUsingSpecial(false);
		}
	}

}