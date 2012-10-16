package org.solace.network.packet;

import org.solace.network.RSChannelContext;

/**
 * Packet handlers manager.
 * @author Faris
 */
public class PacketType {

	private static PacketHandler[] handlers = new PacketHandler[256];

	/**
	 * Handles an incoming packet.
	 * 
	 * @param channelContext
	 *            the channel socket context
	 * 
	 * @param packet
	 *            the packet
	 */
	public static void handlePacket(RSChannelContext channelContext,Packet packet) {
		if (handlers[packet.opcode()] != null) {
			handlers[packet.opcode()].handlePacket(channelContext.player(), packet);
		}
	}

	/**
	 * Static constructor for packet handlers initializing.
	 */
	static {
		
	}
}