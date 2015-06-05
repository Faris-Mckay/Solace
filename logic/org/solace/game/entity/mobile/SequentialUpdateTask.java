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
package org.solace.game.entity.mobile;


import java.util.Map;
import org.solace.game.Game;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.npc.NPCUpdateTask;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.mobile.player.PlayerUpdateTask;
import org.solace.game.entity.mobile.update.PostUpdateExecutor;
import org.solace.game.entity.mobile.update.PreUpdateExecutor;

/**
 *
 * @author Faris
 */
public class SequentialUpdateTask extends MobileUpdateTask {

    @Override
    public void synchronize() {
        Map<Integer, Player> players = Game.getPlayerRepository();

		for (Player player : players.values()) {
			MobileUpdateTask task = new PreUpdateExecutor(player);
                        task.run();
		}

		for (Player player : players.values()) {
			MobileUpdateTask task = player.getUpdater();
                        task.synchronize();
		}

		for (Player player : players.values()) {
			MobileUpdateTask task = new PostUpdateExecutor(player);
                        task.run();
		}
                for (Player player : players.values()) {
			player.getUpdater().resetUpdateVars();
		}
                for (Player player : players.values()) {   
                        MobileUpdateTask task = player.getNpcUpdating();
                        task.run();
                }
		for (Player player : Game.getPlayerRepository().values()) {
				player.getUpdater().resetUpdateVars();
		}
		for (NPC npc : Game.getNpcRepository().values()) {
                    try {
                            npc.getUpdateFlags().reset();
                    } catch (Exception e) {
                            e.printStackTrace();
                    }
                }
    }

    @Override
    public void run() {
        synchronize();
    }
    
}
