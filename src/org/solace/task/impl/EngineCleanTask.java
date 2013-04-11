package org.solace.task.impl;

import org.solace.Server;
import org.solace.task.Task;

/**
 * Some may not see this as necessary but after some testing it has proven
 * positively influence memory management to request GC sooner than the JVM self
 * cleans
 * 
 * @author Faris
 */
public class EngineCleanTask extends Task {
	
	public EngineCleanTask() {
		super(100);
		System.gc();
		System.runFinalization();
		Server.logger.info("System resource cleanup scheduled...");
	}

	public void execute() {
		System.gc();
		System.runFinalization();
		Server.logger.info("System resource cleanup scheduled...");
	}

}
