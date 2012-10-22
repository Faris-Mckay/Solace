package org.solace.network.packet;

import java.util.HashMap;
import java.util.Map;

import org.solace.network.RSChannelContext;
import org.solace.network.packet.impl.ActionButtonPacket;
import org.solace.network.packet.impl.AppearanceChangePacket;
import org.solace.network.packet.impl.CommandPacket;
import org.solace.network.packet.impl.IncomingChatPacket;
import org.solace.network.packet.impl.ClickingIngamePacket;
import org.solace.network.packet.impl.NPCInteractionPacket;
import org.solace.network.packet.impl.ObjectInteractionPacket;
import org.solace.network.packet.impl.PlayeInteractionPacket;
import org.solace.network.packet.impl.PrivateMessagingPacket;
import org.solace.network.packet.impl.RegionChangePacket;
import org.solace.network.packet.impl.WalkingUpdatePacket;

/**
 * Packet handlers manager.
 * 
 * @author Faris
 * @author Arithium
 */
public class PacketType {

	private static Map<Integer, PacketHandler> incomingPacket = new HashMap<Integer, PacketHandler>();

	/**
	 * Handles an incoming packet.
	 * 
	 * @param channelContext
	 *            the channel socket context
	 * 
	 * @param packet
	 *            the packet
	 */
	public static void handlePacket(RSChannelContext channelContext, Packet packet) {
		PacketHandler packets = incomingPacket.get(packet.opcode());
		if (packets == null){
                    //System.out.println("Unhandled Packet Type: " + packet.opcode());
                    return;
                }
		if (packet.opcode() < 0 || packet.opcode() > 256)
			return;
		try {
			packets.handlePacket(channelContext.player(), packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Static constructor for packet handlers initializing.
	 */
	static {
		PrivateMessagingPacket privateMessage = new PrivateMessagingPacket();
		incomingPacket.put(PrivateMessagingPacket.ADD_FRIEND_OPCODE, privateMessage);
		incomingPacket.put(PrivateMessagingPacket.ADD_IGNORE_OPCODE, privateMessage);
		incomingPacket.put(PrivateMessagingPacket.REMOVE_FRIEND_OPCODE, privateMessage);
		incomingPacket.put(PrivateMessagingPacket.REMOVE_IGNORE_OPCODE, privateMessage);
		incomingPacket.put(PrivateMessagingPacket.SEND_PM_OPCODE, privateMessage);
                
                NPCInteractionPacket npcInteraction = new NPCInteractionPacket();
                incomingPacket.put(npcInteraction.FIRST_CLICK, npcInteraction);
		incomingPacket.put(npcInteraction.SECOND_CLICK, npcInteraction);
		incomingPacket.put(npcInteraction.THIRD_CLICK, npcInteraction);
		incomingPacket.put(npcInteraction.FOURTH_CLICK, npcInteraction);
		incomingPacket.put(npcInteraction.ATTACK, npcInteraction);
		incomingPacket.put(npcInteraction.MAGIC_ON_NPC, npcInteraction);
		incomingPacket.put(npcInteraction.ITEM_ON_NPC, npcInteraction);
                
                ObjectInteractionPacket objectPacket = new ObjectInteractionPacket();
		incomingPacket.put(132, objectPacket);
		incomingPacket.put(252, objectPacket);
		incomingPacket.put(70, objectPacket);
                
                incomingPacket.put(241, new ClickingIngamePacket());
		incomingPacket.put(PlayeInteractionPacket.ATTACK, new PlayeInteractionPacket());
		incomingPacket.put(185, new ActionButtonPacket());
		incomingPacket.put(103, new CommandPacket());
		incomingPacket.put(101, new AppearanceChangePacket());
		incomingPacket.put(4, new IncomingChatPacket());
		incomingPacket.put(121, new RegionChangePacket());
		incomingPacket.put(WalkingUpdatePacket.COMMAND_MOVEMENT_OPCODE, new WalkingUpdatePacket());
		incomingPacket.put(WalkingUpdatePacket.GAME_MOVEMENT_OPCODE, new WalkingUpdatePacket());
		incomingPacket.put(WalkingUpdatePacket.MINIMAP_MOVEMENT_OPCODE, new WalkingUpdatePacket());
	}
}