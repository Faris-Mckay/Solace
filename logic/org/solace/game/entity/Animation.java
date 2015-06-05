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
package org.solace.game.entity;

public final class Animation {

	/**
	 * The id of the animation
	 */
	private final int id;

	/**
	 * The delay of the animation
	 */
	private final int delay;

	/**
	 * Constructor for a new animation
	 * 
	 * @param id
	 * @param delay
	 */
	private Animation(int id, int delay) {
		this.id = id;
		this.delay = delay;
	}

	/**
	 * Returns the id of the animation
	 * 
	 * @return
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns the delay of the animation
	 * 
	 * @return
	 */
	public int getDelay() {
		return delay;
	}
	
	/**
	 * Creates a new animation with a delay of 0
	 * 
	 * @param id
	 * @return
	 */
	public static Animation create(int id) {
		return create(id, 0);
	}

	/**
	 * Creates an animation with a delay
	 * 
	 * @param id
	 * @param delay
	 * @return
	 */
	public static Animation create(int id, int delay) {
		return new Animation(id, delay);
	}

}