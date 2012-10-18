package org.solace.network.packet.impl;

import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class RegionChangePacket implements PacketHandler {

    @Override
    public void handlePacket(Player player, Packet packet) {
        
        /**
         * Updates the new song for the players region
         */
        player.getMusicHandler().handleMusic();
    }

}
