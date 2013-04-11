package org.solace.game.entity.mobile.player.command;

import java.util.HashMap;
import java.util.Map;

import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.mobile.player.command.impl.SpawnItemByIdCommand;

/**
 * Handles the storing and processing of commands
 * @author Arithium
 *
 */
public class CommandHandler {
	
	private static Map<String, Command> commandMap = new HashMap<String, Command>();
	
	public static void loadCommands() {
		commandMap.put("item", new SpawnItemByIdCommand());
	}
	
	public static void processCommand(Player player, String command) {
		String[] args = command.split(" ");
		try {
			Command comm = commandMap.get(args[0].toLowerCase());
			if (comm == null) {
				player.getPacketDispatcher().sendMessage("Command " + args[0] + " does not exist.");
				return;
			}
			if (comm.checkRequirements(player)) {
				if (player.getAuthentication().getPlayerRights() >= comm.rightsRequired()) {
					comm.execute(player, command.toLowerCase());
				} else {
					player.getPacketDispatcher().sendMessage("You do not have sufficient rights to access this command.");
				}
			}
		} catch (Exception e) {
			player.getPacketDispatcher().sendMessage("Command " + args[0] + " does not exist.");
		}
	}

}
