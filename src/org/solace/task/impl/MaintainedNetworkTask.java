package org.solace.task.impl;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.solace.network.NIOSelector;
import org.solace.task.Task;

/**
 * Task which maintains listening on the network for connections
 * @author Faris
 */
public class MaintainedNetworkTask extends Task{
    
    public MaintainedNetworkTask(NIOSelector selector){
        this.selector = selector;
    }
    
    NIOSelector selector;

    @Override
    public void execute() {
        selector.select();
    }

    
}
