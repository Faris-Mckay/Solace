package org.solace.event;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Consumes the rest of the activity of the main thread
 * listening and executing tasks which are designed to function
 * on a global basis.
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
    }
    
    /**
     * the main loop of the event manager, executes the events, and then sleeps till next cycle
     * @throws InterruptedException 
     */
    public void execute() throws InterruptedException{
        while(true){
            long time = System.currentTimeMillis();
            if(!events.isEmpty()){
                Iterator<Event> it = events.iterator();
                while(it.hasNext()) {
                Event event = it.next();
                    if(event.isShouldEnd()){
                        removeEvent(event);
                        it.remove();
                    }
                    if(event.cyclesPassed < event.getExecuteInterval()){
                        event.cyclesPassed++;
                    } else {
                        event.cyclesPassed = 0;
                        event.execute();
                    }
                }
            }
            long elapsedTime = (System.currentTimeMillis() - time);
            Thread.sleep(600 - elapsedTime);
        }
    }
    

}
