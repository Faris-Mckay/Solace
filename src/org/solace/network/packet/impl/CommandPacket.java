package org.solace.network.packet.impl;

import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.util.ProtocolUtils;
import org.solace.game.Game;
import org.solace.game.entity.Animation;
import org.solace.game.entity.Graphic;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.Mobile.MovementStatus;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.npc.NPCAdvocate;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;

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
                switch(firstWord){
                    case "players":
                            player.getPacketDispatcher().sendMessage("There are currently "+Game.playerRepository.values().size()+" player(s) online.");
                            break;
                }
		if(args.length != 1){
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
                            NPC npc = new NPC(Integer.parseInt(secondWord));
                            npc.setMoveStatus(MovementStatus.STATIONARY);
                            npc.setLocation(player.getLocation());
                            NPCAdvocate.addNpc(npc);
                            break;
                    }
                }
		
	}

}
