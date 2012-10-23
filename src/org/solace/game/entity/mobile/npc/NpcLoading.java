package org.solace.game.entity.mobile.npc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

import org.solace.util.XStreamUtil;

public class NpcLoading {

	public static Logger logger = Logger.getLogger(NpcLoading.class.getName());

	@SuppressWarnings("unchecked")
	public static void loadSpawns() throws FileNotFoundException {
		logger.info("Loading NPC Spawns...");
		List<NPC> list = (List<NPC>) XStreamUtil.getXStream().fromXML(new FileInputStream("./data/xml/npcs/npcspawns.xml"));
		for (NPC spawn : list) {
			try {
				NPCDefinition def = NPCDefinition.getDefinitions()[spawn.getNpcId()];
				if (def == null)
					continue;
				NPC npc = new NPC(def, spawn.getNpcId());
				npc.setLocation(spawn.getLocation());
				npc.targettedLocation(spawn.getLocation());
				npc.setMaximumWalkingDistance(spawn.getMaximumWalkingDistance());
				npc.setMoveStatus(spawn.getMoveStatus());
				NPCAdvocate.addNpc(npc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
