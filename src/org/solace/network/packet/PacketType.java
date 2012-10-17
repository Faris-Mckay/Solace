package org.solace.network.packet;

import org.solace.network.RSChannelContext;
import org.solace.network.packet.impl.AppearanceChangePacket;
import org.solace.network.packet.impl.RegionChangePacket;
import org.solace.network.packet.impl.WalkingUpdatePacket;

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
            handlers[101] = new AppearanceChangePacket();
            
            handlers[121] = new RegionChangePacket();
            
            handlers[WalkingUpdatePacket.COMMAND_MOVEMENT_OPCODE] = new WalkingUpdatePacket();
            handlers[WalkingUpdatePacket.GAME_MOVEMENT_OPCODE] = new WalkingUpdatePacket();
            handlers[WalkingUpdatePacket.MINIMAP_MOVEMENT_OPCODE] = new WalkingUpdatePacket();
	}
}