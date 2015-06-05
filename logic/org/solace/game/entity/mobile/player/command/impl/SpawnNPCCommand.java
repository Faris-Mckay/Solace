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
package org.solace.game.entity.mobile.player.command.impl;

import org.solace.game.entity.mobile.npc.NPCAdvocate;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.mobile.player.PrivilegeRank;
import org.solace.game.entity.mobile.player.command.Command;

/**
 *
 * @author Faris <https://github.com/faris-mckay>
 */
public class SpawnNPCCommand extends Command {

    @Override
    public void handle(Player player, String command) {
        String[] args = command.split(" ");
        if (args.length == 2) {
            int npcId = Integer.parseInt(args[1]);
            NPCAdvocate.spawnNPC(npcId, player.getLocation(), 0);
        } else {
            player.getPacketDispatcher().sendMessage("Invalid command format.");
        }
    }

    @Override
    public PrivilegeRank rightsRequired() {
        return PrivilegeRank.ADMINISTRATOR;
    }
    
}
