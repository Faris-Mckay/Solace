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
package org.solace.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.solace.game.content.combat.magic.MagicDefinitions;
import org.solace.game.content.combat.magic.MagicDefinitions.MagicSpellLoader;
import org.solace.game.content.combat.ranged.RangedDefinitions;
import org.solace.game.content.combat.ranged.RangedDefinitions.RangedWeaponLoader;
import org.solace.game.content.dialogue.Dialogue;
import org.solace.game.content.dialogue.DialogueDefinition;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.npc.NPCAdvocate;
import org.solace.game.entity.mobile.npc.NPCDefinition;
import org.solace.game.item.ItemDefinition;
import org.solace.game.item.ItemRequirementDefinitions;
import org.solace.game.item.ItemRequirementDefinitions.ItemRequirementLoader;
import org.solace.game.item.WeaponDefinitions;
import org.solace.game.item.WeaponDefinitions.WeaponLoader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;
import org.solace.game.entity.mobile.npc.NPCLoading;

/**
 * 
 * @author Faris
 */
public class XStreamUtil {

	private static XStreamUtil instance = new XStreamUtil();
	private static XStream xStream = new XStream(new Sun14ReflectionProvider());

	public static XStream getXStream() {
		return xStream;
	}

	/**
	 * Singleton getter
	 * 
	 * @return
	 */
	public static XStreamUtil getInstance() {
		return instance;
	}

	// pre-defined attributes
	static {
		xStream.alias("npc", NPC.class);
		xStream.alias("npcDefinition", NPCDefinition.class);
		xStream.alias("dialog", DialogueDefinition.class);
		xStream.alias("weaponData", WeaponDefinitions.class);
		xStream.alias("rangedData", RangedDefinitions.class);
		xStream.alias("magicData", MagicDefinitions.class);
		xStream.alias("item", ItemRequirementDefinitions.class);
	}
	
	public static void loadAllXmlData() throws IOException {
		ItemDefinition.loadDefinitions();
		ItemRequirementLoader.load();
		WeaponLoader.loadWeapons();
		RangedWeaponLoader.loadRangedWeapons();
		MagicSpellLoader.loadMagicSpells();
		NPCAdvocate.loadNPCDefs();
		NPCLoading.loadSpawns();
		Dialogue.load();
	}

	/**
	 * Writes to the targeted XML files
	 * 
	 * @param object
	 * @param file
	 * @throws IOException
	 */
	public static void writeXML(Object object, File file) throws IOException {
		FileOutputStream out = new FileOutputStream(file);
		try {
			xStream.toXML(object, out);
			out.flush();
		} finally {
			out.close();
		}
	}

}
