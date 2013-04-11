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
import org.solace.game.content.skills.mining.OreDefinitions;
import org.solace.game.content.skills.mining.OreDefinitions.OreDefinitionLoader;
import org.solace.game.content.skills.mining.PickaxeDefinitions;
import org.solace.game.content.skills.mining.PickaxeDefinitions.PickaxeDefinitionLoader;
import org.solace.game.content.skills.prayer.Prayer;
import org.solace.game.content.skills.prayer.PrayerDefinitions;
import org.solace.game.content.skills.thieving.Thieving;
import org.solace.game.content.skills.thieving.ThievingData;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.npc.NPCAdvocate;
import org.solace.game.entity.mobile.npc.NPCDefinition;
import org.solace.game.entity.mobile.npc.NpcLoading;
import org.solace.game.item.ItemDefinition;
import org.solace.game.item.ItemRequirementDefinitions;
import org.solace.game.item.ItemRequirementDefinitions.ItemRequirementLoader;
import org.solace.game.item.WeaponDefinitions;
import org.solace.game.item.WeaponDefinitions.WeaponLoader;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.Sun14ReflectionProvider;

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
		xStream.alias("thieving", ThievingData.class);
		xStream.alias("dialog", DialogueDefinition.class);
		xStream.alias("weaponData", WeaponDefinitions.class);
		xStream.alias("rangedData", RangedDefinitions.class);
		xStream.alias("magicData", MagicDefinitions.class);
		xStream.alias("prayer", PrayerDefinitions.class);
		xStream.alias("ore", OreDefinitions.class);
		xStream.alias("pickaxe", PickaxeDefinitions.class);
		xStream.alias("item", ItemRequirementDefinitions.class);
	}
	
	public static void loadAllXmlData() throws IOException {
		Thieving.load();
		Prayer.load();
		OreDefinitionLoader.load();
		PickaxeDefinitionLoader.loadDefinitions();
		ItemDefinition.loadDefinitions();
		ItemRequirementLoader.load();
		WeaponLoader.loadWeapons();
		RangedWeaponLoader.loadRangedWeapons();
		MagicSpellLoader.loadMagicSpells();
		NPCAdvocate.loadNPCDefs();
		NpcLoading.loadSpawns();
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
