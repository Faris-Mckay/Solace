/*
 * This file is part of Solace Framework.
 * Solace is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Solace is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Solace. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.solace.network.packet.impl;

import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.player.Player;

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
            player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
    }

}
