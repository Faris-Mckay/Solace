package org.solace.network.packet.impl;

import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.util.ProtocolUtils;
import org.solace.world.game.Game;
import org.solace.world.game.entity.Animation;
import org.solace.world.game.entity.Graphic;
import org.solace.world.game.entity.UpdateFlags.UpdateFlag;
import org.solace.world.game.entity.mobile.Mobile.MovementStatus;
import org.solace.world.game.entity.mobile.npc.NPC;
import org.solace.world.game.entity.mobile.npc.NPCAdvocate;
import org.solace.world.game.entity.mobile.player.Player;
import org.solace.world.game.item.Item;

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
                String firstWord = args[0].toLowerCase(), secondWord = args[1].toLowerCase(), thirdWord = args[2].toLowerCase();;
                
		/**
		 * Temporary for testing
		 */
		switch (firstWord) {
                    case "players":
                            player.getPacketDispatcher().sendMessage("There are currently "+Game.playerRepository.values().size()+" player(s) online.");
                            break;
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
                            NPC npc = new NPC(Integer.parseInt(secondWord));
                            npc.setMoveStatus(MovementStatus.STATIONARY);
                            npc.setLocation(player.getLocation());
                            NPCAdvocate.addNpc(npc);
                            break;
                    }
	}

}
