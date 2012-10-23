package org.solace.task.impl;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.solace.event.CoreEventExecutor;
import org.solace.game.entity.GroundItemHandler;
import org.solace.task.Task;

/**
 *
 * @author Faris
 */
public class LogicUpdateTask extends Task {

    @Override
    public void execute() {
        
        /**
         * Executors all of the global events waiting 
         */
        try {
            CoreEventExecutor.getSingleton().execute();
        } catch (InterruptedException ex) {
            Logger.getLogger(LogicUpdateTask.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        /**
         * Handles the cycle for the ground item lifetime
         */
        GroundItemHandler.updateGroundItems();
    }

}
