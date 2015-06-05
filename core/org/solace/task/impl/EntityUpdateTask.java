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

import org.solace.game.Game;
import org.solace.game.entity.mobile.MobileUpdateExecutor;
import org.solace.game.entity.mobile.MobileUpdateTask;
import org.solace.game.entity.mobile.ParallelUpdateTask;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.task.Task;

/**
 * 
 * @author Faris
 */
public class EntityUpdateTask extends Task {

	@Override
	public void execute() {
		/*
		 * Loops through and handles all player content
		 */
		for (Player player : Game.getPlayerRepository().values()) {
			if (player != null) {
				player.update();
			}
		}

		/*
		 * Loops through all NPCs and handles all logic to be updated (e.g
		 * Combat and animations)
		 */
		for (NPC npc : Game.getNpcRepository().values()) {
			if (npc != null) {
				try {
					npc.update();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		/*
		 * Loops through and first includes player, then NPCS in the update
		 */
                MobileUpdateTask task = new ParallelUpdateTask();
                
                
                /**
                 * Unimplemented, for use with machines which don't benefit from multiple cores
                 */
                //MobileUpdateTask task = new SequentialUpdateTask();
                
                
                /**
                 * Ensures the updates are handled in correct ordered 
                 */
                MobileUpdateExecutor.includeTask(task);
    
                
                /**
                 * Updates all awaiting tasks
                 */
                MobileUpdateExecutor.getInstance().executeUpdates();


		/**
		 * Finally, register all players waiting to log in
		 */
		Game.getSingleton().syncCycleRegistrys();
	}

}
