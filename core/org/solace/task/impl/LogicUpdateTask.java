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

import org.solace.game.content.minigame.MinigameExecutor;
import org.solace.game.entity.ground.GroundItemHandler;
import org.solace.task.Task;

/**
 * 
 * @author Faris
 */
public class LogicUpdateTask extends Task {
    
    public LogicUpdateTask(){
        Thread minigameExecutor = new MinigameExecutor();
        minigameExecutor.start();
    }

	@Override
	public void execute() {

		/**
		 * Handles the cycle for the ground item lifetime
		 */
		GroundItemHandler.updateGroundItems();
                
	}

}
