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
package org.solace.game.entity.mobile;

import java.util.HashMap;

import org.solace.game.content.combat.impl.Hit;
import org.solace.game.content.combat.melee.MeleeCalculations;
import org.solace.game.entity.Entity;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.game.item.WeaponDefinitions;
import org.solace.game.item.WeaponDefinitions.WeaponLoader;
import org.solace.game.map.Location;

/**
 * 
 * @author Faris
 */
public abstract class Mobile extends Entity {

	private WelfareStatus welfareStatus;
	private MovementStatus moveStatus;
	private Location targettedLocation;

	/**
	 * The mobile entities current hit delay
	 */
	private int hitDelay;

	/**
	 * Determines whether the mobile entity is in combat
	 */
	private boolean inCombat;

	/**
	 * The Mobile entities current cached cacheRegion
	 */
	public Location cachedRegion;
	/**
	 * The current entity interacting with this entity
	 */
	private Entity interactingEntity;

	/**
	 * Creates an instance of the mobilityManager
	 */
	private MobilityManager mobilityQueue = new MobilityManager(this);

	public Mobile(Location location) {
		super(location);
	}

	private int index, maximumWalkingDistance, interactingEntityIndex;

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * Parses in a location and sets it to the Entity's location variable
	 * 
	 * @param location
	 */
	public Entity location(Location location) {
		this.location = location;
		return this;
	}

	/**
	 * Parses in a location and sets it to the Entity's location variable
	 * 
	 * @param location
	 */
	public void setLocation(Location location) {
		super.location = location;
	}

	/**
	 * Parses in a cacheRegion, sets the mobile's variable
	 * 
	 * @param currentRegion
	 * @return the player with the updated cacheRegion
	 */
	public Entity cacheRegion(Location currentRegion) {
		this.cachedRegion = currentRegion;
		return this;
	}

	/**
	 * returns cached cacheRegion for player cacheRegion checking
	 * 
	 * @return
	 */
	public Location getCachedRegion() {
		return this.cachedRegion;
	}

