package org.solace.game.content.combat;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.solace.util.XStreamUtil;

public class WeaponSpecialDefinition {
	
	private int itemId;
	
	private int attackAnimation;
	
	private int attackGfx;
	
	public int getItemId() {
		return itemId;
	}
	
	public int getAttackAnimation() {
		return attackAnimation;
	}
	
	public int getAttackGfx() {
		return attackGfx;
	}
	
	private static Map<Integer, WeaponSpecialDefinition> weapons;
	
	public static Map<Integer, WeaponSpecialDefinition> getWeapons() {
		return weapons;
	}

	public static void load() throws FileNotFoundException {
		weapons = new HashMap<Integer, WeaponSpecialDefinition>();
		@SuppressWarnings("unchecked")
		List<WeaponSpecialDefinition> XMLlist = (List<WeaponSpecialDefinition>) XStreamUtil
				.getXStream()
				.fromXML(
						new FileInputStream("./data/xml/items/weapon_defs.xml"));
		for (WeaponSpecialDefinition weapon : XMLlist) {
			weapons.put(weapon.getItemId(), weapon);
		}
		System.out.println("Loaded " + XMLlist.size()
				+ " weapon definitions definitions");
	}

}
