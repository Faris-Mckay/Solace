package org.solace.game.content.combat;

import java.util.HashMap;
import java.util.Map;

import org.solace.game.content.skills.Skill;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.player.Player;

public class PrayerHandler {

	public static enum Prayer {
		THICK_SKIN(5609, 1, 0.5), BURST_OF_STRENGTH(5610, 4, 0.5), CLARITY_OF_THOUGHT(
				5611, 7, 0.5), ROCK_SKIN(5612, 10, 1.0), SUPERHUMAN_STRENGTH(
				5613, 13, 1.0), IMPROVED_REFLEXES(5614, 16, 1.0), RAPID_RESTORE(
				5615, 19, (1 / 6)), RAPID_HEAL(5616, 22, (1 / 3)), PROTECT_ITEM(
				5617, 25, (1 / 3)), STEEL_SKIN(5618, 28, 2.0), ULTIMATE_STRENGTH(
				5619, 31, 2.0), INCREDIBLE_REFLEXES(5620, 34, 2.0), PROTECT_FROM_MAGIC(
				5621, 37, 2.0), PROTECT_FROM_MISSILE(5622, 40, 2.0), PROTECT_FROM_MELEE(
				5623, 43, 2.0), RETRIBUTION(683, 46, 0.5), REDEMPTION(684, 49,
				1.0), SMITE(685, 52, (10 / 3));

		private int buttonId;

		private int levelRequirement;

		private double drainRate;

		private Prayer(int buttonId, int level, double drain) {
			this.buttonId = buttonId;
			this.levelRequirement = level;
			this.drainRate = drain;
		}

		public int getPrayerIndex(Prayer prayer) {
			for (Prayer data : Prayer.values()) {
				if (data == prayer) {
					return data.ordinal();
				}
			}
			return -1;
		}

		public int getButtonId() {
			return buttonId;
		}

		public int getConfigId(Prayer prayer) {
			return 83 + getPrayerIndex(prayer);
		}

		public int getLevelRequirement() {
			return levelRequirement;
		}

		public double getDrainRate() {
			return drainRate;
		}
	}

	public static void togglePrayer(final Player player, final int buttonId) {
		for (Prayer prayer : Prayer.values()) {
			if (buttonId == prayer.getButtonId()) {
				if (player.isActivePrayer(prayer)) {
					deactivatePrayer(player, prayer);
					return;
				} else {
					activatePrayer(player, prayer);
					return;
				}
			}
		}
	}

	private static void activatePrayer(final Player player, final Prayer prayer) {
		if (player.getSkills().getLevelForXP(
				player.getSkills().getPlayerExp()[5]) + 1 < prayer
				.getLevelRequirement()) {
			player.getPacketDispatcher().sendMessage(
					"You need a prayer level of at least "
							+ prayer.getLevelRequirement()
							+ " to use "
							+ prayer.toString().toLowerCase()
									.replaceAll("_", " ") + ".");
			deactivatePrayer(player, prayer);
			return;
		}
		if (player.getSkills().getPlayerLevel()[5] <= 0) {
			player.getPacketDispatcher()
					.sendMessage(
							"You have run out of prayer points; recharge your prayer points at an altar.");
			return;
		}
		switchPrayer(player, prayer);
		if (prayer.equals(Prayer.PROTECT_FROM_MAGIC)
				|| prayer.equals(Prayer.PROTECT_FROM_MISSILE)
				|| prayer.equals(Prayer.PROTECT_FROM_MELEE)
				|| prayer.equals(Prayer.RETRIBUTION)
				|| prayer.equals(Prayer.REDEMPTION)
				|| prayer.equals(Prayer.SMITE)) {
			int headIcon = -1;
			switch (prayer) {
			case PROTECT_FROM_MAGIC:
				headIcon = 2;
				break;
			case PROTECT_FROM_MISSILE:
				headIcon = 1;
				break;
			case PROTECT_FROM_MELEE:
				headIcon = 0;
				break;
			case RETRIBUTION:
				headIcon = 3;
				break;
			case REDEMPTION:
				headIcon = 5;
				break;
			case SMITE:
				headIcon = 4;
				break;
			default:
				break;
			}
			player.setPrayerIcon(headIcon);
			player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		}
		player.setActivePrayer(prayer, true);
		player.getPacketDispatcher().sendConfig(prayer.getConfigId(prayer), 1);
		player.addPrayerDrainRate(prayer.getDrainRate());
	}