	/**
	 * @return the status
	 */
	public WelfareStatus getStatus() {
		return welfareStatus;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(WelfareStatus status) {
		this.welfareStatus = status;
	}

	/**
	 * @return the moveStatus
	 */
	public MovementStatus getMoveStatus() {
		return moveStatus;
	}

	/**
	 * @param moveStatus
	 *            the moveStatus to set
	 */
	public void setMoveStatus(MovementStatus moveStatus) {
		this.moveStatus = moveStatus;
	}

	/**
	 * @return the targettedLocation
	 */
	public Location getTargettedLocation() {
		return targettedLocation;
	}

	/**
	 * @param targettedLocation
	 *            the targettedLocation to set
	 */
	public Mobile targettedLocation(Location targettedLocation) {
		this.targettedLocation = targettedLocation;
		return this;
	}

	/**
	 * Returns the mobility/movement handler for the mobile
	 * 
	 * @return
	 */
	public MobilityManager getMobilityManager() {
		return mobilityQueue;
	}

	/**
	 * Returns current mobile interacted with
	 * 
	 * @return
	 */
	public Entity getInteractingEntity() {
		return interactingEntity;
	}

	/**
	 * Parses in a mobile and sets as the current mobile interaction
	 * 
	 * @param interactingEntity
	 */
	public void setInteractingEntity(Entity interactingEntity) {
		this.interactingEntity = interactingEntity;
	}

	/**
	 * @return the maximumWalkingDistance
	 */
	public int getMaximumWalkingDistance() {
		return maximumWalkingDistance;
	}

	/**
	 * @param maximumWalkingDistance
	 *            the maximumWalkingDistance to set
	 */
	public void setMaximumWalkingDistance(int maximumWalkingDistance) {
		this.maximumWalkingDistance = maximumWalkingDistance;
	}

	/**
	 * @return the interactingEntityIndex
	 */
	public int getInteractingEntityIndex() {
		return interactingEntityIndex;
	}

	/**
	 * @param interactingEntityIndex
	 *            the interactingEntityIndex to set
	 */
	public void setInteractingEntityIndex(int interactingEntityIndex) {
		this.interactingEntityIndex = interactingEntityIndex;
	}

	/**
	 * Returns the entities hit delay
	 * 
	 * @return
	 */
	public int getHitDelay() {
		return hitDelay;
	}

	/**
	 * Sets the mobile entities current hit delay
	 * 
	 * @param delay
	 */
	public void setHitDelay(int delay) {
		this.hitDelay = delay;
	}
	
	private final HashMap<String, Object> attribute = new HashMap<String, Object>();

	/**
	 * Gets the associated attribute
	 * 
	 * @param name
	 * @return
	 */
	public Object getAttribute(String name) {
		return attribute.get(name);
	}

	/**
	 * Adds an attribute
	 * 
	 * @param name
	 * @param value
	 */
	public void addAttribute(String name, Object value) {
		attribute.put(name, value);
	}

	public enum WelfareStatus {
		/**
		 * The Mobile Entity is current alive
		 */
		ALIVE,

		/**
		 * The Mobile Entity is currently dead
		 */
		DEAD,

		/**
		 * The Mobile Entity is currently in the process of dieing
		 */
		DIEING;
	}

	public enum MovementStatus {
		/**
		 * The Mobile Entity is standing still
		 */
		STATIONARY,

		/**
		 * The Mobile Entity is currently moving
		 */
		MOBILE,
	}
	
	public enum AttackStyle {
		ACCURATE,
		AGGRESSIVE,
		CONTROLLED,
		DEFENSIVE,
	}

	/**
	 * The different combat styles
	 * 
	 * @author Arithium
	 * 
	 */
	public enum AttackTypes {
		MELEE, MAGIC, RANGED
	}
	
	/**
	 * Determines the mobile entities current attack style
	 */
	public AttackStyle attackStyle = AttackStyle.ACCURATE;
	
	/**
	 * Returns the mobile entities current attack style
	 * @return
	 */
	public AttackStyle getAttackStyle() {
		return attackStyle;
	}
	
	/**
	 * Sets the attack style
	 * @param style
	 */
	public void setAttackStyle(AttackStyle style) {
		this.attackStyle = style;
	}
	
	/**
	 * Gets the attack style as a variable
	 * @return
	 */
	public byte getAttackStyleValue() {
		switch (getAttackStyle()) {
		case ACCURATE:
			return 0;
		case AGGRESSIVE:
			return 1;
		case CONTROLLED:
			return 2;
		case DEFENSIVE:
			return 3;
		}
		return 0;
	}
	/**
	 * Defines the entities current attack type
	 */
	public AttackTypes attackTypes = AttackTypes.MELEE;

	/**
	 * Returns the entities current attack type
	 * 
	 * @return
	 */
	public AttackTypes getAttackType() {
		return attackTypes;
	}

	/**
	 * Sets the entities current attack type
	 * 
	 * @param attack
	 */
	public void setAttackType(AttackTypes attack) {
		this.attackTypes = attack;
	}

	/**
	 * Returns whether the entity is in combat
	 * @return
	 */
	public boolean isInCombat() {
		return inCombat;
	}

	/**
	 * Sets the entity in or out of combat
	 * @param combat
	 */
	public void setInCombat(boolean combat) {
		this.inCombat = combat;
	}

	/**
	 * Grabs the attack speed of the entity
	 * @return
	 */
	public int grabAttackSpeed() {
		if (this instanceof Player) {
			Player player = (Player) this;
			Item item = player.getEquipment().get(3);
			if (item != null) {
				WeaponDefinitions weapon = WeaponLoader.getWeapon(item.getIndex());
				if (weapon != null) {
					return weapon.getAttackSpeed() / 600;
				}
			}
		} else if (this instanceof NPC) {
			return 4;
		}
		return 4;
	}

	/**
	 * Grabs the entitys attack bonus
	 * @return
	 */
	public int grabAttackBonus() {
		int bonus = 0;
		if (this instanceof Player) {
			Player player = (Player) this;
			bonus = MeleeCalculations.calculateMeleeAttack(player);
		} else if (this instanceof NPC) {
			NPC npc = (NPC) this;
			bonus = npc.getDefinition().getAttackBonus();
		}
		return bonus;
	}

	/**
	 * Gets the entities defence bonus
	 * @return
	 */
	public int grabDefenceBonus() {
		int bonus = 0;
		if (this instanceof Player) {
			Player player = (Player) this;
			bonus = MeleeCalculations.calculateMeleeDefence(player);
		} else if (this instanceof NPC) {
			NPC npc = (NPC) this;
			bonus = npc.getDefinition().getDefenceMelee();
		}
		return bonus;
	}

	/**
	 * Gets the entities attack animation
	 * @return
	 */
	public int grabAttackAnimation() {
		if (this instanceof Player) {
			Player player = (Player) this;
			Item item = player.getEquipment().get(3);
			if (item != null) {
				WeaponDefinitions weapon = WeaponLoader.getWeapon(item.getIndex());
				if (weapon != null) {
					return weapon.getAttackEmote(getAttackStyleValue()); // 0 = fightype
				} else {
					if (getAttackStyleValue() != 1) {
						return 422;
					} else {
						return 423;
					}
				}
			}
		} else if (this instanceof NPC) {
			NPC n = (NPC) this;
			return n.getDefinition().getAttackAnimation();
		}
		return 422;
	}

	/**
	 * Gets the entities block animation
	 * @return
	 */
	public int getBlockAnimation() {
		if (this instanceof Player) {
			Player player = (Player) this;
			Item item = player.getEquipment().get(3);
			if (item != null) {
				WeaponDefinitions weapon = WeaponLoader.getWeapon(item.getIndex());
				if (weapon != null) {
					return weapon.getBlockEmote();
				} 
			}
		} else if (this instanceof NPC) {
			NPC n = (NPC) this;
			return n.getDefinition().getDefenceAnimation();
		}
		return 424;
	}
	
	public int getWildernessLevel() {
		if (!inWild()) {
			return 0;
		} else {
			int modY = getLocation().getY() > 6400 ?  getLocation().getY() - 6400 :  getLocation().getY();
			return (modY - 3520) / 8 + 1;
		}
	}
	
	public boolean inWild() {
		return ( getLocation().inArea( getLocation(), new Location(2942, 3520), new Location(3391, 3965)))
				|| ( getLocation().inArea( getLocation(), new Location(2942, 9919), new Location(3391, 10365)));
	}
	
	/**
	 * A method to hit the extended entity
	 * 
	 * @param attack
	 */
	public abstract void hit(Hit attack);

}
