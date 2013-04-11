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
