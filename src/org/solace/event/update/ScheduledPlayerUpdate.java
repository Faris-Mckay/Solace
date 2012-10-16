package org.solace.event.update;

import org.solace.task.impl.ConcurrentTask;
import org.solace.world.World;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class ScheduledPlayerUpdate extends ConcurrentTask {
    
    @Override
    protected void execute() {
        for(Player player : World.worldRepository){
            if(player == null){
                World.getSingleton().deregister(player);
            }
            player.update();
        }
    }

}
