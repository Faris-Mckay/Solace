package org.solace.task.impl;

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
