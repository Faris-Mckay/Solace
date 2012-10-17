package org.solace.network.packet.impl;

import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class AppearanceChangePacket implements PacketHandler {

    @Override
    public void handlePacket(Player player, Packet packet) {
            player.getAuthentication().setPlayerAppearanceIndex(0, packet.getByte());
            player.getAuthentication().setPlayerAppearanceIndex(1, packet.getByte());
            player.getAuthentication().setPlayerAppearanceIndex(7, packet.getByte());
            player.getAuthentication().setPlayerAppearanceIndex(2, packet.getByte());
            player.getAuthentication().setPlayerAppearanceIndex(3, packet.getByte());
            player.getAuthentication().setPlayerAppearanceIndex(4, packet.getByte());
            player.getAuthentication().setPlayerAppearanceIndex(5, packet.getByte());
            player.getAuthentication().setPlayerAppearanceIndex(6, packet.getByte());
            player.getAuthentication().setPlayerAppearanceIndex(8, packet.getByte());
            player.getAuthentication().setPlayerAppearanceIndex(9, packet.getByte());
            player.getAuthentication().setPlayerAppearanceIndex(10, packet.getByte());
            player.getAuthentication().setPlayerAppearanceIndex(11, packet.getByte());
            player.getAuthentication().setPlayerAppearanceIndex(12, packet.getByte());
            player.getUpdateFlags().setAppearanceUpdateRequired(true);
    }

}
