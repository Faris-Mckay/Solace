package org.solace.game.content.combat.impl;

/**
 * Represents a single hit
 * 
 * @author Arithium
 * 
 */
public class Hit {

	private final int damage;

	private final int delay;

	private final int hitType;
	
	private final int hitmask;

	public Hit(int damage, int hitType, int hitmask, int delay) {
		this.damage = damage;
		this.delay = delay;
		this.hitType = hitType;
		this.hitmask = hitmask;
	}

	public int getDamage() {
		return damage;
	}

	public int getHitType() {
		return hitType;
	}

	public int getDelay() {
		return delay;
	}
	
	public int getHitmask() {
		return hitmask;
	}

}
