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
package org.solace.game.content.combat.impl;

import org.solace.game.map.Location;

/**
 * Creates a new instance of a projectile
 * @author Arithium
 *
 */
public class Projectile {
	
	private Location location;
	
	private final int offsetX;
	
	private final int offsetY;
	
	private final int id;
	
	private final int startHeight;
	
	private final int endHeight;
	
	private final int speed;
	
	private final int lockon;
	
	public Projectile(Location location, int offsetX, int offsetY, int id, int startHeight, int endHeight, int speed, int lockon) {
		this.location = location;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.id = id;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.speed = speed;
		this.lockon = lockon;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public int getOffsetX() {
		return offsetX;
	}
	
	public int getOffsetY() {
		return offsetY;
	}
	
	public int getId() {
		return id;
	}
	
	public int getStartHeight() {
		return startHeight;
	}
	
	public int getEndHeight() {
		return endHeight;
	}
	
	public int getSpeed() {
		return speed;
	}
	
	public int getLockon() {
		return lockon;
	}
}
