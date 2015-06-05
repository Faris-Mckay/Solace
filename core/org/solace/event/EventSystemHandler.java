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
package org.solace.event;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Schedules {@link Event}s to be ran at the proper time.
 *
 * @author Thomas Nappo
 * @author Graham Edgecombe
 */
public class EventSystemHandler implements Runnable {

    /**
     * Constructs a new service delegate.
     *
     * @param span The time units which need to pass between each service
     * execution.
     * @param unit Time {@link TimeUnit} of the cycles.
     */
    public EventSystemHandler(int span, TimeUnit unit) {
        service.scheduleAtFixedRate(this, 0, span, unit);
    }

    /**
     * Constructs a new service delegate which cycles in
     * {@link TimeUnit#MILLISECONDS}.
     *
     * @param span The time units which need to pass between each service
     * execution.
     */
    public EventSystemHandler(int span) {
        this(span, TimeUnit.MILLISECONDS);
    }
    /**
     * Schedules this delegate to be ran at the {@link #span} rate.
     */
    private final ScheduledExecutorService service = Executors
            .newSingleThreadScheduledExecutor();
    /**
     * Lists the managed services of the delegate.
     */
    private final List<Event> services = new ArrayList<Event>();
    /**
     * Queues the services which are still appending.
     */
    private final Queue<Event> serviceQueue = new ArrayDeque<Event>();

    /**
     * Schedules a new service to the delegate.
     *
     * @param service The service to schedule.
     */
    public void schedule(Event service) {
        synchronized (serviceQueue) {
            serviceQueue.add(service);
        }
    }

    @Override
    public void run() {
        synchronized (serviceQueue) {
            Event service;
            while ((service = serviceQueue.poll()) != null) {
                services.add(service);
            }
        }

        for (Iterator<Event> it = services.iterator(); it.hasNext();) {
            Event service = it.next();
            try {
                if (!service.tick()) {
                    it.remove();
                }
            } catch (Throwable t) {
            }
        }
    }
}
