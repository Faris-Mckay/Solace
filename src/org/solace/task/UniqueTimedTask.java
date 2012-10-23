package org.solace.task;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Faris
 */
public class UniqueTimedTask implements Runnable {
    
    public UniqueTimedTask(int minutes, Task task){
        this.minutes = minutes;
        this.task = task;
        startTask();
    }
    
    Task task;
    Integer minutes;
    
    public void startTask(){
        service.scheduleAtFixedRate(this, 0, minutes, TimeUnit.MINUTES);
    }
    
    private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();


    @Override
    public void run() {
        task.execute();
    }
    
    

}
