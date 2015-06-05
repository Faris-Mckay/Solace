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
