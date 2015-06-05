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

/**
 * An executable service.
 *
 * @author Thomas Nappo
 * @author Graham Edgecombe
 */
public abstract class Event {

    /**
     * The amount of cycles to pass before the service is executed.
     */
    private final int delay;

    /**
     * Gets the service's {@link #delay}.
     *
     * @return The amount of cycles to pass before the service is executed.
     */
    public int getDelay() {
        return delay;
    }
    /**
     * The countdown amount of the service. Once this reaches <tt>0</tt> the
     * service's context is executed.
     */
    private int countdown;

    /**
     * Gets the service's {@link #countdown}.
     *
     * @return The countdown amount of the service. Once this reaches <tt>0</tt>
     * the service's context is executed.
     */
    public int getCountdown() {
        return countdown;
    }
    /**
     * Whether or not the service is running.
     */
    protected boolean running = false;

    /**
     * Gets the service's {@link #running} flag.
     *
     * @return <b>true</b> if the service is running.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * Constructs a new service.
     *
     * @param delay The amount of cycles to pass before the service is executed.
     */
    public Event(int delay) {
        this.delay = delay;
        this.countdown = delay;
        this.running = true;
    }

    /**
     * Constructs a new service with a {@link #delay} of <tt>1</tt>.
     */
    public Event() {
        this(1);
    }

    /**
     * Stops the service from executing.
     */
    public void stop() {
        running = false;
    }

    /**
     * Updates the countdown and executes the service's context if the service
     * is ready.
     *
     * @return Whether or not the service is still running.
     */
    public boolean tick() {
        if (running && --countdown <= 0) {
            execute();
            countdown = delay;
        }
        return running;
    }

    /**
     * Executes the context of the service.
     */
    protected abstract void execute();
}
