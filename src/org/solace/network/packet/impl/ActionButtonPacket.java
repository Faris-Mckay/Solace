package org.solace.network.packet.impl;

import org.solace.game.content.EmoteButtons;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.game.entity.mobile.player.Player;

/**
 * 
 * @author Faris
 *
 */
public class ActionButtonPacket implements PacketHandler {

	@Override
	public void handlePacket(Player player, Packet packet) {
		int buttonId = packet.getShort();
                player.getPacketDispatcher().sendMessage("ID: "+buttonId);
		switch (buttonId) {
                    case 2458:
                        player.handleLogoutData();
                        break;
                    case 152:
                        player.getMobilityManager().running(false);
                        break;
                    case 153:
                        player.getMobilityManager().running(true);
                        break;
                    case 168:
                    case 169:
                    case 162:
                    case 164:
                    case 165:
                    case 161:
                    case 170:
                    case 171:
                    case 163:
                    case 167:
                    case 172:
                    case 166:
                    case 52050:
                    case 52051:
                    case 52052:
                    case 52053:
                    case 52054:
                    case 52055:
                    case 52056:
                    case 52057:
                    case 52058:
                    case 43092:
                    case 2155:
                    case 25103:
                    case 25106:
                    case 2154:
                    case 52071:
                    case 52072:
                    case 59062:
                    case 72032:
                    case 72033:
                    case 72254:
                            EmoteButtons.handle(player, buttonId);
                    break;
		}

	}

}
