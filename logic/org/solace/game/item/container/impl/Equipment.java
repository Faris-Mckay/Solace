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
package org.solace.game.item.container.impl;

import org.solace.game.content.combat.Combat;
import org.solace.game.content.skills.SkillHandler;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.game.item.ItemDefinition;
import org.solace.game.item.container.Container;

/**
 * RuneScape equipment container implementation.
 * 
 * @author kLeptO <http://www.rune-server.org/members/klepto/>
 * @author animeking1120
 */
public class Equipment extends Container {

	private ItemDefinition definition = new ItemDefinition();

	public ItemDefinition getDefinition() {
		return definition;
	}

	/**
	 * Allocates a new equipment container for specified player().
	 * 
	 * @param player
	 *            the player instance
	 */
	public Equipment(Player player) {
		super(player);
		resetItems();
	}

	public boolean checkRequirements(int itemId) {
		boolean canWear = true;
		for (int i = 0; i < 21; i++) {
			if (ItemDefinition.get(itemId) != null) {
				if (player().getSkills().getPlayerLevel()[i] < ItemDefinition.get(itemId).getReq()[i]) {
					player().getPacketDispatcher().sendMessage("You need a " + SkillHandler.SKILL_NAMES[i] + " level of " + ItemDefinition.get(itemId).getReq()[i] + " to wear this.");
					canWear = false;
					break;
				}
			}
		}
		return canWear;
	}

	/**
	 * Equips the item from a given slot.
	 * 
	 * @param index
	 *            the item index
	 * 
	 * @param slot
	 *            the item slot
	 */
	public Equipment equip(int index, int slot) {
		if (!checkRequirements(index)) {
			return this;
		}
		Item item = player().getInventory().items()[slot].copy();
		if (item.getIndex() != index) {
			return this;
		}
		int equipmentSlot = ItemDefinition.get(index).equipmentSlot();
		if (ItemDefinition.get(index).twoHanded()
				&& equipmentSlot == WEAPON_SLOT
				&& items()[SHIELD_SLOT].getIndex() != -1) {
			if (player().getInventory().freeSpace() == 0) {
				player().getInventory().noSpace();
				return this;
			} else {
				player().getInventory().add(items()[SHIELD_SLOT].getIndex(),
						items()[SHIELD_SLOT].getAmount(), false);
				set(SHIELD_SLOT, -1, 0);
			}
		}
		Item unequipItem = null;

		if (ItemDefinition.get(index).stackable()
				&& items()[equipmentSlot].getIndex() == index) {
			long totalAmount = (long) item.getAmount()
					+ (long) items()[equipmentSlot].getAmount();
			if (totalAmount > Integer.MAX_VALUE) {
				item.setAmount(Integer.MAX_VALUE);
				unequipItem = items()[equipmentSlot].copy().setAmount(
						(int) (totalAmount - Integer.MAX_VALUE));
			} else {
				item.setAmount((int) totalAmount);
			}
		} else {
			if (equipmentSlot == SHIELD_SLOT
					&& ItemDefinition.get(items()[WEAPON_SLOT].getIndex())
							.twoHanded()) {
				unequipItem = items()[WEAPON_SLOT].copy();
				set(WEAPON_SLOT, -1, 0);
			} else if (items()[equipmentSlot].getIndex() != -1) {
				unequipItem = items()[equipmentSlot].copy();
			}
		}
		set(equipmentSlot, item.getIndex(), item.getAmount());
		if (unequipItem != null) {
			player().getInventory().set(slot, unequipItem);
		} else {
			player().getInventory().set(slot, -1, 0);
		}
		setUsingSpecial(false);
		player().getInventory().refreshItems();
		refreshItems();
		Combat.resetCombat(player());
		return this;
	}

	public static void setWalkingIndex(Player player) {
		// player.getAuthentication().get
	}

	public static final String BONUS_NAME[] = { "Stab", "Slash", "Crush",
			"Magic", "Range", "Stab", "Slash", "Crush", "Magic", "Range",
			"Strength", "Prayer" };

