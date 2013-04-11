package org.solace.game.content.skills.mining;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import org.solace.util.XStreamUtil;

/**
 * Loads the pickaxe definitions
 * 
 * @author Arithium
 * 
 */
public class PickaxeDefinitions {

	private int itemId;

	private int levelRequired;

	private int miningBonus;

	private int miningAnimation;

	public int getItemId() {
		return itemId;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getMiningBonus() {
		return miningBonus;
	}

	public int getMiningAnimation() {
		return miningAnimation;
	}

	public static class PickaxeDefinitionLoader {

		private static Map<Integer, PickaxeDefinitions> spells;

		public static PickaxeDefinitions getPickaxe(int id) {
			return spells.get(id);
		}

		@SuppressWarnings("unchecked")
		public static void loadDefinitions() throws FileNotFoundException {
			XStreamUtil.getInstance();
			spells = (Map<Integer, PickaxeDefinitions>) XStreamUtil
					.getXStream()
					.fromXML(
							new FileInputStream("./data/xml/mining/pickaxe.xml"));
		}
	}

}
