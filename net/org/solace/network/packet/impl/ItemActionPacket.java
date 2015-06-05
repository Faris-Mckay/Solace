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
import org.solace.game.item.container.impl.Banking;
import org.solace.game.item.container.impl.Equipment;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;

/**
 * Item action packet handler.
 * 
 * @author Arithium
 */
public class ItemActionPacket implements PacketHandler {

	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.opcode()) {
		case FIRST_ITEM_ACTION_OPCODE:
			handleFirstItemAction(player, packet);
			break;
		case SECOND_ITEM_ACTION_OPCODE:
			handleSecondItemAction(player, packet);
			break;
		case THIRD_ITEM_ACTION_OPCODE:
			handleThirdItemAction(player, packet);
			break;
		case FOURTH_ITEM_ACTION_OPCODE:
			handleFourthItemAction(player, packet);
			break;
		}
	}

	/**
	 * Handles first item action.
	 * 
	 * @param player
	 *            the player reference
	 * 
	 * @param packet
	 *            the packet
	 */
	public void handleFirstItemAction(Player player, Packet packet) {
		int interfaceIndex = packet.getUShortA();
		int itemSlot = packet.getUShortA();
		int itemIndex = packet.getUShortA();
		switch (interfaceIndex) {
		case Equipment.EQUIPMENT_INTERFACE:
			player.getEquipment().unequip(itemIndex, itemSlot);
			break;
		case Banking.BANK_INTERFACE:
			player.getBanking().withdraw(itemIndex, 1, itemSlot);
			break;
		case Banking.BANK_INVENTORY_INTERFACE:
			player.getBanking().deposit(itemIndex, 1, itemSlot);
			break;
		}
	}

	/**
	 * Handles second item action.
	 * 
	 * @param player
	 *            the player reference
	 * 
	 * @param packet
	 *            the packet
	 */
	public void handleSecondItemAction(Player player, Packet packet) {
		int interfaceIndex = packet.getLEShortA();
		int itemIndex = packet.getLEShortA();
		int itemSlot = packet.getLEShort();
		switch (interfaceIndex) {
		case Banking.BANK_INTERFACE:
			player.getBanking().withdraw(itemIndex, 5, itemSlot);
			break;
		case Banking.BANK_INVENTORY_INTERFACE:
			player.getBanking().deposit(itemIndex, 5, itemSlot);
			break;
		}
	}

	/**
	 * Handles third item action.
	 * 
	 * @param player
	 *            the player reference
	 * 
	 * @param packet
	 *            the packet
	 */
	public void handleThirdItemAction(Player player, Packet packet) {
		int interfaceIndex = packet.getULEShort();
		int itemIndex = packet.getUShortA();
		int itemSlot = packet.getUShortA();

		switch (interfaceIndex) {
		case Banking.BANK_INTERFACE:
			player.getBanking().withdraw(itemIndex, 10, itemSlot);
			break;
		case Banking.BANK_INVENTORY_INTERFACE:
			player.getBanking().deposit(itemIndex, 10, itemSlot);
			break;
		}
	}

	/**
	 * Handles fourth item action.
	 * 
	 * @param player
	 *            the player reference
	 * 
	 * @param packet
	 *            the packet
	 */
	public void handleFourthItemAction(Player player, Packet packet) {
		int itemSlot = packet.getUShortA();
		int interfaceIndex = packet.getUShort();
		int itemIndex = packet.getUShortA();

		switch (interfaceIndex) {
		case Banking.BANK_INTERFACE:
			player.getBanking().withdraw(itemIndex, player.getBanking().items()[itemSlot].getAmount(), itemSlot);
			break;
		case Banking.BANK_INVENTORY_INTERFACE:
			player.getBanking().deposit(itemIndex, player.getInventory().itemAmount(itemIndex), itemSlot);
			break;
		}
	}

	public static final int FIRST_ITEM_ACTION_OPCODE = 145;
	public static final int SECOND_ITEM_ACTION_OPCODE = 117;
	public static final int THIRD_ITEM_ACTION_OPCODE = 43;
	public static final int FOURTH_ITEM_ACTION_OPCODE = 129;

}