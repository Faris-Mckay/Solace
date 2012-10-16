package org.solace.task.impl;

import org.solace.task.Task;
import org.solace.world.World;
import org.solace.world.game.Game;
import org.solace.world.game.entity.mobile.Mobile;
import org.solace.world.game.entity.mobile.npc.NPCAdvocate;

/**
 *
 * @author Faris
 */
public class NPCUpdateTask extends Task {
    
    @Override
    public void execute(){
        for(Mobile mobile : Game.npcRepository){
            if(mobile != null){
                mobile.update();
            }
        }
    }

}
