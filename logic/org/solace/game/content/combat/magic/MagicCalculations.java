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
package org.solace.game.content.combat.magic;

import org.solace.game.content.combat.PrayerHandler.Prayer;
import org.solace.game.entity.mobile.player.Player;

/**
 * Calculates the magic bonuses
 * @author D
 *
 */
public class MagicCalculations {
	
	/**
	 * Returns the players magic attack bonus
	 * @param player
	 * @return
	 */
	public static int calculateMagicAttack(Player player) {
		int attackAccuracy = 0;
		//TODO: void bonus
		int magicBonus = player.getBonuses()[3];
		return (int) (attackAccuracy + (magicBonus * 2));
	}
	
	/**
	 * Returns the players magic defence bonus
	 * @param player
	 * @return
	 */
	public static int calculateMagicDefence(Player player) {
		int magicDefence = ((player.getSkills().getPlayerLevel()[1] / 2) + (player.getSkills().getPlayerLevel()[6] / 2));
		int magicBonus = player.getBonuses()[8];
		if (player.isActivePrayer(Prayer.THICK_SKIN)) {
			magicDefence += player.getSkills().getLevelForXP(player.getSkills().getPlayerExp()[0]) * 0.05;
		} else if (player.isActivePrayer(Prayer.ROCK_SKIN)) {
			magicDefence += player.getSkills().getLevelForXP(player.getSkills().getPlayerExp()[0]) * 0.10;
		} else if (player.isActivePrayer(Prayer.STEEL_SKIN)) {
			magicDefence += player.getSkills().getLevelForXP(player.getSkills().getPlayerExp()[0]) * 0.15;
		}
		return (magicDefence + magicBonus + (magicDefence / 3));
	}
	
	public static int magicMaxHitModifier(Player player) {
		int modifier = 1;
		
		return modifier;
	}

}
