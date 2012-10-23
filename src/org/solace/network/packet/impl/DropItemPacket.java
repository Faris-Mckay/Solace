package org.solace.network.packet.impl;

import org.solace.game.entity.GroundItem;
import org.solace.game.entity.GroundItemHandler;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;

public class DropItemPacket implements PacketHandler {
	
	public static final int DROP_ITEM_OPCODE = 87;

	@Override
	public void handlePacket(Player player, Packet packet) {
		@SuppressWarnings("unused")
		int itemIndex = packet.getUShortA();
		@SuppressWarnings("unused")
		int interfaceIndex = packet.getUShort();
		int itemSlot = packet.getUShortA();

		Item item = player.getInventory().items()[itemSlot];
		
		if (item.getIndex() > -1) {
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
