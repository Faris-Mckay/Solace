package org.solace.event.impl;

import org.solace.event.Event;

/**
 *
 * @author Faris
 */
public class PlayerSaveEvent extends Event {
    
    public PlayerSaveEvent(){
        super(EventType.Standalone, 0 , false);
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