	/**
	 * Sets the equipment bonuses
	 * 
	 * @param player
	 *            The player instance
	 * @return The equipment container
	 */
	public Equipment setBonus(Player player) {
		player.resetBonuses();
		for (Item item : player.getEquipment().items()) {
			for (int i = 0; i < player.getBonuses().length; i++) {
				if (item != null && item.getIndex() < 8000) {
					player.setBonuses(i, player.getBonuses()[i]
							+ ItemDefinition.get(item.getIndex()).bonus(i));
				}
			}
		}
		sendBonusToInterface(player);
		return this;
	}

	/**
	 * Sends to bonus to the equipment interface
	 * 
	 * @param player
	 *            The player instance
	 * @return The equipment container
	 */
	public Equipment sendBonusToInterface(Player player) {
		int offset = 0;
		String toSend = null;
		String operator = null;
		for (int i = 0; i < player.getBonuses().length; i++) {
			if (i == 10) {
				offset = 1;
			}
			operator = player.getBonuses()[i] > 0 ? " +" : " -";
			toSend = BONUS_NAME[i] + operator + " " + player.getBonuses()[i];
			player.getPacketDispatcher()
					.sendString((1675 + i + offset), toSend);
		}
		return this;
	}

	/**
	 * Special attack text and what to highlight or blackout
	 **/

	public void updateSpecialBar(Player player) {

		if (isUsingSpecial()) {
			player.getPacketDispatcher().sendString(player.getSpecialBarId(),
					"@yel@S P E C I A L  A T T A C K");
		} else {
			player.getPacketDispatcher().sendString(player.getSpecialBarId(),
					"@bla@S P E C I A L  A T T A C K");
		}

	}

	public void specialAmount(Player player, int weapon, double specAmount,
			int barId) {
		player.setSpecialBarId(barId);
		player.getPacketDispatcher().moveComponent(specAmount >= 100 ? 150 : 0,
				0, (--barId));
		player.getPacketDispatcher().moveComponent(specAmount >= 90 ? 150 : 0,
				0, (--barId));
		player.getPacketDispatcher().moveComponent(specAmount >= 80 ? 150 : 0,
				0, (--barId));
		player.getPacketDispatcher().moveComponent(specAmount >= 70 ? 150 : 0,
				0, (--barId));
		player.getPacketDispatcher().moveComponent(specAmount >= 60 ? 150 : 0,
				0, (--barId));
		player.getPacketDispatcher().moveComponent(specAmount >= 50 ? 150 : 0,
				0, (--barId));
		player.getPacketDispatcher().moveComponent(specAmount >= 40 ? 150 : 0,
				0, (--barId));
		player.getPacketDispatcher().moveComponent(specAmount >= 30 ? 150 : 0,
				0, (--barId));
		player.getPacketDispatcher().moveComponent(specAmount >= 20 ? 150 : 0,
				0, (--barId));
		player.getPacketDispatcher().moveComponent(specAmount >= 10 ? 150 : 0,
				0, (--barId));
		updateSpecialBar(player);
	}

	private static final int[][] weaponSpecials = { { 861, 7549, 7561 },
			{ 859, 7549, 7561 }, { 11235, 7549, 7561 }, { 4587, 7599, 7611 },
			{ 3204, 8493, 8505 }, { 1377, 7499, 7511 }, { 4153, 7474, 7486 },
			{ 1249, 7674, 7686 }, { 1215, 7574, 7586 }, { 1231, 7574, 7586 },
			{ 5680, 7574, 7586 }, { 5698, 7574, 7586 }, { 1305, 7574, 7586 },
			{ 1305, 7574, 7586 }, { 1434, 7624, 7636 } };

	public Equipment sendSpecialBar(Player player, int id, String name) {
		name = name.toLowerCase();
		if (name.endsWith("whip")) {
			player.getPacketDispatcher().interfaceConfig(0, 12323);
			specialAmount(player, id, player.getSpecialAmount(), 12335);
		}
		for (int i = 0; i < weaponSpecials.length; i++) {
			if (id == weaponSpecials[i][0]) {
				player.getPacketDispatcher().interfaceConfig(0,
						weaponSpecials[i][1]);
				specialAmount(player, id, player.getSpecialAmount(),
						weaponSpecials[i][2]);
			}
		}
		return this;
	}

	public Equipment sendWeapon(Player player) {
		Item weapon = player.getEquipment().get(3);
		int id = -1;
		String name = "Unarmed";
		if (weapon.getIndex() > -1) {
			name = ItemDefinition.get(weapon.getIndex()).name();
			id = weapon.getIndex();
		}
		String name2 = filterWeaponName(name).trim();
		sendWeapon(player, id, name, name2);
		sendSpecialBar(player, id, name);
		return this;
	}

