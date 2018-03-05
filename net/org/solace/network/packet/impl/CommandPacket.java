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

import org.solace.Server;
import org.solace.event.events.ProcessCommandEvent;
import org.solace.game.entity.mobile.player.Player;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.util.ProtocolUtils;

/**
 *
 * @author Faris <https://github.com/faris-mckay>
 *
 */
public class CommandPacket implements PacketHandler {

    @Override
    public void handlePacket(Player player, Packet packet) {
        String command = packet.getRS2String();
        Server.getEventManager().dispatchEvent(new ProcessCommandEvent(player, command));
    }

}
