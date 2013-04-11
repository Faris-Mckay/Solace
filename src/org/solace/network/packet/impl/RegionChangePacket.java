package org.solace.network.packet.impl;

import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.game.entity.grounded.GroundItemHandler;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.object.ObjectManager;

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
        /*
		 * Refresh the ground items.
		 */
		GroundItemHandler.sendRemoveGroundItems(player);
		GroundItemHandler.sendGroundItems(player);
		/**
		 * Refresh the objects.
		 */
		ObjectManager.updateObject(player);
    }

}