	private void sendWeapon(Player player, int id, String name, String name2) {
		name = name.toLowerCase();
		if (name.equalsIgnoreCase("unarmed")) {
			player.getPacketDispatcher().sendSidebar(0, 5855);
			player.getPacketDispatcher().sendString(5857, name2);
		} else if (name.endsWith("whip")) {
			player.getPacketDispatcher().sendSidebar(0, 12290);
			player.getPacketDispatcher().sendItemOnInterface(12291, 200, id);
			player.getPacketDispatcher().sendString(12293, name2);
		} else if (name.contains("2h")) {
			player.getPacketDispatcher().sendSidebar(0, 4705);
			player.getPacketDispatcher().sendItemOnInterface(4706, 200, id);
			player.getPacketDispatcher().sendString(4708, name2);
		} else if (name.contains("bow")) {
			player.getPacketDispatcher().sendSidebar(0, 1764);
			player.getPacketDispatcher().sendItemOnInterface(1765, 200, id);
			player.getPacketDispatcher().sendString(1767, name2);
		} else if (name.contains("dagger") || name.contains("longsword")) {
			player.getPacketDispatcher().sendSidebar(0, 2276);
			player.getPacketDispatcher().sendItemOnInterface(2277, 200, id);
			player.getPacketDispatcher().sendString(2279, name2);
		} else if (name.contains("maul")) {
			player.getPacketDispatcher().sendSidebar(0, 425);
			player.getPacketDispatcher().sendItemOnInterface(426, 200, id);
			player.getPacketDispatcher().sendString(428, name2);
		} else if (name.contains("staff")) {
			player.getPacketDispatcher().sendSidebar(0, 328);
			player.getPacketDispatcher().sendItemOnInterface(329, 200, id);
			player.getPacketDispatcher().sendString(331, name2);
		} else if (name.contains("knife")) {
			player.getPacketDispatcher().sendSidebar(0, 4446);
			player.getPacketDispatcher().sendItemOnInterface(4447, 200, id);
			player.getPacketDispatcher().sendString(4449, name2);
		} else if (name.contains("pickaxe")) {
			player.getPacketDispatcher().sendSidebar(0, 5570);
			player.getPacketDispatcher().sendItemOnInterface(5571, 200, id);
			player.getPacketDispatcher().sendString(5573, name2);
		} else if (name.contains("axe")) {
			player.getPacketDispatcher().sendSidebar(0, 1698);
			player.getPacketDispatcher().sendItemOnInterface(1699, 200, id);
			player.getPacketDispatcher().sendString(1701, name2);
		} else if (name.contains("halberd")) {
			player.getPacketDispatcher().sendSidebar(0, 8460);
			player.getPacketDispatcher().sendItemOnInterface(8461, 200, id);
			player.getPacketDispatcher().sendString(8463, name2);
		} else if (name.contains("claws")) {
			player.getPacketDispatcher().sendSidebar(0, 7762);
			player.getPacketDispatcher().sendItemOnInterface(7763, 200, id);
			player.getPacketDispatcher().sendString(7765, name2);
		} else if (name.contains("spear")) {
			player.getPacketDispatcher().sendSidebar(0, 4679);
			player.getPacketDispatcher().sendItemOnInterface(4680, 200, id);
			player.getPacketDispatcher().sendString(4682, name2);
		} else if (name.contains("mace")) {
			player.getPacketDispatcher().sendSidebar(0, 3796);
			player.getPacketDispatcher().sendItemOnInterface(3797, 200, id);
			player.getPacketDispatcher().sendString(3799, name2);
		} else {
			player.getPacketDispatcher().sendSidebar(0, 2423);
			player.getPacketDispatcher().sendItemOnInterface(2424, 200, id);
			player.getPacketDispatcher().sendString(2426, name2);
		}
	}

	private String filterWeaponName(String name) {
		final String[] filtered = new String[] { "Iron", "Steel", "Scythe",
				"Black", "Mithril", "Adamant", "Rune", "Granite", "Dragon",
				"Crystal", "Bronze" };
		for (String filter : filtered) {
			name = name.replaceAll(filter, "");
		}
		return name;
	}

