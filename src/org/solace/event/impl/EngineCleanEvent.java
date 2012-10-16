package org.solace.event.impl;

import org.solace.task.Task;
import org.solace.task.UniqueTimedTask;

/**
 * Some may not see this as necessary but after some testing it has proven
 * positively influence memory management to request GC sooner than the JVM self cleans
 * @author Faris
 */
public class EngineCleanEvent {
    
    public static void init(){
        System.out.println("Submitting Resource Cleanup task...");
        new UniqueTimedTask(5, new Task(){
            @Override
            public void execute(){
                System.gc();
                System.runFinalization();
                System.out.println("System object cleanup scheduled");
            }
        });
    }

}
