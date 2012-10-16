package org.solace.world.game.entity.mobile.npc;

import java.util.LinkedList;
import java.util.List;
import org.solace.world.game.Game;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class NPCAdvocate {
     
     public static void addNpc(NPC npc){
         Game.npcRepository.add(npc);
     }
     
     public void removeNpc(NPC givenNpc){
        for(NPC npc : Game.npcRepository){
            if(npc == givenNpc){
                givenNpc.kill();
                Game.npcRepository.remove(givenNpc);
            }
        }
    }

}
