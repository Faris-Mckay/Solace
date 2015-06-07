/*
 * This file is part of Solace Framework.
 * Solace is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Solace is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Solace. If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
		super(500);
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
