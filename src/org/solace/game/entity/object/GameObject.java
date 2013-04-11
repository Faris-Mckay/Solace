package org.solace.game.entity.object;

import org.solace.game.map.Location;

/**
 * Represents a single game object
 * 
 * @author Faris
 * 
 */
public class GameObject {

	/**
	 * The location of the object
	 */
	private final Location location;

	/**
	 * The id of the object
	 */
	private final int objectId;

	/**
	 * The replacement id of the object
	 */
	private int replacementId;
	
	/**
	 * How long the object will exist
	 */
	private int lifeCycle;
	
	private boolean expireable;
	
	public GameObject(Location location, int objectId) {
		this.location = location;
		this.objectId = objectId;
		this.expireable = false;
	}

	/**
	 * Constructs a new game object
	 * 
	 * @param location
	 * @param objectId
	 * @param replacementId
	 */
	public GameObject(Location location, int objectId, int replacementId, int lifeCycle) {
		this.location = location;
		this.objectId = objectId;
		this.replacementId = replacementId;
		this.lifeCycle = lifeCycle;
		this.expireable = true;
	}

	/**
	 * Returns the objects location
	 * 
	 * @return
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * Returns the objects id
	 * 
	 * @return
	 */
	public int getObjectId() {
		return objectId;
	}

	/**
	 * Returns the replacement object id
	 * 
	 * @return
	 */
	public int getReplacementId() {
		return replacementId;
	}
	
	/**
	 * Returns how long the object will exist
	 * @return
	 */
	public int getLifeCycle() {
		return lifeCycle;
	}
	
	public void setLifeCycle(int cycle) {
		this.lifeCycle = cycle;
	}
	
	public boolean isExpireable() {
		return expireable;
	}

}
