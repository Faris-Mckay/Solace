package org.solace.network.packet;

import org.solace.network.RSChannelContext;
import org.solace.network.packet.impl.AppearanceChangePacket;
import org.solace.network.packet.impl.IncomingChatPacket;
import org.solace.network.packet.impl.RegionChangePacket;
import org.solace.network.packet.impl.WalkingUpdatePacket;

/**
 * Packet handlers manager.
 * @author Faris
 */
public class PacketType {

	private static PacketHandler[] incomingPacket = new PacketHandler[256];

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
		if (incomingPacket[packet.opcode()] != null) {
			incomingPacket[packet.opcode()].handlePacket(channelContext.player(), packet);
		}
	}

	/**
	 * Static constructor for packet handlers initializing.
	 */
	static {
            incomingPacket[101] = new AppearanceChangePacket();
            incomingPacket[4] = new IncomingChatPacket();
            incomingPacket[121] = new RegionChangePacket();
            
            incomingPacket[WalkingUpdatePacket.COMMAND_MOVEMENT_OPCODE] = new WalkingUpdatePacket();
            incomingPacket[WalkingUpdatePacket.GAME_MOVEMENT_OPCODE] = new WalkingUpdatePacket();
            incomingPacket[WalkingUpdatePacket.MINIMAP_MOVEMENT_OPCODE] = new WalkingUpdatePacket();
	}
}