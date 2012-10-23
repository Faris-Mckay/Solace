package org.solace.network.packet.impl;

import org.solace.game.content.Consumables;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.container.Equipment;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;

public class ItemActionPacket implements PacketHandler {

	public static final int FIRST_ITEM_ACTION_OPCODE = 145;
	public static final int SECOND_ITEM_ACTION_OPCODE = 117;
	public static final int THIRD_ITEM_ACTION_OPCODE = 43;
	public static final int FOURTH_ITEM_ACTION_OPCODE = 129;

	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.opcode()) {
		case FIRST_ITEM_ACTION_OPCODE:
			handleFirstClickActions(player, packet);
			break;
		}
	}

	private void handleFirstClickActions(Player player, Packet packet) {
		int interfaceIndex = packet.getUShortA();
		int itemSlot = packet.getUShortA();
		int itemIndex = packet.getUShortA();
		Consumables.eatFood(player, itemIndex, itemSlot);
		switch (interfaceIndex) {
		case Equipment.EQUIPMENT_INTERFACE:
			player.getEquipment().unequip(itemIndex, itemSlot);
			break;
		}
	}

}
