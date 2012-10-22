package org.solace.game.entity.mobile.npc;

import java.io.IOException;
import org.solace.util.IndexManager;
import org.solace.game.Game;

/**
 * 
 * @author Faris
 */
public class NPCAdvocate {
    
        public static void loadNPCDefs() throws IOException{
            NPCDefinition.init();
        }
        
        public static void loadNPCDropData(){
            //NPCDropController.init();
        }

	public static void addNpc(NPC npc) {
		Integer npcIndex = IndexManager.getNpcIndex();
		Game.npcRepository.put(npcIndex, npc);
		npc.setIndex(npcIndex);
	}

	public void removeNpc(NPC givenNpc) {
		for (NPC npc : Game.npcRepository.values()) {
			if (npc == givenNpc) {
				givenNpc.kill();
				npc.setIndex(npc.getIndex());
				Game.npcRepository.remove(givenNpc);
			}
		}
	}

}
