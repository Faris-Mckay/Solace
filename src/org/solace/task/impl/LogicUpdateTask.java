package org.solace.task.impl;

import org.solace.game.content.minigame.MinigameExecutor;
import org.solace.game.entity.grounded.GroundItemHandler;
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
