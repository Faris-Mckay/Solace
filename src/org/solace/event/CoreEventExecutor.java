package org.solace.event;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Faris
 */
public final class CoreEventExecutor {
    
    public static CoreEventExecutor getSingleton(){
        return coreEventExecutor;
    }
    
    private static final CoreEventExecutor coreEventExecutor = new CoreEventExecutor();
    
    /**
     * Stores the global event objects for execution
     */
    private List<Event> events = new ArrayList<Event>();
    
    /**
     * Submits an event into the executory cycle
     * @param event 
     */
    public void submitEvent(Event event){
        if(event.isInstant())
            event.execute();
        events.add(event);
        event.init();
    }
    
    /**
     * Removes an event from the executory cycle 
     * @param event 
     */
    public void removeEvent(Event event){
        event.stop();
        events.remove(event);
    }
    
    /**
     * Used to idle the event manager when no events current need running
     */
    private boolean idle = true;
    
    /**
     * the main loop of the event manager, executes the events, and then sleeps till next cycle
     * @throws InterruptedException 
     */
    public void execute() throws InterruptedException{
        while(!idle){
            long time = System.currentTimeMillis();
            if(events.isEmpty()){
                return;
            }
            for(Event event : events){
                if(event.isShouldEnd()){
                    removeEvent(event);
                }
                if(event.cyclesPassed < event.getExecuteInterval()){
                    event.cyclesPassed++;
                } else {
                    event.cyclesPassed = 0;
                    event.execute();
                }
            }
            long elapsedTime = (System.currentTimeMillis() - time);
            Thread.sleep(600 - elapsedTime);
        }
    }
    

}
