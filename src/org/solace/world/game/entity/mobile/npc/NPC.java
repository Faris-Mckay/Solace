package org.solace.world.game.entity.mobile.npc;

import org.solace.world.game.entity.mobile.Mobile;
import org.solace.world.map.Location;

/**
 * Represents a single NPC entity
 * @author Faris
 * @author Arithium
 */
public class NPC extends Mobile {

	/**
	 * Constructs a new NPC entity
	 */
	public NPC(int npcId) {
		super(new Location(3222, 3222));
		this.npcId = npcId;
	}

	/**
	 * Determines whether the npc is visible or not
	 */
	private boolean isVisible;
	
	/**
	 * The npcs current npc id
	 */
	private int npcId;

	@Override
	public void update() {
		getMobilityManager().processMovement();
	}

	public void kill() {

	}

	/**
	 * Returns whether the npc is visible or not
	 * @return
	 */
	public boolean isNpcVisible() {
		return isVisible;
	}

	/**
	 * Sets whether the npc is visible or not
	 * @param visible
	 */
	public void setVisible(boolean visible) {
		this.isVisible = visible;
	}
	
	/**
	 * returns the npcs id
	 * @return
	 */
	public int getNpcId() {
		return npcId;
	}

}