	public static void deactivatePrayer(final Player player, final Prayer prayer) {
		player.setActivePrayer(prayer, false);
		if (prayer.equals(Prayer.PROTECT_FROM_MAGIC)
				|| prayer.equals(Prayer.PROTECT_FROM_MISSILE)
				|| prayer.equals(Prayer.PROTECT_FROM_MELEE)
				|| prayer.equals(Prayer.RETRIBUTION)
				|| prayer.equals(Prayer.REDEMPTION)
				|| prayer.equals(Prayer.SMITE)) {
			player.setPrayerIcon(-1);
			player.getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		}
		player.addPrayerDrainRate(-(prayer.getDrainRate()));
		player.getPacketDispatcher().sendConfig(prayer.getConfigId(prayer), 0);
	}

	private static Prayer[] defensePrayers = { Prayer.THICK_SKIN,
			Prayer.ROCK_SKIN, Prayer.STEEL_SKIN };

	private static Prayer[] strengthPrayers = { Prayer.BURST_OF_STRENGTH,
			Prayer.SUPERHUMAN_STRENGTH, Prayer.ULTIMATE_STRENGTH };

	private static Prayer[] attackPrayers = { Prayer.CLARITY_OF_THOUGHT,
			Prayer.IMPROVED_REFLEXES, Prayer.INCREDIBLE_REFLEXES };

	private static Prayer[] restorePrayers = { Prayer.RAPID_RESTORE,
			Prayer.RAPID_HEAL };
	private static Prayer[] overheadPrayers = { Prayer.PROTECT_FROM_MAGIC,
			Prayer.PROTECT_FROM_MISSILE, Prayer.PROTECT_FROM_MELEE,
			Prayer.RETRIBUTION, Prayer.REDEMPTION, Prayer.SMITE };

	private static Map<Prayer, Prayer[]> unstackable = new HashMap<Prayer, Prayer[]>();

	static {
		for (Prayer p : defensePrayers) {
			unstackable.put(p, defensePrayers);
		}
		for (Prayer p : strengthPrayers) {
			unstackable.put(p, strengthPrayers);
		}
		for (Prayer p : attackPrayers) {
			unstackable.put(p, attackPrayers);
		}
		for (Prayer p : restorePrayers) {
			unstackable.put(p, restorePrayers);
		}
		for (Prayer p : overheadPrayers) {
			unstackable.put(p, overheadPrayers);
		}
	}

	public static void handlePrayerDraining(Player player) {
		double toRemove = 0.0;
		toRemove += player.getPrayerDrainRate() / 20;
		if (toRemove > 0) {
			toRemove /= (1 + (0.035 * player.getBonuses()[11]));
		}
		player.setPrayerPoint(player.getPrayerPoint() - toRemove);
		if (player.getPrayerPoint() <= 0) {
			player.setPrayerPoint(1.0 + player.getPrayerPoint());
			if (player.getSkills().getPlayerLevel()[Skill.PRAYER] > 1) {
				player.getSkills().getPlayerLevel()[Skill.PRAYER] -= 1;
			} else {
				player.getPacketDispatcher().sendMessage(
						"You have run out of prayer points.");
				player.getSkills().getPlayerLevel()[Skill.PRAYER] = 0;
				for (Prayer prayer : Prayer.values()) {
					PrayerHandler.deactivatePrayer(player, prayer);
				}
			}
		}
		player.getSkills().refreshSkill(5);
	}
	
	/**
	 * deactivates all of the prayers
	 * @param player
	 */
	public static void resetAllPrayers(final Player player) {
		for (Prayer prayer : Prayer.values()) {
			deactivatePrayer(player, prayer);
		}
	}

	private static void switchPrayer(final Player player, final Prayer prayer) {
		Prayer[] toDeactivate = unstackable.get(prayer);
		for (int i = 0; i < toDeactivate.length; i++) {
			if (player.isActivePrayer(toDeactivate[i])
					&& toDeactivate[i] != prayer) {
				deactivatePrayer(player, toDeactivate[i]);
			}
		}
	}
}