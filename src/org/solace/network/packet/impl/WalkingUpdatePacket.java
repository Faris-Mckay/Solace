package org.solace.network.packet.impl;

import org.solace.game.content.combat.Combat;
import org.solace.game.entity.mobile.MobilityManager;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Location;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;

/**
 * 
 * @author Faris
 */
public class WalkingUpdatePacket implements PacketHandler {

	public static final int COMMAND_MOVEMENT_OPCODE = 98;
	public static final int GAME_MOVEMENT_OPCODE = 164;
	public static final int MINIMAP_MOVEMENT_OPCODE = 248;

	@Override
	public void handlePacket(Player player, Packet packet) {
		if (packet.opcode() == 248) {
			packet.length(packet.length() - 14);
		}
		player.getPacketDispatcher().sendCloseInterface();
		Combat.resetCombat(player);
		if ((Boolean) player.getAttribute("FROZEN")) {
			player.getPacketDispatcher().sendMessage("A magical force prevents you from moving.");
			return;
		}
		if ((Boolean) player.getAttribute("STUNNED") == false) {
			MobilityManager queue = player.getMobilityManager();
			queue.prepare();
			int steps = (packet.length() - 5) / 2;
			int[][] path = new int[steps][2];
			int firstStepX = packet.getLEShortA();
			for (int i = 0; i < steps; i++) {
				path[i][0] = packet.getByte();
				path[i][1] = packet.getByte();
			}
			int firstStepY = packet.getLEShort();
			queue.queueDestination(new Location(firstStepX, firstStepY));
			for (int i = 0; i < steps; i++) {
				path[i][0] += firstStepX;
				path[i][1] += firstStepY;
				queue.queueDestination(new Location(path[i][0], path[i][1]));
			}
			queue.finish();
		} else {
			player.getPacketDispatcher().sendMessage("You are currently stunned.");
		}

		/*
		 * Reset the walk to action task.
		 */
		if (player.walkToAction() != null) {
			player.walkToAction().stop();
			player.walkToAction(null);
		}

	}

}
