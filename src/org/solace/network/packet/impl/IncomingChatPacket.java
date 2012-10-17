package org.solace.network.packet.impl;

import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.util.ProtocolUtils;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class IncomingChatPacket implements PacketHandler {

    @Override
    public void handlePacket(Player player, Packet packet) {
        int effects = packet.getUByteS();
        int color = packet.getUByteS();
        int length = packet.length() - 2;
        byte[] text = packet.getBytesA(length);
        player.getUpdater().chatTextEffects(effects).chatTextColor(color).chatText(text);
        player.getUpdateFlags().setChatUpdateRequired(true);
    }

}
