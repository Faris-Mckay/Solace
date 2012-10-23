package org.solace.network.packet.impl;

import org.solace.game.Game;
import org.solace.game.entity.Animation;
import org.solace.game.entity.Graphic;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.Mobile.MovementStatus;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.npc.NPCAdvocate;
import org.solace.game.entity.mobile.npc.NPCDefinition;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
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
		String[] args = command.split(" ");
		String firstWord = args[0].toLowerCase();
		String secondWord;
		switch (firstWord) {
                    case "players":
			player.getPacketDispatcher().sendMessage("There are currently "+ Game.playerRepository.values().size()+ " player(s) online.");
			break;
                    case "coords":
                            player.getAdvocate().displayChatboxText("X: "+player.getLocation().getX()+ " Y: "+player.getLocation().getY());
                            break;
		}
		if (args.length != 1) {
			secondWord = args[1].toLowerCase();
			switch (firstWord) {
			case "gfx":
				int gfxId = Integer.parseInt(secondWord);
				player.setGraphic(Graphic.lowGraphic(gfxId, 0));
				player.getUpdateFlags().flag(UpdateFlag.GRAPHICS);
				break;
			case "anim":
				int animId = Integer.parseInt(secondWord);
				player.setAnimation(Animation.create(animId));
				player.getUpdateFlags().flag(UpdateFlag.ANIMATION);
				break;
			case "item":
				int itemId = Integer.parseInt(secondWord);
				player.getInventory().add(new Item(itemId));
				break;
			case "npc":
				NPCDefinition def =NPCDefinition.getDefinitions()[Integer.parseInt(args[1])];
				NPC npc = new NPC(def, Integer.parseInt(secondWord));
				npc.setMoveStatus(MovementStatus.MOBILE);
				npc.setVisible(true);
				npc.setLocation(player.getLocation());
				NPCAdvocate.addNpc(npc);
				break;
			case "setlevel":
				int skill = Integer.parseInt(secondWord);
				int level = Integer.parseInt(args[2]);
				player.getSkills().getPlayerLevel()[skill] = level;
				player.getSkills().getPlayerExp()[skill] = player.getSkills().getXPForLevel(level);
				player.getSkills().refreshSkill(skill);
				break;
                        
			}
		}

	}

}
