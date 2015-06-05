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
package org.solace.event.listener;

import mint.event.EventHandler;
import mint.event.EventListener;

import org.solace.Server;
import org.solace.event.events.PlayerLoginEvent;
import org.solace.game.Game;
import org.solace.game.content.skills.SkillHandler;
import org.solace.util.Constants;

public class PlayerLoginListener implements EventListener {

    @EventHandler
    public void handleLogin(PlayerLoginEvent event) {
        Server.logger.info("[Registry]: new connection made from player: " + event.getPlayer().getAuthentication().getUsername());
        event.getPlayer().getPacketDispatcher().sendMessage("Welcome " + event.getPlayer().getAuthentication().getUsername() + ", to " + Constants.SERVER_NAME);
        event.getPlayer().getPacketDispatcher().sendMessage("Current players: " + (Game.getPlayerRepository().size() + Game.registryQueue.size()));
        event.getPlayer().getEquipment().refreshItems();
        event.getPlayer().getInventory().refreshItems();
        for (int i = 0; i < SkillHandler.MAXIMUM_SKILLS; i++) {
            event.getPlayer().getSkills().refreshSkill(i);
        }
        event.getPlayer().appendLoginAttributes();
    }

}
