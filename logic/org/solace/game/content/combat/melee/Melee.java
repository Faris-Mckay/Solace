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
package org.solace.game.content.combat.melee;

import org.solace.Server;
import org.solace.event.events.SpecialAttackEvent;
import org.solace.game.content.combat.Combat;
import org.solace.game.content.combat.PrayerHandler.Prayer;
import org.solace.game.content.combat.impl.DelayedHit;
import org.solace.game.content.combat.impl.Hit;
import org.solace.game.entity.Animation;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.Mobile.WelfareStatus;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.util.ProtocolUtils;

/**
 * Represents the melee combat style
 * @author Arithium
 *
 */
public class Melee extends Combat {

	/**
	 * Creates an instance of the melee class
	 */
	private static final Melee melee = new Melee();

	/**
	 * Returns the singleton of the melee class
	 * 
	 * @return
	 */
	public static final Melee getSingleton() {
		return melee;
	}

	// TODO: Damage mapping to get the attacker who dealt the most damage

	@Override
	public void handle(Mobile attacker) {
		/*
		 * First we create an instance of the victim
		 */
		Mobile victim = (Mobile) attacker.getInteractingEntity();
		/*
		 * Check if all of the requirements are met, if not don't attack
		 */
		if (!checkRequirements(attacker, victim))
			return;
		/*
		 * First we need to face the entity
		 */
		attacker.getUpdateFlags().faceEntity(victim.getIndex() + ((victim instanceof Player) ? 32768 : 0));
		
		/*
		 * Check if the attackers hit delay is at least 0 to prevent spam
		 * attacking
		 */
		if (attacker.getHitDelay() <= 0) {

			/*
			 * First we need to face the entity
			 */
			attacker.getUpdateFlags().faceEntity(victim.getIndex() + ((victim instanceof Player) ? 32768 : 0));

			// attacker.getUpdateFlags().faceEntity();
			/*
			 * Attack animation then block animation before damage
			 */
			attacker.setAnimation(Animation.create(attacker.grabAttackAnimation()));

			/*
			 * Now victims block animation
			 */
			if (victim.getHitDelay() < 2
					&& (victim.getMobilityManager().walkingDirection() < 1 || victim
							.getMobilityManager().runningDirection() < 1)) {
				victim.setAnimation(Animation.create(victim.getBlockAnimation()));
			}

			/*
			 * Now we send the delayed hit based on the weapons attack speed by
			 * default we'll be using 1 since we don't have weapon definitions
			 * 
			 * First we calculate the damage we will deal to the victim, then
			 * create a DelayedHit instance which sends the delayed damage to
			 * the victim
			 * 
			 * TODO: special attack checking
			 */
			int damage = calculateDamage(attacker, victim);
			
			if (victim instanceof Player) {
				Player otherPlayer = (Player) victim;
				if (otherPlayer.isActivePrayer(Prayer.PROTECT_FROM_MELEE)) {
					damage = (int) (damage * 0.60);
				}
			}

			if (attacker instanceof Player) {
				Player player = (Player) attacker;
				if (!player.getEquipment().isUsingSpecial()) {
					DelayedHit.submit(attacker, victim, new Hit(damage,
							damage > 0 ? 1 : 0, 1, 1));
				} else {
					Server.getEventManager().dispatchEvent(new SpecialAttackEvent(player));
				}
			} else if (attacker instanceof NPC) {
				DelayedHit.submit(attacker, victim, new Hit(damage,
						damage > 0 ? 1 : 0, 1, 1));
			}

			/*
			 * Adds experience to the player
			 */
			if (attacker instanceof Player)
				addExperience(attacker, damage);

			/*
			 * Now we reset the attackers hit delay so they cant spam attack
			 */
			victim.setCombatDelay(System.currentTimeMillis());
			attacker.setHitDelay(attacker.grabAttackSpeed());
		}
	}

	@Override
	public boolean checkRequirements(Mobile attacker, Mobile victim) {
		/*
		 * Check if the victim is null or dead, or if the attacker is dead
		 */
		if (victim == null || attacker.getStatus() == WelfareStatus.DEAD
				|| victim.getStatus() == WelfareStatus.DEAD) {
			resetCombat(attacker);
			return false;
		}

		if (!attacker.isInCombat()) {
			// no need to attack as were not in combat, prevents attacking while
			// following
			return false;
		}
		/*
		 * Check if the attacker is within the defined distance requirements
		 * before attacking
		 */
		if (!attacker.getLocation().withinDistance(victim.getLocation(),
				distanceRequired(attacker))) {
			return false;
		}
		return true;
	}

	@Override
	public int distanceRequired(Mobile attacker) {
		return 2;
	}

	@Override
	public int calculateDamage(Mobile attacker, Mobile victim) {
		/*
		 * Calculates the damage a player can deal to the victim
		 */
		if (attacker instanceof Player) {
			/*
			 * Creates a player instance of the attacker
			 */
			Player player = (Player) attacker;

			/*
			 * Checks if the victim is an npc so we can grab the calculations
			 */
			if (victim instanceof NPC) {
				/*
				 * Creates an npc instance of the victim so we don't have to
				 * cast NPC
				 */
				NPC npc = (NPC) victim;
				/*
				 * Checks if the players attack bonus is greater then the npcs
				 * defence bonus
				 */
				int randomAttack = ProtocolUtils.random(player
						.grabAttackBonus() + ProtocolUtils.random(10));
				int randomDefence = ProtocolUtils.random(npc.grabDefenceBonus()
						+ ProtocolUtils.random(5));
				if (randomAttack > randomDefence) {
					return ProtocolUtils.random(MeleeCalculations
							.calculateMeleeMaxHit(player, false));
				}
			} else if (victim instanceof Player) {
				/*
				 * Creates a player instance of the victim so we don't have to
				 * cast player
				 */
				Player otherPlayer = (Player) victim;

				if (((Math.random() * player.grabAttackBonus()) + Math.random() * 5) > ((Math
						.random() * otherPlayer.grabDefenceBonus()) + Math
						.random() * 5)) {
					return ProtocolUtils.random(MeleeCalculations
							.calculateMeleeMaxHit(player, false));
				}
			}
		}
		/*
		 * If none of the requirements are met, set the damage to 0
		 */
		return 0;
	}

}
