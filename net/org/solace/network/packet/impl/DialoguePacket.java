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

import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;

public class DialoguePacket implements PacketHandler {

    public static final int DIALOGUE_OPCODE = 40;

    @Override
    public void handlePacket(Player player, Packet packet) {
        if (player.getInteractingEntity() instanceof NPC) {
            NPC npc = (NPC) player.getInteractingEntity();
            if (player.getDialogue().getDialogueId() > 0) {
                player.getDialogue().sendDialogue(
                        player.getDialogue().getDialogueId(), npc.getNpcId());
            } else {
                player.getPacketDispatcher().sendCloseInterface();
            }
        } else {
            player.getPacketDispatcher().sendCloseInterface();
        }

    }

}
