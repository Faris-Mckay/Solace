package org.solace.world.game.entity.mobile.npc;

import java.util.LinkedList;
import java.util.List;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class NPCAdvocate {
    
     public static final List<NPC> npcRepository = new LinkedList<NPC>();
     
     public static void addNpc(NPC npc){
         npcRepository.add(npc);
     }
     
     public void removeNpc(NPC givenNpc){
        for(NPC npc : npcRepository){
            if(npc == givenNpc){
                givenNpc.kill();
                npcRepository.remove(givenNpc);
            }
        }
    }

}
