package org.solace.task.impl;

import org.solace.event.CoreEventExecutor;
import org.solace.game.entity.GroundItemHandler;
import org.solace.task.Task;

/**
 *
 * @author Faris
 */
public class LogicUpdateTask extends Task {
    
    public LogicUpdateTask() throws InterruptedException{
        CoreEventExecutor.getSingleton().execute();
    }

    @Override
    public void execute() {
        
        
        /**
         * Handles the cycle for the ground item lifetime
         */
        GroundItemHandler.updateGroundItems();
    }

}
