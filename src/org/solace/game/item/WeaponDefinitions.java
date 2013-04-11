package org.solace.game.item;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.solace.util.XStreamUtil;

/**
 * 
 * @author Anthony (Ultimate)
 * 
 */
public class WeaponDefinitions {

	private int[] id;

	private int[] equipEmotes = new int[3];
	private int[] attackEmotes = new int[4];
	private int blockEmote;

	private int speed;
	private int interfaceId;

	public int[] getId() {
		return id;
	}

	public int getStandEmote() {
		return equipEmotes[0];
	}

	public int getWalkEmote() {
		return equipEmotes[1];
	}

	public int getRunEmote() {
		return equipEmotes[2];
	}

	public int getAttackEmote(int fightStyle) {
		return attackEmotes[fightStyle];
	}

	public int getBlockEmote() {
		return blockEmote;
	}

	public int getAttackSpeed() {
		return speed;
	}

	public int getInterfaceId() {
		return interfaceId;
	}

	public static class WeaponLoader {

		private static Map<Integer, WeaponDefinitions> weapons;

		public static WeaponDefinitions getWeapon(int id) {
			return weapons.get(id);
		}
		
		public static Map<Integer, WeaponDefinitions> getWeapons() {
			return weapons;
		}

		public static void loadWeapons() throws FileNotFoundException {
			weapons = new HashMap<Integer, WeaponDefinitions>();
			@SuppressWarnings("unchecked")
			List<WeaponDefinitions> loaded = (ArrayList<WeaponDefinitions>) XStreamUtil
					.getXStream().fromXML(
							new FileInputStream("./data/xml/items/weapon_defs.xml"));
			for (WeaponDefinitions weapon : loaded) {
				for (int id : weapon.getId()) {
					weapons.put(id, weapon);
				}
			}
		}

	}

}