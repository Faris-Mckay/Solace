package org.solace.game.entity.mobile.player.command.impl;

import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.mobile.player.command.Command;
import org.solace.game.item.ItemDefinition;

public class SpawnItemByIdCommand implements Command {

	@Override
	public void execute(Player player, String command) {
		String[] args = command.split(" ");
		if (args.length == 2) {
			int newItemId = Integer.parseInt(args[1]);
			if (newItemId > ItemDefinition.MAXIMUM_ITEM_INDEX) {
				player.getPacketDispatcher().sendMessage("Item does not exist.");
			}
			if (player.getInventory().contains(newItemId) && (player.getInventory().get(newItemId).getAmount() + 1 > Integer.MAX_VALUE)) {
				player.getInventory().get(newItemId).setAmount(Integer.MAX_VALUE);
			} else {
				player.getInventory().add(newItemId, 1);
			}
		} else if (args.length == 3) {
			int newItemId = Integer.parseInt(args[1]);
			int newItemAmount = Integer.parseInt(args[2]);
			if (newItemId > ItemDefinition.MAXIMUM_ITEM_INDEX) {
				player.getPacketDispatcher().sendMessage("Item does not exist.");
			}
			if (player.getInventory().contains(newItemId) && (player.getInventory().get(newItemId).getAmount() + newItemAmount > Integer.MAX_VALUE)) {
				player.getInventory().get(newItemId).setAmount(Integer.MAX_VALUE);
			} else {
				player.getInventory().add(newItemId, newItemAmount);
			}
		}
	}

	@Override
	public int rightsRequired() {
		return 0;
	}

	@Override
	public boolean checkRequirements(Player player) {
		return true;
	}

}
