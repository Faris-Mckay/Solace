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

import org.solace.game.content.skills.SkillHandler;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.mobile.player.PrivilegeRank;
import org.solace.game.entity.mobile.player.command.Command;

/**
 *
 * @author Faris <https://github.com/faris-mckay>
 */
public class MasterStatsCommand extends Command {

    @Override
    public void handle(Player player, String command) {
        for (int i = 0; i < SkillHandler.MAXIMUM_SKILLS; i++) {
            player.getSkills().getPlayerLevel()[i] = player.getSkills().getLevelForXP(player.getSkills().getXPForLevel(99));
            player.getSkills().getPlayerExp()[i] = player.getSkills().getXPForLevel(99);
            player.getSkills().refreshSkill(i);
        }
    }

    @Override
    public PrivilegeRank rightsRequired() {
        return PrivilegeRank.ADMINISTRATOR;
    }
    
}
