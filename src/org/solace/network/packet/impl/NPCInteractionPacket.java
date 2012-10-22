package org.solace.network.packet.impl;

import org.solace.game.entity.mobile.player.Player;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;

/**
 *
 * @author Faris
 */
public class NPCInteractionPacket implements PacketHandler {
    
    public static final int FIRST_CLICK = 155;
    public static final int SECOND_CLICK = 17;
    public static final int THIRD_CLICK = 21;
    public static final int FOURTH_CLICK = 230;
    public static final int ATTACK = 72;
    public static final int MAGIC_ON_NPC = 131;
    public static final int ITEM_ON_NPC = 57;

    @Override
    public void handlePacket(Player player, Packet packet) {
       
    }

}
