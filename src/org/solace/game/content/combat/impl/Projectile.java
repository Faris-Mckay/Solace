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
