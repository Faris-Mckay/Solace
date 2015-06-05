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