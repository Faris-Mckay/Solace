package org.solace.game.item;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.solace.util.XStreamUtil;

public class ItemRequirementDefinitions {
	
	private int[] requirements;
	
	private int[] itemId;
	
	public int[] getRequirements() {
		return requirements;
	}
	
	public int[] getItemId() {
		return itemId;
	}
	
	public static class ItemRequirementLoader {
		
		private static Map<Integer, ItemRequirementDefinitions> items;
		
		public static ItemRequirementDefinitions getItemId(int id) {
			return items.get(id);
		}
		
		public static Map<Integer, ItemRequirementDefinitions> getItems() {
			return items;
		}
		
		public static void load() throws FileNotFoundException {
			items = new HashMap<Integer, ItemRequirementDefinitions>();
			@SuppressWarnings("unchecked")
			List<ItemRequirementDefinitions> loaded = (ArrayList<ItemRequirementDefinitions>) XStreamUtil
					.getXStream().fromXML(
							new FileInputStream("./data/xml/items/requirements.xml"));
			for (ItemRequirementDefinitions weapon : loaded) {
				for (int id : weapon.getItemId()) {
					items.put(id, weapon);
				}
			}
		}
		
	}

}
