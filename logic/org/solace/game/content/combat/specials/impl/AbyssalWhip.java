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
 * Represents the abyssal whip special attack
 * @author Arithium
 *
 */
public class AbyssalWhip implements SpecialAttackHandler {
	
	public static int ABYSSAL_WHIP = 4151;

	@Override
	public void handle(Player attacker, int weaponId) {
		Mobile victim = (Mobile) attacker.getInteractingEntity();
		if (attacker.getSpecialAmount() >= 50) {
			
			int damage = ProtocolUtils.random(MeleeCalculations.calculateMeleeMaxHit(attacker, true));
			
			if (ProtocolUtils.random(attacker.grabAttackBonus()) < ProtocolUtils.random(victim.grabDefenceBonus())) {
				damage = 0;
			}
			
			if (victim instanceof Player) {
				Player otherPlayer = (Player) victim;
				if (otherPlayer.isActivePrayer(Prayer.PROTECT_FROM_MELEE)) {
					damage = (int) (damage * 0.60);
				}
			}
			
			attacker.setAnimation(Animation.create(1658));
			
			victim.setGraphic(Graphic.lowGraphic(341, 0));
			
			DelayedHit.submit(attacker, victim, new Hit(damage, damage > 0 ? 1 : 0, 1, 1));
			
			attacker.setSpecialAmount(attacker.getSpecialAmount() - 50);
			
			Combat.addExperience(attacker, damage);
		} else {
			attacker.getPacketDispatcher().sendMessage("You don't have enough special.");
		}
		attacker.getEquipment().setUsingSpecial(false);
		attacker.getEquipment().sendWeapon(attacker);
		attacker.getEquipment().updateSpecialBar(attacker);
	}

}
