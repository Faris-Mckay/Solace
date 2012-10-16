package org.solace.event;

/**
 *
 * @author Faris
 */
public abstract class Event {
    
    public void init(){
        
    }
    
    public abstract void execute();
    
    public void stop(){
        
    }

}
