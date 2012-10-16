package org.solace.event;

/**
 *
 * @author Faris
 */
public class InstantExecutionEvent {
    
    public InstantExecutionEvent(Event event){
        this.toExecute = event;
    }
    
    Event toExecute;
    
    public void executeEvent(){
        toExecute.init();
        toExecute.execute();
        toExecute.stop();
    }

}
