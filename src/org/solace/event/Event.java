package org.solace.event;

/**
 *
 * @author Faris
 */
public abstract class Event {
    
    public int cyclesPassed = 0;
    
    private int executeInterval;
    private boolean shouldEnd, instant;
    private EventType eventType;
    
    public Event(EventType eventType, int executeInterval, boolean instant){
        this.eventType = eventType;
        this.executeInterval = executeInterval;
        this.instant = instant;
    }
    
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

    /**
     * @return the eventType
     */
    public EventType getEventType() {
        return eventType;
    }

    /**
     * @return the shouldEnd
     */
    public boolean isShouldEnd() {
        return shouldEnd;
    }

    /**
     * @param shouldEnd the shouldEnd to set
     */
    public void setShouldEnd(boolean shouldEnd) {
        this.shouldEnd = shouldEnd;
    }

    /**
     * @return the executeInterval
     */
    public int getExecuteInterval() {
        return executeInterval;
    }

    /**
     * @return the instant
     */
    public boolean isInstant() {
        return instant;
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
