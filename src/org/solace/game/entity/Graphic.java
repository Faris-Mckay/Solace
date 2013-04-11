package org.solace.game.entity;

/**
 * 
 * @author Faris
 */
public class Graphic {

	private final int id, height, delay;

	public Graphic(int id, int delay, int height) {
		this.id = id;
		this.height = height;
		this.delay = delay;
	}

	public static Graphic highGraphic(int id, int delay) {
		return new Graphic(id, delay, 100);
	}

	public static Graphic lowGraphic(int id, int delay) {
		return new Graphic(id, delay, 0);
	}

	public int getId() {
		return id;
	}

	public int getDelay() {
		return delay;
	}

	public int getValue() {
		return delay | height << 16;
	}

	public int getHeight() {
		return height;
	}

}
