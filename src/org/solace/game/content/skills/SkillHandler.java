package org.solace.game.content.skills;

import org.solace.game.entity.Graphic;
import org.solace.game.entity.mobile.player.Player;

public class SkillHandler {

	/**
	 * Creates a new player instance
	 */
	private Player player;

	/**
	 * Constructs a skills instance for the designated player
	 * 
	 * @param player
	 */
	public SkillHandler(Player player) {
		this.player = player;
		for (int i = 0; i < getPlayerLevel().length; i++) {
			getPlayerLevel()[i] = 1;
			getPlayerExp()[i] = 0;
		}
		getPlayerLevel()[3] = 10;
		getPlayerExp()[3] = 1155;
	}

	/**
	 * The maximum amount of skills
	 */
	public static final int MAXIMUM_SKILLS = 22;

	private static final int MAXIMUM_EXPERIENCE = 200000000;

	/**
	 * Holds the players levels
	 */
	private int[] playerLevel = new int[MAXIMUM_SKILLS];

	/**
	 * Holds the players experience
	 */
	private int[] playerExp = new int[MAXIMUM_SKILLS];

	private int skillRenewalTimer = 100;

	/**
	 * The current skill constants
	 */
	public static final int ATTACK = 0, DEFENCE = 1, STRENGTH = 2,
			HITPOINTS = 3, RANGED = 4, PRAYER = 5, MAGIC = 6, COOKING = 7,
			WOODCUTTING = 8, FLETCHING = 9, FISHING = 10, FIREMAKING = 11,
			CRAFTING = 12, SMITHING = 13, MINING = 14, HERBLORE = 15,
			AGILITY = 16, THIEVING = 17, SLAYER = 18, FARMING = 19,
			RUNECRAFTING = 20, SUMMONING = 21, HUNTER = 22, CONSTRUCTION = 23,
			DUNGEONEERING = 24;

	/**
	 * Handles all of the frames for the level up interfaces
	 */
	private static final int[][] LEVEL_UP_DATA = { { 6248, 6249, 6247 }, // ATTACK
			{ 6254, 6255, 6253 }, // DEFENCE
			{ 6207, 6208, 6206 }, // STRENGTH
			{ 6217, 6218, 6216 }, // HITPOINTS
			{ 5453, 6114, 4443 }, // RANGED
			{ 6243, 6244, 6242 }, // PRAYER
			{ 6212, 6213, 6211 }, // MAGIC
			{ 6227, 6228, 6226 }, // COOKING
			{ 4273, 4274, 4272 }, // WOODCUTTING
			{ 6232, 6233, 6231 }, // FLETCHING
			{ 6259, 6260, 6258 }, // FISHING
			{ 4283, 4284, 4282 }, // FIREMAKING
			{ 6264, 6265, 6263 }, // CRAFTING
			{ 6222, 6223, 6221 }, // SMITHING
			{ 4417, 4438, 4416 }, // MINING
			{ 6238, 6239, 6237 }, // HERBLORE
			{ 4278, 4279, 4277 }, // AGILITY
			{ 4263, 4264, 4261 }, // THIEVING
			{ 12123, 12124, 12122 }, // SLAYER
			{ -1, -1, -1 }, // FARMING
			{ 4268, 4269, 4267 }, // RUNECRAFTING
	};

	public static final String[] SKILL_NAMES = { "Attack", "Defence",
			"Strength", "Hitpoints", "Ranged", "Prayer", "Magic", "Cooking",
			"Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting",
			"Smithing", "Mining", "Herblore", "Agility", "Thieving", "Slayer",
			"Farming", "Runecrafting", };

	/**
	 * Mainly used to send the strings to the skill frames for newer clients.
	 * Not needed on older clients
	 */
	@SuppressWarnings("unused")
	private static final int[][] REFRESH_DATA = {
		{ ATTACK, 4004, 4005, 4044, 4045, 18792, 18790 },
		{ DEFENCE, 4008, 4009, 4056, 4057, 18817, 18815 },
		{ STRENGTH, 4006, 4007, 4050, 4051, 18798, 18796 },
		{ HITPOINTS, 4016, 4017, 18853, 18854, 18859, 18857 },
		{ RANGED, 4010, 4011, 4062, 4063, 18822, 18820 },
		{ PRAYER, 4012, 4013, 4068, 4069, 18827, 18825 },
		{ MAGIC, 4014, 4015, 18832, 18833, 18838, 18836 },
		{ COOKING, 4034, 4035, 19042, 19043, 19048, 19046 },
		{ WOODCUTTING, 4038, 4039, 19084, 19085, 19090, 19088 },
		{ FLETCHING, 4026, 4027, 18958, 18959, 18964, 18962 },
		{ FISHING, 4032, 4033, 19021, 19022, 19027, 19025 },
		{ FIREMAKING, 4036, 4037, 19063, 19064, 19069, 19067 },
		{ CRAFTING, 4024, 4025, 18937, 18938, 18943, 18941 },
		{ SMITHING, 4030, 4031, 19422, 19423, 19428, 19426 },
		{ MINING, 4028, 4029, 18979, 18980, 18985, 18983 },
		{ HERBLORE, 4020, 4021, 18895, 18896, 18901, 18899 },
		{ AGILITY, 4018, 4019, 18874, 18875, 18880, 18878 },
		{ THIEVING, 4022, 4023, 18916, 18917, 18922, 18920 },
		{ SLAYER, 18809, 18810, 19126, 19127, 19132, 19130 },
		{ FARMING, 18811, 18812, 19275, 19276, 19281, 19279 },
		{ RUNECRAFTING, 18807, 18808, 19105, 19106, 19111, 19109 },
		{ SUMMONING, 19178, 19179, 19232, 19233, 19238, 19236 },
		{ HUNTER, 19176, 19177, 19211, 19212, 19217, 19215 },
		{ CONSTRUCTION, 19174, 19175, 19190, 19191, 19196, 19194 },
		{ DUNGEONEERING, 19180, 19181, 19253, 19254, 19259, 19257 }, };

