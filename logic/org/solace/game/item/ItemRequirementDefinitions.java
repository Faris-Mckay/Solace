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
