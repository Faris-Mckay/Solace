package org.solace.task.impl;

import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.solace.task.Task;

/**
 * Maintained side by side tasks to be executed upon submission
 * @author Faris
 */
public class ConcurrentTask extends Task {

    @Override
    protected void execute() {
        try {
            throw new Exception("ConcurrentTask null execution submitted");
        } catch (Exception ex) {
            Logger.getLogger(ConcurrentTask.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
