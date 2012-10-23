package org.solace.event;

import java.util.ArrayList;
import java.util.List;
import org.solace.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public final class CycleEventExecutor {
    
    public CycleEventExecutor(Player player){
        this.player = player;
    }
    
    Player player;
    
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
     * Stores the events for each player
     */
    private List<Event> events = new ArrayList<Event>();
    
    /**
     * Handles the execute logic for each event in the list
     */
    public void execute(){
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
    }
}
