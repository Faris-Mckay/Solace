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