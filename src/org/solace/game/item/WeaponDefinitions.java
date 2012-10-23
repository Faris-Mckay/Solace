package org.solace.game.item;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import org.solace.Server;
import org.solace.util.XStreamUtil;

/**
 * 
 * @author Arithrium
 */
public class WeaponDefinitions {

	private int itemId;

	private int runIndex;

	private int walkIndex;

	private int standIndex;

	private int[] attackAnimation;

	private int blockAnimation;
	
	private int hitDelay;

	public int getItemId() {
		return itemId;
	}

	public int getRunIndex() {
		return runIndex;
	}

	public int getWalkIndex() {
		return walkIndex;
	}

	public int getStandIndex() {
		return standIndex;
	}

	public int[] getAttackAnimation() {
		return attackAnimation;
	}

	public int getBlockAnimation() {
		return blockAnimation;
	}
	
	public int getHitDelay() {
		return hitDelay;
	}

	private static WeaponDefinitions[] def = null;
	
	public static WeaponDefinitions[] getDefinitions() {
		return def;
	}

	public static void load() throws FileNotFoundException {
		Server.logger.info("Loading weapon definitions...");
		@SuppressWarnings("unchecked")
		List<WeaponDefinitions> XMLlist = (List<WeaponDefinitions>) XStreamUtil.getXStream().fromXML(new FileInputStream("./data/xml/items/weapon_defs.xml"));
		def = new WeaponDefinitions[XMLlist.size()];
		for (int i = 0; i < def.length; i++) {
			def[i] = XMLlist.get(i);
		}
	}

}
