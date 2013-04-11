package org.solace.game.content.combat.ranged;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import org.solace.util.XStreamUtil;

/**
 * 
 * @author Anthony (Ultimate)
 * 
 */
public class RangedDefinitions {

	private int drawBack;
	private int projectile;

	public int getDrawBack() {
		return drawBack;
	}

	public int getProjectile() {
		return projectile;
	}

	public static class RangedWeaponLoader {

		private static Map<Integer, RangedDefinitions> rangedWeapons;

		public static RangedDefinitions getRangedWeapon(int id) {
			return rangedWeapons.get(id);
		}

		@SuppressWarnings("unchecked")
		public static void loadRangedWeapons() throws FileNotFoundException {
			XStreamUtil.getInstance();
			rangedWeapons = (Map<Integer, RangedDefinitions>) XStreamUtil.getXStream().fromXML(new FileInputStream("./data/xml/items/ranged_weapons.xml"));
		}

	}

}
