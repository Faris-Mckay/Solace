package org.solace.network.packet;

import org.solace.game.entity.mobile.player.Player;

/**
 * Packet handler interface.
 * @author Faris
 */
public interface PacketHandler {

	/**
	 * Handles an incoming packet.
	 * @param player the player reference
	 * @param packet the packet
	 */
	public void handlePacket(Player player, Packet packet);

}