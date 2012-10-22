package org.solace.game.content.skills;

import org.solace.game.entity.Graphic;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
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
	}

	/**
	 * The maximum amount of skills
	 */
	public static final int MAXIMUM_SKILLS = 21;

	/**
	 * Holds the players levels
	 */
	private int[] playerLevel = new int[MAXIMUM_SKILLS];

	/**
	 * Holds the players experience
	 */
	private int[] playerExp = new int[MAXIMUM_SKILLS];

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
	private static final int[][] LEVEL_UP_DATA ={ 
		{ 6248, 6249, 6247 }, // ATTACK
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
	
	private static final String[] SKILL_NAMES = { 
		"Attack", "Defence", "Strength", "Hitpoints",
		"Ranged", "Prayer", "Magic", "Cooking", "Woodcutting",
		"Fletching", "Fishing", "Firemaking", "Crafting", "Smithing",
		"Mining", "Herblore", "Agility", "Thieving", "Slayer",
		"Farming", "Runecrafting", 
	};

	/**
	 * Mainly used to send the strings to the skill frames for newer clients.
	 * Not needed on older clients
	 */
	@SuppressWarnings("unused")
	private static final int[][] REFRESH_DATA = { {} };
	
	public void handleLevelUpData(int skillId) {
		/**
		 * The level up messages to be displayed
		 */
		String displayCongratulations = "@bla@Congratulations, you just advanced a " + SKILL_NAMES[skillId] + " level!";
		String levelUpMessage = "@dbl@Your " + SKILL_NAMES[skillId] + " is now level " + getPlayerLevel()[skillId] + ".";
		
		/**
		 * Sends the information to the client
		 */
		player.getPacketDispatcher().sendString(LEVEL_UP_DATA[skillId][0], displayCongratulations);
		player.getPacketDispatcher().sendString(LEVEL_UP_DATA[skillId][1], levelUpMessage);
		player.getPacketDispatcher().sendChatInterface(LEVEL_UP_DATA[skillId][2]);
	}
	
	/**
	 * Refreshes a certain skill id
	 * @param skillId
	 */
	public void refreshSkill(int skillId) {
		player.getPacketDispatcher().sendSkill(skillId);
	}

	/**
	 * Adds experience to a skill
	 * 
	 * @param skill
	 *            The skill to add experience too
	 * @param amount
	 *            The amount of experience to add
	 */
	public void addSkillExp(int skill, int amount) {
		if ((getPlayerExp()[skill] + amount) >= 2000000) {
			getPlayerExp()[skill] = 2000000;
		} else {
			getPlayerExp()[skill] += amount;
		}
		int nextLevel = getLevelForXP(getPlayerExp()[skill] + 1);
		if (getPlayerExp()[skill] == nextLevel) {
			// handle level up method
		}
		player.setGraphic(Graphic.highGraphic(199, 0));
		player.getUpdateFlags().flag(UpdateFlag.GRAPHICS);
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
