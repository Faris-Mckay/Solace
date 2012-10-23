package org.solace.network.packet.impl;

import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;

public class DialoguePacket implements PacketHandler {

	public static final int DIALOGUE_OPCODE = 40;

	@Override
	public void handlePacket(Player player, Packet packet) {
		NPC npc = (NPC) player.getInteractingEntity();
		if (player.getDialogue().getDialogueId() > 0) {
			player.getDialogue().sendDialogue(
					player.getDialogue().getDialogueId(), npc.getNpcId());
		} else {
			player.getPacketDispatcher().sendCloseInterface();
		}

	}

}
