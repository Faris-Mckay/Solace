package org.solace.game.content.skills.mining;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.solace.util.XStreamUtil;

/**
 * Loads the definitions for different types of ores
 * 
 * @author Arithim
 * 
 */
public class OreDefinitions {

	private int[] oreId;

	private int replacementId;

	private int itemId;

	private int levelRequired;

	private int experience;

	private int respawnTimer;

	private String oreName;

	public int[] getOreId() {
		return oreId;
	}

	public int getReplacementId() {
		return replacementId;
	}

	public int getItemId() {
		return itemId;
	}

	public int getLevelRequired() {
		return levelRequired;
	}

	public int getExperience() {
		return experience;
	}

	public int getRespawnTimer() {
		return respawnTimer;
	}

	public String getOreName() {
		return oreName;
	}

	public static class OreDefinitionLoader {

		private static Map<Integer, OreDefinitions> ore = null;

		public static OreDefinitions getOreId(int id) {
			return ore.get(id);
		}

		public static void load() throws FileNotFoundException {
			ore = new HashMap<Integer, OreDefinitions>();
			@SuppressWarnings("unchecked")
			List<OreDefinitions> loaded = (ArrayList<OreDefinitions>) XStreamUtil
					.getXStream().fromXML(
							new FileInputStream("./data/xml/mining/ore.xml"));
			for (OreDefinitions allOre : loaded) {
				for (int id : allOre.getOreId()) {
					ore.put(id, allOre);
				}
			}
		}

	}

}
