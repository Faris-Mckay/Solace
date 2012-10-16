package org.solace.task.impl;

import org.solace.network.NIOSelector;
import org.solace.task.ConcurrentTask;

/**
 * Task which maintains listening on the network for connections
 * @author Faris
 */
public class MaintainedNetworkTask extends ConcurrentTask {
    
    public MaintainedNetworkTask(NIOSelector selector){
        this.selector = selector;
    }
    
    NIOSelector selector;

    @Override
    public void execute() {
        selector.select();
    }
}
