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
package org.solace.game.content.combat.ranged;

import org.solace.game.content.combat.PrayerHandler.Prayer;
import org.solace.game.content.skills.Skill;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.game.item.container.impl.Equipment;

/**
 * Calculates the ranged bonuses
 * @author Arithium
 *
 */
public class RangedCalculations {

	public static int calculateRangedMaxHit(Player player, boolean special) {
		double specialMultiplier = 1;
		double otherBonusMultiplier = 1;
		double prayerMultiplier = 1;
		int rangeLvl = player.getSkills().getPlayerLevel()[Skill.RANGED];
		Item weapon = player.getEquipment().get(Equipment.WEAPON_SLOT);
		Item arrows = player.getEquipment().get(Equipment.AMMUNITION_SLOT);
		double maxHit = (1.05 + (rangeLvl * 0.00125)) + (rangeLvl * 0.11);

		// TODO: Range prayers, void

		if (weapon != null && weapon.getIndex() == 800
				|| weapon.getIndex() == 806 || weapon.getIndex() == 825
				|| weapon.getIndex() == 864) {
			maxHit *= 0.7;
		} else if (weapon != null && weapon.getIndex() == 801
				|| weapon.getIndex() == 807 || weapon.getIndex() == 820
				|| weapon.getIndex() == 863) {
			maxHit *= 0.72;
		} else if (weapon != null && weapon.getIndex() == 802
				|| weapon.getIndex() == 808 || weapon.getIndex() == 827
				|| weapon.getIndex() == 865) {
			maxHit *= 0.79;
		} else if (weapon != null && weapon.getIndex() == 803
				|| weapon.getIndex() == 809 || weapon.getIndex() == 828
				|| weapon.getIndex() == 866) {
			maxHit *= 0.84;
		} else if (weapon != null && weapon.getIndex() == 804
				|| weapon.getIndex() == 810 || weapon.getIndex() == 829
				|| weapon.getIndex() == 867) {
			maxHit *= 0.99;
		} else if (weapon != null && weapon.getIndex() == 805
				|| weapon.getIndex() == 811 || weapon.getIndex() == 830
				|| weapon.getIndex() == 868) {
			maxHit *= 1.24;
		} else if (weapon != null && weapon.getIndex() == 6522) {
			maxHit *= 1.64;
		} else if (weapon != null && weapon.getIndex() == 4212
				|| weapon.getIndex() == 4214) {
			maxHit *= 2.25;
		} else if (arrows != null && arrows.getIndex() == 882
				|| arrows.getIndex() == 883) {
			maxHit *= 1.042;
		} else if (arrows != null && arrows.getIndex() == 884
				|| arrows.getIndex() == 885) {
			maxHit *= 1.044;
		} else if (arrows != null && arrows.getIndex() == 886
				|| arrows.getIndex() == 887) {
			maxHit *= 1.134;
		} else if (arrows != null && arrows.getIndex() == 888
				|| arrows.getIndex() == 889) {
			maxHit *= 1.2;
		} else if (arrows != null && arrows.getIndex() == 890
				|| arrows.getIndex() == 891) {
			maxHit *= 1.35;
		} else if (arrows != null && arrows.getIndex() == 892
				|| arrows.getIndex() == 893) {
			maxHit *= 1.6;
		} else if (arrows != null && arrows.getIndex() == 4740) {
			maxHit *= 1.95;
		} else if (arrows != null && arrows.getIndex() == 9244) {
			maxHit *= 2.6;
		}
		if (special) {
			switch (weapon.getIndex()) {
			case 861:
				specialMultiplier = 1.05;
				break;
			}
		}
		return (int) Math.ceil(maxHit * otherBonusMultiplier * prayerMultiplier
				* specialMultiplier);
	}
	
	public static int calculateRangeDefence(Player player) {
		int rangeDefence = player.getSkills().getPlayerLevel()[0];
		int rangeBonus = player.getBonuses()[9];
		if (player.isActivePrayer(Prayer.THICK_SKIN)) {
			rangeDefence += player.getSkills().getLevelForXP(player.getSkills().getPlayerExp()[0]) * 0.05;
		} else if (player.isActivePrayer(Prayer.ROCK_SKIN)) {
			rangeDefence += player.getSkills().getLevelForXP(player.getSkills().getPlayerExp()[0]) * 0.10;
		} else if (player.isActivePrayer(Prayer.STEEL_SKIN)) {
			rangeDefence += player.getSkills().getLevelForXP(player.getSkills().getPlayerExp()[0]) * 0.15;
		}
		return (int) (rangeDefence + rangeBonus + (rangeBonus / 2));
	}

	public static int calculateRangeAttack(Player player) {
		int rangeLevel = player.getSkills().getPlayerLevel()[4];
		int rangeBonus = player.getBonuses()[4];
		return (int) (rangeLevel + (rangeBonus * 1.95));
	}

}
