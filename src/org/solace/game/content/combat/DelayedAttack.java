package org.solace.game.content.combat;

/**
 * Represents a delayed attack
 * 
 * @author Arithium
 * 
 */
public class DelayedAttack {

	private final int damage;

	private final int delay;

	private final int hitmask;

	public DelayedAttack(int damage, int hitmask, int delay) {
		this.damage = damage;
		this.delay = delay;
		this.hitmask = hitmask;
	}

	public int getDamage() {
		return damage;
	}

	public int getHitmask() {
		return hitmask;
	}

	public int getDelay() {
		return delay;
	}

}
