package org.solace.world.game.entity;

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