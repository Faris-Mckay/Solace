package org.solace.task.impl;

import org.solace.task.Task;

/**
 * Task to be executed once a new thread
 * @author Faris
 */
public abstract class SingleExecuteTask extends Task {

    
    @Override
    protected void execute() {
        System.out.println("SingleExecutor Left un overriden");
    }
    
    public abstract void exececutionFinalization();

}
