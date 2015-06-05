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
package org.solace.task.impl;

import org.solace.Server;
import org.solace.event.events.PlayerSaveEvent;
import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class ShutdownExecutionTask extends Thread {
    
    @Override
    public void run(){
        Server.logger.warning("Server unexpected shutdown, hook executed");
        for(Player player : Game.getPlayerRepository().values()){
            if (player == null){
                continue;
            }
            Server.getEventManager().dispatchEvent(new PlayerSaveEvent(player));
        }
    }

}
