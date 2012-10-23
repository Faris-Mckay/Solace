package org.solace.game.entity.mobile;

import org.solace.game.content.combat.Calculations;
import org.solace.game.content.combat.DelayedAttack;
import org.solace.game.entity.Entity;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.game.item.WeaponDefinitions;
import org.solace.game.map.Location;
import org.solace.game.map.Region;
import org.solace.util.ProtocolUtils;

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

	public boolean isInCombat() {
		return inCombat;
	}

	public void setInCombat(boolean combat) {
		this.inCombat = combat;
	}

	public int grabAttackSpeed() {
		if (this instanceof Player) {
			Player player = (Player) this;
			Item item = player.getEquipment().get(3);
			for (WeaponDefinitions weapon : WeaponDefinitions.getDefinitions()) {
				if (weapon.getItemId() == item.getIndex()) {
					return weapon.getHitDelay();
				}
			}
		} else if (this instanceof NPC) {
			@SuppressWarnings("unused")
			NPC npc = (NPC) this;
			return 4;
		}
		return 4;
	}

	public int grabAttackBonus() {
		int bonus = 0;
		if (this instanceof Player) {
			Player player = (Player) this;
			bonus = ProtocolUtils.random(Calculations
					.calculateMeleeAttack(player));
		} else if (this instanceof NPC) {
			NPC npc = (NPC) this;
			bonus = npc.getDefinition().getAttackBonus();
		}
		return bonus;
	}

	public int grabDefenceBonus() {
		int bonus = 0;
		if (this instanceof Player) {
			Player player = (Player) this;
			bonus = ProtocolUtils.random(Calculations
					.calculateMeleeDefence(player));
		} else if (this instanceof NPC) {
			NPC npc = (NPC) this;
			bonus = npc.getDefinition().getDefenceMelee();
		}
		return bonus;
	}

	public int grabAttackAnimation() {
		if (this instanceof Player) {
			Player player = (Player) this;
			Item item = player.getEquipment().get(3);
			for (WeaponDefinitions weapon : WeaponDefinitions.getDefinitions()) {
				if (weapon.getItemId() == item.getIndex()) {
					return weapon.getAttackAnimation()[0];
				}
			}
		} else if (this instanceof NPC) {
			NPC n = (NPC) this;
			return n.getDefinition().getAttackAnimation();
		}
		return 422;
	}

	public int getBlockAnimation() {
		int anim = 425;
		if (this instanceof Player) {
			Player player = (Player) this;
			Item item = player.getEquipment().get(3);
			if (item != null) {
				for (WeaponDefinitions weapon : WeaponDefinitions
						.getDefinitions()) {
					if (weapon.getItemId() == item.getIndex()) {
						return weapon.getBlockAnimation();
					}
				}
			}
		} else if (this instanceof NPC) {
			NPC n = (NPC) this;
			anim = n.getDefinition().getDefenceAnimation();
		}
		return anim;
	}

	/**
	 * A method to hit the extended entity
	 * 
	 * @param attack
	 */
	public abstract void hit(DelayedAttack attack);

}