	/**
	 * Unequips the item from a given slot.
	 * 
	 * @param index
	 *            the item index
	 * 
	 * @param slot
	 *            the item slot
	 */
	public Equipment unequip(int index, int slot) {
		Item item = items()[slot].copy();
		if (item.getIndex() != index) {
			return this;
		}
		if (ItemDefinition.get(index).stackable()) {
			int otherItemSlot = player().getInventory().itemSlot(index);
			if (player().getInventory().isFull() && otherItemSlot == -1) {
				player().getInventory().noSpace();
				return this;
			}

			if (otherItemSlot != -1) {
				int otherItemAmount = player().getInventory().items()[otherItemSlot]
						.getAmount();
				long totalAmount = (long) item.getAmount()
						+ (long) otherItemAmount;
				if (totalAmount > Integer.MAX_VALUE) {
					set(slot, index, item.getAmount()
							- (int) (Integer.MAX_VALUE - otherItemAmount));
					item.setAmount((int) (Integer.MAX_VALUE - otherItemAmount));
				} else {
					set(slot, -1, 0);
				}
			} else {
				set(slot, -1, 0);
			}
			player().getInventory().add(item.getIndex(), item.getAmount(),
					false);
		} else {
			if (player().getInventory().isFull()) {
				player().getInventory().noSpace();
				return this;
			}
			player().getInventory().add(item.getIndex(), item.getAmount(),
					false);
			set(slot, -1, 0);
		}
		player().getInventory().refreshItems();
		refreshItems();
		return this;
	}

	@Override
	public int capacity() {
		return 14;
	}

	@Override
	public boolean stack() {
		return false;
	}

	@Override
	public Equipment refreshItems() {
		setBonus(player());
		sendWeapon(player());
		player().getPacketDispatcher().sendItemContainer(this,
				EQUIPMENT_INTERFACE);
		player().getUpdateFlags().flag(UpdateFlag.APPEARANCE);
		return this;
	}

	public static final int[] RANGED_WEAPONS = { 837, 839, 841, 843, 845, 847,
			849, 851, 853, 855, 857, 859, 861, 2883, 4212, 4212, 4216, 4217,
			4218, 4219, 4220, 4221, 4222, 4223, 4236, 4734, 4827, 4934, 4835,
			4936, 4937, 4938, 6818, 884, 863, 864, 865, 869, 866, 867, 868,
			806, 807, 808, 809, 810, 811, 4212, 4214, 6522, 825, 826, 827, 828,
			829, 830, 6724, 9185 };

