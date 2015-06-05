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

import org.solace.game.content.combat.PrayerHandler.Prayer;
import org.solace.game.content.skills.Skill;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.container.impl.Equipment;

/**
 * Calculates the melee bonuses
 *
 * @author Arithium
 * 
 */
public class MeleeCalculations {

	public static int calculateMeleeMaxHit(Player player, boolean special) {
		int maxHit = 0;
		double specialMultiplier = 1;
		double prayerMultiplier = 1;
		double otherBonusMultiplier = 1;
		int strengthLevel = player.getSkills().getPlayerLevel()[Skill.STRENGTH];
		int combatStyleBonus = weaponBonus(player);

		if (player.isActivePrayer(Prayer.BURST_OF_STRENGTH)) {
			prayerMultiplier = 1.05;
		} else if (player.isActivePrayer(Prayer.SUPERHUMAN_STRENGTH)) {
			prayerMultiplier = 1.1;
		} else if (player.isActivePrayer(Prayer.ULTIMATE_STRENGTH)) {
			prayerMultiplier = 1.15;
		}

		int effectiveStrengthDamage = (int) ((strengthLevel * prayerMultiplier * otherBonusMultiplier) + combatStyleBonus);
		double base = (13 + effectiveStrengthDamage + (strengthLevel / 8) + ((effectiveStrengthDamage * strengthLevel) / 64)) / 10;

		if (special) {
			switch (player.getEquipment().get(3).getIndex()) {
			case 3101:
			case 3204:
			case 1215:
			case 1231:
			case 5680:
			case 5698:
				specialMultiplier = 1.1;
				break;
			case 1305:
				specialMultiplier = 1.15;
				break;
			case 1434:
				specialMultiplier = 1.45;
				break;
			}
		}

		maxHit = (int) (base * specialMultiplier);

		if (Equipment.fullDharok(player)) {
			base += (player.getSkills().getLevelForXP(player.getSkills().getPlayerExp()[3]) - player.getSkills().getPlayerLevel()[3]) / 2;
		}
		return maxHit;
	}

	public static final int weaponBonus(Player player) {
		switch (player.getAttackStyle()) {
		case AGGRESSIVE:
			return 3;
		case CONTROLLED:
			return 1;
		default:
			break;
		}
		return 0;
	}

	public static int bestMeleeDef(Player c) {
		if (c.getBonuses()[5] > c.getBonuses()[6]
				&& c.getBonuses()[5] > c.getBonuses()[7]) {
			return 5;
		}
		if (c.getBonuses()[6] > c.getBonuses()[5]
				&& c.getBonuses()[6] > c.getBonuses()[7]) {
			return 6;
		}
		return c.getBonuses()[7] <= c.getBonuses()[5]
				|| c.getBonuses()[7] <= c.getBonuses()[6] ? 5 : 7;
	}

	public static int calculateMeleeDefence(Player c) {
		int defenceLevel = c.getSkills().getPlayerLevel()[1];
		int i = c.getBonuses()[bestMeleeDef(c)];
		if (c.isActivePrayer(Prayer.THICK_SKIN)) {
			defenceLevel += c.getSkills().getLevelForXP(
					c.getSkills().getPlayerExp()[Skill.DEFENCE]) * 0.05;
		} else if (c.isActivePrayer(Prayer.ROCK_SKIN)) {
			defenceLevel += c.getSkills().getLevelForXP(
					c.getSkills().getPlayerExp()[Skill.DEFENCE]) * 0.1;
		} else if (c.isActivePrayer(Prayer.STEEL_SKIN)) {
			defenceLevel += c.getSkills().getLevelForXP(
					c.getSkills().getPlayerExp()[Skill.DEFENCE]) * 0.15;
		}
		return (int) (defenceLevel + (defenceLevel * 0.15) + (i + i * 0.05));
	}

	public static int bestMeleeAtk(Player c) {
		if (c.getBonuses()[0] > c.getBonuses()[1]
				&& c.getBonuses()[0] > c.getBonuses()[2]) {
			return 0;
		}
		if (c.getBonuses()[1] > c.getBonuses()[0]
				&& c.getBonuses()[1] > c.getBonuses()[2]) {
			return 1;
		}
		return c.getBonuses()[2] <= c.getBonuses()[1]
				|| c.getBonuses()[2] <= c.getBonuses()[0] ? 0 : 2;
	}

	public static int calculateMeleeAttack(Player c) {
		int attackLevel = c.getSkills().getPlayerLevel()[0];
		if (c.isActivePrayer(Prayer.CLARITY_OF_THOUGHT)) {
			attackLevel += c.getSkills().getLevelForXP(
					c.getSkills().getPlayerExp()[Skill.ATTACK]) * 0.05;
		} else if (c.isActivePrayer(Prayer.IMPROVED_REFLEXES)) {
			attackLevel += c.getSkills().getLevelForXP(
					c.getSkills().getPlayerExp()[Skill.ATTACK]) * 0.1;
		} else if (c.isActivePrayer(Prayer.INCREDIBLE_REFLEXES)) {
			attackLevel += c.getSkills().getLevelForXP(
					c.getSkills().getPlayerExp()[Skill.ATTACK]) * 0.15;
		}
		int i = c.getBonuses()[bestMeleeAtk(c)];
		return (int) (attackLevel + (attackLevel * 0.15) + (i + i * 0.05));
	}

}
