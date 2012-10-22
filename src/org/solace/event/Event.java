package org.solace.event;

/**
 *
 * @author Faris
 */
public abstract class Event {
    
    public Event(EventType eventType){
        this.eventType = eventType;
    }
    
    public EventType eventType;
    
    /**
     * Optional to be called, can be used to set up your event before execution
     * is called instantly upon submission
     */
    public void init(){
        //DEFAULT INIT METHOD, MUST OVERRIDE TO GAIN USAGE
    }
    
    public abstract void execute();
    
    /**
     * Optional to be called upon event ending
     */
    public void stop(){
        //DEFAULT STOP METHOD, MUST OVERRIDE TO GAIN USAGE
    }
    
    public enum EventType{
        /**
         * The event creates its own engine during runtime
         * for use with global events, such as mini games
         */
        Standalone,
        
        /**
         * The event runs on the individual players game cycle
         * for use with individual tasks such as skilling
         */
        Core
    }

}
