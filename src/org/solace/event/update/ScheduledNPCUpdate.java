package org.solace.event.update;

import org.solace.task.impl.ConcurrentTask;
import org.solace.world.game.entity.mobile.npc.NPC;
import org.solace.world.game.entity.mobile.npc.NPCAdvocate;

/**
 *
 * @author Faris
 */
public class ScheduledNPCUpdate extends ConcurrentTask {
    
    @Override
    protected void execute() {
        for(NPC npc : NPCAdvocate.npcRepository){
            npc.update();
        }
    }

}
