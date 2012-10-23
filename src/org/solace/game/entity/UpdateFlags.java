package org.solace.game.entity;

import java.util.BitSet;
import org.solace.game.map.Location;

/**
 * Holds update flags.
 * 
 * @author Graham Edgecombe
 * 
 */
public class UpdateFlags {

	private String forceChatMessage;

	/**
	 * The bitset (flag data).
	 */
	private BitSet flags = new BitSet();
	private Location faceLocation;
	private int hitType, hitType2;

	public String getForceChatMessage() {
		return forceChatMessage;
	}

	/**
	 * Represents a single type of update flag.
	 * 
	 * @author Graham Edgecombe
	 * 
	 */
	public enum UpdateFlag {

		/**
		 * Appearance update.
		 */
		APPEARANCE(0x10),

		/**
		 * Chat update.
		 */
		CHAT(0x80),
		
		/**
		 * Graphics update.
		 */
		GRAPHICS(0x100),

		/**
		 * Animation update.
		 */
		ANIMATION(0x8),

		/**
		 * Forced chat update.
		 */
		FORCED_CHAT(0x4),

		/**
		 * Interacting entity update.
		 */
		FACE_ENTITY(0x1),

		/**
		 * Face coordinate entity update.
		 */
		FACE_COORDINATE(0x2),

		/**
		 * Hit update.
		 */
		HIT(0x20),

		/**
		 * Hit 2 update/
		 */
		HIT_2(0x200),

		/**
		 * Update flag used to transform npc to another.
		 */
		TRANSFORM(0), // not a real mask...

		/**
		 * Update flag used to signify force movement.
		 */
		FORCE_MOVEMENT(0x400);

		private final int mask;

		UpdateFlag(int mask) {
			this.mask = mask;
		}

		public int getMask() {
			return mask;
		}
	}

	/**
	 * The damage dealt
	 */
	private int damage = -1;

	/**
	 * The hitmask to the attack
	 */
	private int hitMask = -1;

	/**
	 * Checks if an update required.
	 * 
	 * @return <code>true</code> if 1 or more flags are set, <code>false</code>
	 *         if not.
	 */
	public boolean isUpdateRequired() {
		return !flags.isEmpty();
	}

	/**
	 * Flags (sets to true) a flag.
	 * 
	 * @param flag
	 *            The flag to flag.
	 */
	public void flag(UpdateFlag flag) {
		flags.set(flag.ordinal(), true);
	}

	/**
	 * Gets the value of a flag.
	 * 
	 * @param flag
	 *            The flag to get the value of.
	 * @return The flag value.
	 */
	public boolean get(UpdateFlag flag) {
		return flags.get(flag.ordinal());
	}

	/**
	 * Resest all update flags.
	 */
	public void reset() {
		flags.clear();
	}

	/**
	 * Gets the damage dealt to the opposing entity
	 * 
	 * @return
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Sets the damage to the opposing entity
	 * 
	 * @param damage
	 */
	public void setDamage(int damage) {
		this.damage = damage;
	}

	/**
	 * Sets the hitmask to send
	 * 
	 * @param mask
	 */
	public void setHitMask(int mask) {
		this.hitMask = mask;
	}

	public void setHitType(int hitType) {
		this.hitType = hitType;
	}

	public int getHitType() {
		return hitType;
	}

	public void setHitType2(int hitType2) {
		this.hitType2 = hitType2;
	}

	public int getHitType2() {
		return hitType2;
	}

	/**
	 * Gets the hitmask
	 * 
	 * @return
	 */
	public int getHitmask() {
		return hitMask;
	}

	/**
	 * Sends a forced message string
	 * 
	 * @param forceChatMessage
	 */
	public void sendForceMessage(String forceChatMessage) {
		this.forceChatMessage = forceChatMessage;
		flag(UpdateFlag.FORCED_CHAT);
	}

	/**
	 * sets the face update to face an entity
	 * 
	 * @param entityFaceIndex
	 */
	public void faceEntity() {
		flag(UpdateFlag.FACE_ENTITY);
	}

	/**
	 * sets the face update to face a direction
	 * 
	 * @param face
	 */
	public void sendFaceToDirection(Location face) {
		this.faceLocation = face;
		flag(UpdateFlag.FACE_COORDINATE);
	}

	public Location getFaceLocation() {
		return faceLocation;
	}

}
