package org.solace.network.packet.impl;

import org.solace.Server;
import org.solace.event.impl.ProcessCommandEvent;
import org.solace.game.entity.mobile.player.Player;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.util.ProtocolUtils;

/**
 * 
 * @author Faris
 * 
 */
public class CommandPacket implements PacketHandler {

	@Override
	public void handlePacket(Player player, Packet packet) {
		String command = ProtocolUtils.getRSString(packet.buffer());
		Server.getEventManager().dispatchEvent(new ProcessCommandEvent(player, command));
		//String[] args = command.split(" ");
		/*String firstWord = args[0].toLowerCase();
		String secondWord;
		switch (firstWord) {
		case "players":
			player.getPacketDispatcher().sendMessage("There are currently "+ Game.playerRepository.values().size() + " player(s) online.");
			break;
		case "spec":
			player.setSpecialAmount(1000);
			player.getEquipment().sendWeapon(player);
			break;
		case "master":
			for (int i = 0; i < SkillHandler.MAXIMUM_SKILLS; i++) {
				player.getSkills().getPlayerLevel()[i] = player.getSkills().getLevelForXP(player.getSkills().getXPForLevel(99));
				player.getSkills().getPlayerExp()[i] = player.getSkills().getXPForLevel(99);
				player.getSkills().refreshSkill(i);
			}
			break;
		case "empty":
			for (int i = 0; i < 28; i++) {
				player.getInventory().set(i, -1, 0);
				player.getInventory().refreshItems();
			}
			break;
		case "char":
			player.getPacketDispatcher().sendInterface(3559);
			break;
		case "coords":
		case "mypos":
			player.getAdvocate().displayChatboxText("X: " + player.getLocation().getX() + " Y: " + player.getLocation().getY());
			break;
		}
		if (args.length != 1) {
			secondWord = args[1].toLowerCase();
			switch (firstWord) {
			case "gfx":
				int gfxId = Integer.parseInt(secondWord);
				player.setGraphic(Graphic.lowGraphic(gfxId, 0));
				break;
			case "anim":
				int animId = Integer.parseInt(secondWord);
				player.setAnimation(Animation.create(animId));
				break;
			case "item":
				int itemId = Integer.parseInt(secondWord);
				if (args.length == 2) {
					player.getInventory().add(new Item(itemId));
				} else if (args.length == 3) {
					player.getInventory().add(new Item(itemId, Integer.parseInt(args[2])));
				}
				break;
			case "npc":
				NPCDefinition def = NPCDefinition.getDefinitions()[Integer
						.parseInt(args[1])];
				NPC npc = new NPC(def, Integer.parseInt(secondWord));
				npc.setMoveStatus(MovementStatus.STATIONARY);
				npc.setNpcSpawned(true);
				npc.setLocation(player.getLocation());
				npc.targettedLocation(player.getLocation());
				NPCAdvocate.addNpc(npc);
				break;
			case "setlevel":
				int skill = Integer.parseInt(secondWord);
				int level = Integer.parseInt(args[2]);
				player.getSkills().getPlayerLevel()[skill] = level;
				player.getSkills().getPlayerExp()[skill] = player.getSkills().getXPForLevel(level);
				player.getSkills().refreshSkill(skill);
				break;
			case "xteleto":
				for (Player otherPlayer : Game.playerRepository.values()) {
					if (otherPlayer == null)
						continue;
					if (otherPlayer.getAuthentication().getUsername().equalsIgnoreCase(secondWord)) {
						player.getMobilityManager().processTeleport(player, otherPlayer.getLocation());
						break;
					}
				}
				break;
			case "object":
				int objectId = Integer.parseInt(secondWord);
				//player.getPacketDispatcher().sendObject(
						//new GameObject(player.getLocation(), objectId), false);
				ObjectManager.registerObject(new GameObject(player.getLocation(), objectId, 2213, 200));
				break;
			case "sidebar":
				if (args.length == 3) {
					int sidebarToChange = Integer.parseInt(secondWord);
					int sidebarId = Integer.parseInt(args[2]);
					player.getPacketDispatcher().sendSidebar(sidebarToChange, sidebarId);
					player.getPacketDispatcher().sendMessage("Changed sidebar " + sidebarToChange + " to " + sidebarId);
				}
				break;
			case "tele":
				if (args.length == 3) {
					int x = Integer.parseInt(secondWord);
					int y = Integer.parseInt(args[2]);
					player.getMobilityManager().processTeleport(player, new Location(x, y));
				}
				break;
			}
		}*/

	}

}
