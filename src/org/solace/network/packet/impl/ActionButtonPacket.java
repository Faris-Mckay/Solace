package org.solace.network.packet.impl;

import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.world.game.entity.mobile.player.Player;

/**
 * 
 * @author Faris
 *
 */
public class ActionButtonPacket implements PacketHandler {

	@Override
	public void handlePacket(Player player, Packet packet) {
		int buttonId = packet.getShort();
		switch (buttonId) {
		case 2458:
                    player.handleLogoutData();
                    break;
		}

	}

}
