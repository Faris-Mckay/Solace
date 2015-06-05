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
