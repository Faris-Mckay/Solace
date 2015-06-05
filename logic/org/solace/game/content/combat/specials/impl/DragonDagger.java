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
package org.solace.game.content.combat.specials.impl;

import org.solace.game.content.combat.Combat;
import org.solace.game.content.combat.PrayerHandler.Prayer;
import org.solace.game.content.combat.impl.DelayedHit;
import org.solace.game.content.combat.impl.Hit;
import org.solace.game.content.combat.melee.MeleeCalculations;
import org.solace.game.content.combat.specials.SpecialAttackManager.SpecialAttackHandler;
import org.solace.game.entity.Animation;
import org.solace.game.entity.Graphic;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.player.Player;
import org.solace.util.ProtocolUtils;

/**
 * Represents the dds special attack
 * @author Arithium
 *
 */
public class DragonDagger implements SpecialAttackHandler {

	public static final int WEAPON_ID = 5698;

	@Override
	public void handle(Player attacker, int weaponId) {
		/*
		 * Create an instance of the victim
		 */
		final Mobile victim = (Mobile) attacker.getInteractingEntity();
		/*
		 * Check if the player has the right amount of special
		 */
		if (attacker.getSpecialAmount() >= 25) {
			/*
			 * Now we define the damage calculations
			 */
			int damage = ProtocolUtils.random(MeleeCalculations.calculateMeleeMaxHit(attacker, true));
			int damage2 = ProtocolUtils.random(MeleeCalculations.calculateMeleeMaxHit(attacker, true));
			/*
			 * We check if the victim is a player, and if the player has protect from melee active
			 */
			if (victim instanceof Player) {
				/*
				 * Create a player instance of the victim
				 */
				Player otherPlayer = (Player) victim;
				/*
				 * Check if the prayer is active, if so reduce the damage
				 */
				if (otherPlayer.isActivePrayer(Prayer.PROTECT_FROM_MELEE)) {
					damage2 = (int) (damage * 0.60);
					damage = (int) (damage * 0.60);
				}
			}
			/*
			 * We do the first damage since the damage calculations should be seperate
			 */
			if (ProtocolUtils.random(attacker.grabAttackBonus()) < ProtocolUtils.random(victim.grabDefenceBonus())) {
				damage = 0;
			}
			/*
			 * Then we do the second one since it's a different attack
			 */
			if (ProtocolUtils.random(attacker.grabAttackBonus()) < ProtocolUtils.random(victim.grabDefenceBonus())) {
				damage2 = 0;
			}
			
			/*
			 * Now we send the attack animation
			 */
			attacker.setAnimation(Animation.create(1062, 0));
			/*
			 * Now send the gfx
			 */
			attacker.setGraphic(Graphic.highGraphic(252, 0));
			
			/*
			 * Now send both attacks with a hitmask of 1 first, then 2
			 */
			DelayedHit.submit(attacker, victim, new Hit(damage, damage > 0 ? 1 : 0, 1, 1));
			DelayedHit.submit(attacker, victim, new Hit(damage2, damage2 > 0 ? 1 : 0, 2, 1));
			
			/*
			 * After sending the hits we reduce the special amount
			 */
			attacker.setSpecialAmount(attacker.getSpecialAmount() - 25);
			
			/*
			 * Next add experience
			 */
			Combat.addExperience(attacker, damage);
		} else {
			attacker.getPacketDispatcher().sendMessage(
					"You don't have enough special.");
		}
		/*
		 * Updates the special bar
		 */
		attacker.getEquipment().setUsingSpecial(false);
		attacker.getEquipment().sendWeapon(attacker);
		attacker.getEquipment().updateSpecialBar(attacker);

	}
}