	public void handleLevelUpData(int skillId) {
		/**
		 * The level up messages to be displayed
		 */
		String displayCongratulations = "@bla@Congratulations, you just advanced a "
				+ SKILL_NAMES[skillId] + " level!";
		String levelUpMessage = "@dbl@Your " + SKILL_NAMES[skillId]
				+ " is now level " + getPlayerLevel()[skillId] + ".";

		/**
		 * Sends the information to the client
		 */
		player.getPacketDispatcher().sendString(LEVEL_UP_DATA[skillId][0],
				displayCongratulations);
		player.getPacketDispatcher().sendString(LEVEL_UP_DATA[skillId][1],
				levelUpMessage);
		player.getPacketDispatcher().sendChatInterface(
				LEVEL_UP_DATA[skillId][2]);
	}

	/**
	 * Refreshes a certain skill id
	 * 
	 * @param skillId
	 */
	public void refreshSkill(int i) {
		try {
			player.getPacketDispatcher().sendSkill(i);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Adds experience to a skill
	 * 
	 * @param skill
	 *            The skill to add experience too
	 * @param experience
	 *            The amount of experience to add
	 */
	public void addSkillExp(int skill, double experience) {
		int oldLevel = getLevelForXP(getPlayerExp()[skill]);
		if ((getPlayerExp()[skill] + experience) >= MAXIMUM_EXPERIENCE) {
			getPlayerExp()[skill] = MAXIMUM_EXPERIENCE;
		} else {
			getPlayerExp()[skill] += experience;
		}
		int newLevel = getLevelForXP(getPlayerExp()[skill]);
		int levelDifference = newLevel - oldLevel;
		if (levelDifference > 0) {
			playerLevel[skill] += levelDifference;
			handleLevelUpData(skill);
			player.setGraphic(Graphic.highGraphic(199, 0));
		}
		refreshSkill(skill);
	}

	public int calculateCombatLevel() {
		final int attack = getLevelForXP(playerExp[ATTACK]);
		final int defence = getLevelForXP(playerExp[DEFENCE]);
		final int strength = getLevelForXP(playerExp[STRENGTH]);
		final int hp = getLevelForXP(playerExp[HITPOINTS]);
		final int prayer = getLevelForXP(playerExp[PRAYER]);
		final int ranged = getLevelForXP(playerExp[RANGED]);
		final int magic = getLevelForXP(playerExp[MAGIC]);
		double level = defence + hp + (prayer / 2);
		double magiclvl = (level + (1.3 * (1.5 * magic))) / 4;
		double rangelvl = (level + (1.3 * (1.5 * ranged))) / 4;
		double meleelvl = (level + (1.3 * (attack + strength))) / 4;
		if (meleelvl >= rangelvl && meleelvl >= magiclvl) {
			return (int) meleelvl;
		} else if (rangelvl >= meleelvl && rangelvl >= magiclvl) {
			return (int) rangelvl;
		} else {
			return (int) magiclvl;
		}
	}

	/**
	 * Gets the amount of experience for the level
	 * 
	 * @param level
	 *            The level to check the amount of experience of
	 * @return
	 */
	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	/**
	 * Gets the players level by checking the experience
	 * 
	 * @param skillExp
	 *            The experience of the skill
	 * @return The level of the skill
	 */
	public int getLevelForXP(int skillExp) {
		int points = 0;
		int output = 0;
		if (skillExp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor((double) lvl + 300.0
					* Math.pow(2.0, (double) lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= skillExp) {
				return lvl;
			}
		}
		return 0;
	}

	public void handleSkillRestoring() {
		if (skillRenewalTimer > 0) {
			skillRenewalTimer--;
		} 
		if (skillRenewalTimer == 50) {
			if (player.getSpecialAmount() < 100) {
				if ((player.getSpecialAmount() + 10) > 100) {
					player.setSpecialAmount(100);
				} else {
					player.setSpecialAmount(player.getSpecialAmount() + 10);
				}
				player.getEquipment().sendWeapon(player);
				player.getEquipment().updateSpecialBar(player);
			}
		} else if (skillRenewalTimer <= 0) {
			for (int i = 0; i < MAXIMUM_SKILLS; i++) {
				if (getPlayerLevel()[i] != getLevelForXP(getPlayerExp()[i])) {
					if (getPlayerLevel()[i] != 5) {
						if (getPlayerLevel()[i] > getLevelForXP(getPlayerExp()[i])) {
							getPlayerLevel()[i] -= 1;
						} else {
							getPlayerLevel()[i] += 1;
						}
						refreshSkill(i);
					}
				}
			}
			if (player.getSpecialAmount() < 100) {
				if ((player.getSpecialAmount() + 10) > 100) {
					player.setSpecialAmount(100);
				} else {
					player.setSpecialAmount(player.getSpecialAmount() + 10);
				}
				player.getEquipment().sendWeapon(player);
				player.getEquipment().updateSpecialBar(player);
			}
			skillRenewalTimer = 100;
		}
	}

	/**
	 * returns the players levels
	 * 
	 * @return
	 */
	public int[] getPlayerLevel() {
		return playerLevel;
	}

	/**
	 * Returns the players experience
	 * 
	 * @return
	 */
	public int[] getPlayerExp() {
		return playerExp;
	}

}