	public static boolean isUsingRanged(Player player) {
		for (int i : RANGED_WEAPONS) {
			Item weapon = player.getEquipment().get(WEAPON_SLOT);
			if (weapon != null && weapon.getIndex() == i) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasCorrectArrows(Player player, int weapon) {
		Item arrows = player.getEquipment().get(AMMUNITION_SLOT);
		if (arrows == null) {
			return false;
		}
		switch (weapon) {
		case 839:
		case 841:
			if (arrows.getIndex() > 881 && arrows.getIndex() < 886) {
				return true;
			}
			break;
		case 843:
		case 845:
			if (arrows.getIndex() > 881 && arrows.getIndex() < 888) {
				return true;
			}
			break;
		case 847:
		case 849:
			if (arrows.getIndex() > 881 && arrows.getIndex() < 890) {
				return true;
			}
			break;
		case 851:
		case 853:
			if (arrows.getIndex() > 881 && arrows.getIndex() < 892) {
				return true;
			}
			break;
		case 855:
		case 857:
			if (arrows.getIndex() > 881 && arrows.getIndex() < 894) {
				return true;
			}
			break;
		case 859:
		case 861:
			if (arrows.getIndex() > 881 && arrows.getIndex() < 894) {
				return true;
			}
			break;
		case 4734:
			if (arrows.getIndex() > 4739 && arrows.getIndex() < 4741) {
				return true;
			}
			break;
		case 9185:
			if (arrows.getIndex() == 9244) {
				return true;
			}
			break;
		}
		return false;
	}

	public static boolean hasRuneAsStaff(Player player, int rune) {
		Item weapon = player.getEquipment().get(3);
		if (weapon == null) {
			return false;
		}
		switch (rune) {
		case 556: // Air rune
			if (weapon.getIndex() == 1381) {
				return true;
			}
			break;
		case 555: // Water rune
			if (weapon.getIndex() == 1383) {
				return true;
			}
			break;
		case 557: // Earth rune
			if (weapon.getIndex() == 1385) {
				return true;
			}
			break;
		case 554: // Fire rune
			if (weapon.getIndex() == 1387) {
				return true;
			}
			break;
		}
		return false;
	}

	@Override
	public Equipment noSpace() {
		return this;
	}

	public static final int HAT_SLOT = 0;
	public static final int CAPE_SLOT = 1;
	public static final int AMULET_SLOT = 2;
	public static final int WEAPON_SLOT = 3;
	public static final int BODY_SLOT = 4;
	public static final int SHIELD_SLOT = 5;
	public static final int LEGS_SLOT = 7;
	public static final int HANDS_SLOT = 9;
	public static final int FEET_SLOT = 10;
	public static final int RING_SLOT = 12;
	public static final int AMMUNITION_SLOT = 13;

	public static final int[] CAPES = { 1007, 1019, 1021, 1023, 1027, 1029,
			1031, 1052, 2412, 2413, 2414, 4304, 4315, 4317, 4319, 4321, 4323,
			4325, 4327, 4329, 4331, 4333, 4335, 4337, 4339, 4341, 4343, 4345,
			4347, 4349, 4351, 4353, 4355, 4357, 4359, 4361, 4363, 4365, 4367,
			4369, 4371, 4373, 4375, 4377, 4379, 4381, 4383, 4385, 4387, 4389,
			4391, 4393, 4395, 4397, 4399, 4401, 4403, 4405, 4407, 4409, 4411,
			4413, 4514, 4516, 6070, 6570 };

	public static final int[] BOOTS = { 88, 89, 626, 628, 630, 632, 634, 1061,
			1837, 1846, 2577, 2579, 2894, 2904, 2914, 2924, 2934, 3061, 3105,
			3107, 3791, 4097, 4107, 4117, 4119, 4121, 4123, 4125, 4127, 4129,
			4131, 4310, 5062, 5063, 5064, 5345, 5557, 6069, 6106, 6143, 6145,
			6147, 6328 };

	public static final int[] GLOVES = { 1059, 1063, 1065, 1580, 2487, 2489,
			2491, 2902, 2912, 2922, 2932, 2942, 3060, 3799, 4095, 4105, 4115,
			4308, 5556, 6068, 6110, 6149, 6151, 6153, 7462 };

	public static final int[] SHIELDS = { 1171, 1173, 1175, 1177, 1179, 1181,
			1183, 1185, 1187, 1189, 1191, 1193, 1195, 1197, 1199, 1201, 1540,
			2589, 2597, 2603, 2611, 2621, 2629, 2659, 2667, 2675, 2890, 3122,
			3488, 3758, 3839, 3840, 3841, 3842, 3843, 3844, 4072, 4156, 4224,
			4225, 4226, 4227, 4228, 4229, 4230, 4231, 4232, 4233, 4234, 4302,
			4507, 4512, 6215, 6217, 6219, 6221, 6223, 6225, 6227, 6229, 6231,
			6233, 6235, 6237, 6239, 6241, 6243, 6245, 6247, 6249, 6251, 6253,
			6255, 6257, 6259, 6261, 6263, 6265, 6267, 6269, 6271, 6273, 6275,
			6277, 6279, 6524, 8850 };

	public static final int[] HATS = { 5525, 5527, 5529, 5531, 5533, 5535,
			5537, 5539, 5541, 5543, 5545, 5547, 5549, 5551, 74, 579, 656, 658,
			660, 662, 664, 740, 1017, 1037, 1038, 1040, 1042, 1044, 1046, 1048,
			1050, 1053, 1055, 1057, 1137, 1139, 1141, 1143, 1145, 1147, 1149,
			1151, 1153, 1155, 1157, 1159, 1161, 1163, 1165, 1506, 1949, 2422,
			2581, 2587, 2595, 2605, 2613, 2619, 2627, 2631, 2651, 2657, 2673,
			2900, 2910, 2920, 2930, 2940, 2978, 2979, 2980, 2981, 2982, 2983,
			2984, 2985, 2986, 2987, 2988, 2989, 2990, 2991, 2992, 2993, 2994,
			2995, 3057, 3385, 3486, 3748, 3749, 3751, 3753, 3755, 3797, 4041,
			4042, 4071, 4089, 4099, 4109, 4164, 4302, 4506, 4511, 4513, 4515,
			4551, 4567, 4708, 4716, 4724, 4745, 4753, 4856, 4857, 4858, 4859,
			4880, 4881, 4882, 4883, 4904, 4905, 4906, 4907, 4952, 4953, 4954,
			4955, 4976, 4977, 4978, 4979, 5013, 5014, 5554, 5574, 6109, 6128,
			6131, 6137, 6182, 6188, 6335, 6337, 6339, 6345, 6355, 6365, 6375,
			10828 };

	public static final int[] AMULETS = { 86, 87, 295, 421, 552, 589, 1478,
			1692, 1694, 1696, 1698, 1700, 1702, 1704, 1706, 1708, 1710, 1712,
			1725, 1727, 1729, 1731, 4021, 4081, 4250, 4677, 6040, 6041, 6208,
			6585 };

	public static final int[] ARROWS = { 78, 598, 877, 878, 879, 880, 881, 882,
			883, 884, 885, 886, 887, 888, 889, 890, 891, 892, 893, 942, 2532,
			2533, 2534, 2535, 2536, 2537, 2538, 2539, 2540, 2541, 2866, 4160,
			4172, 4173, 4174, 4175, 4740, 5616, 5617, 5618, 5619, 5620, 5621,
			5622, 5623, 5624, 5625, 5626, 5627, 6061, 6062 };

	public static final int[] RINGS = { 773, 1635, 1637, 1639, 1641, 1643,
			1645, 2550, 2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566, 2568,
			2570, 2572, 4202, 4657, 6465 };

	public static final int[] BODY = { 3140, 1101, 1103, 1105, 1107, 1109,
			1111, 1113, 1115, 1117, 1119, 1121, 1123, 1125, 1127, 1129, 1131,
			1133, 1135, 2499, 2501, 2503, 2583, 2591, 2599, 2607, 2615, 2623,
			2653, 2669, 3387, 3481, 4712, 4720, 4728, 4749, 4892, 4893, 4894,
			4895, 4916, 4917, 4918, 4919, 4964, 4965, 4966, 4967, 6107, 6133,
			6322, 10551 };

	public static final int[] LEGS = { 538, 542, 548, 1011, 1013, 1015, 1067,
			1069, 1071, 1073, 1075, 1077, 1079, 1081, 1083, 1085, 1087, 1089,
			1091, 1093, 2585, 2593, 2601, 2609, 2617, 2625, 2655, 2663, 2671,
			2497, 3059, 3389, 3472, 3473, 3474, 3475, 3476, 3477, 3478, 3479,
			3480, 3483, 3485, 3795, 4087, 4585, 4712, 4714, 4722, 4730, 4738,
			4751, 4759, 4874, 4875, 4876, 4877, 4898, 4899, 4900, 4901, 4922,
			4923, 4924, 4925, 4946, 4947, 4948, 4949, 4970, 4971, 4972, 4973,
			4994, 4995, 4996, 4997, 5048, 5050, 5052, 5576, 6107, 6130, 6187,
			6390 };

	public static final int[] PLATEBODY = { 3140, 1115, 1117, 1119, 1121, 1123,
			1125, 1127, 2583, 2591, 2599, 2607, 2615, 2623, 2653, 2669, 3481,
			4720, 4728, 4749, 10551 };

	public static final int[] FULL_HELM = { 1153, 1155, 1157, 1159, 1161, 1163,
			1165, 2587, 2595, 2605, 2613, 2619, 2627, 2657, 2673, 3486 };

	public static final int[] FULL_MASK = { 1053, 1055, 1057 };

	public static final int EQUIPMENT_INTERFACE = 1688;

	public static boolean fullDharok(Player player) {
		return player.getEquipment() != null
				&& player.getEquipment().contains(4716)
				&& player.getEquipment().contains(4718)
				&& player.getEquipment().contains(4720)
				&& player.getEquipment().contains(4722);
	}

	private boolean usingSpecial;

	public boolean isUsingSpecial() {
		return usingSpecial;
	}

	public void setUsingSpecial(boolean special) {
		this.usingSpecial = special;
	}

}