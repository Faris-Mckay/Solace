package org.solace.game.content.combat.magic;

import org.solace.game.entity.mobile.player.Player;

/**
 * Other magic related materials
 * @author Arithium
 *
 */
public class MagicExtras {

	public static boolean isAutoButton(int button) {
		for (int j = 0; j < autocastIds.length; j += 2) {
			if (autocastIds[j] == button)
				return true;
		}
		return false;
	}

	public static final int[] autocastIds = { 
		1830, 1152, // wind strike
		1831, 1154, // water strike
		1832, 1156, // earth strike
		1833, 1158, // fire strike
		
		1834, 1160, // wind bolt
		1835, 1163, // water bolt
		1836, 1166, // earth bolt
		1837, 1169, // fire bolt
		
		1838, 1172, // wind blast
		1839, 1175, // water blast
		1840, 1177, // earth blast
		1841, 1181, // fire blast
		
		1842, 1183, // wind wave
		1843, 1185, // water wave
		1844, 1188, // earth wave
		1845, 1192, // fire wave
		
	};

	public static void assignAutocast(Player player, int button) {
		for (int j = 0; j < autocastIds.length; j++) {
			if (autocastIds[j] == button) {
				player.getSettings().setAutoCasting(true);
				player.setSpellId(autocastIds[j + 1]);
				player.getPacketDispatcher().sendConfig(108, 1);
				player.getPacketDispatcher().sendSidebar(0, 328);
				break;
			}
		}
	}
	
	public static void resetAutocasting(Player player) {
			player.setSpellId(0);
			player.getSettings().setAutoCasting(false);
			player.getPacketDispatcher().sendSidebar(0, 328);
			player.getPacketDispatcher().sendConfig(108, 0);
			player.getEquipment().sendWeapon(player);
	}
	
	public static void handleActionButtons(Player player, int buttonId) {
		switch (buttonId) {
		case 1093:
		case 1094:
		case 1097:
		case 15486:
		case 353:
			if (player.getSpellId() > 0) {
				resetAutocasting(player);
			} else {
				player.getPacketDispatcher().sendSidebar(0, 1829);
			}
			break;
		case 2004:
			resetAutocasting(player);
			break;
		}
	}

}
