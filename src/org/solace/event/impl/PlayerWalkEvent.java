package org.solace.event.impl;

import org.solace.world.map.Location;


/**
 * An event which the client sends to request that the player walks somewhere.
 * @author Graham
 */
public final class PlayerWalkEvent {

	/**
	 * The steps.
	 */
	private final Location[] steps;

	/**
	 * The running flag.
	 */
	private boolean run;

	/**
	 * Creates the event.
	 * @param steps The steps array.
	 * @param run The run flag.
	 */
	public PlayerWalkEvent(Location[] steps, boolean run) {
		if (steps.length < 0) {
			throw new IllegalArgumentException("number of steps must not be negative");
		}
		this.steps = steps;
		this.run = run;
	}

	/**
	 * Gets the steps array.
	 * @return An array of steps.
	 */
	public Location[] getSteps() {
		return steps;
	}

	/**
	 * Checks if the steps should be ran (ctrl+click).
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isRunning() {
		return run;
	}

}
