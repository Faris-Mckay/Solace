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
package org.solace.task;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A class which schedules the execution of {@link Task}s.
 * 
 * @author Graham
 */
public final class TaskExecuter implements Runnable {

	/**
	 * A logger used to report error messages.
	 */
	private static final Logger logger = Logger.getLogger(TaskExecuter.class.getName());

	/**
	 * The time period, in milliseconds, of a single cycle.
	 */
	private static final int TIME_PERIOD = 600;

	/**
	 * The {@link ScheduledExecutorService} which schedules calls to the
	 * {@link #run()} method.
	 */
	private final ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();

	/**
	 * A list of active tasks.
	 */
	private final List<Task> tasks = new ArrayList<Task>();

	/**
	 * A queue of tasks that still need to be added.
	 */
	private final Queue<Task> newTasks = new ArrayDeque<Task>();

	/**
	 * Creates and starts the task scheduler.
	 */
	public TaskExecuter() {
		service.scheduleAtFixedRate(this, 0, TIME_PERIOD, TimeUnit.MILLISECONDS);
	}

	/**
	 * Stops the task scheduler.
	 */
	public void terminate() {
		service.shutdown();
	}

	/**
	 * Schedules the specified task. If this scheduler has been stopped with
	 * the {@link #terminate()} method the task will not be executed or
	 * garbage-collected.
	 * 
	 * @param task
	 *            The task to schedule.
	 */
	public void schedule(final Task task) {
		if (task.isImmediate()) {
			service.execute(new Runnable() {
				@Override
				public void run() {
					task.execute();
				}
			});
		}

		synchronized (newTasks) {
			newTasks.add(task);
		}
	}

	/**
	 * This method is automatically called every cycle by the
	 * {@link ScheduledExecutorService} and executes, adds and removes
	 * {@link Task}s. It should not be called directly as this will lead to
	 * concurrency issues and inaccurate time-keeping.
	 */
	@Override
	public void run() {
		synchronized (newTasks) {
			Task task;
			while ((task = newTasks.poll()) != null)
				tasks.add(task);
		}

		for (Iterator<Task> it = tasks.iterator(); it.hasNext();) {
			Task task = it.next();
			try {
				if (!task.tick())
					it.remove();
			} catch (Throwable t) {
				logger.log(Level.SEVERE, "Exception during task execution.", t);
			}
		}
	}

	private static TaskExecuter singleton;

	public static TaskExecuter get() {
		if (singleton == null) {
			singleton = new TaskExecuter();
		}
		return singleton;
	}

}