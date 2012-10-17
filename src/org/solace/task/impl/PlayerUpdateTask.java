package org.solace.task.impl;

import java.util.Iterator;
import org.solace.task.Task;
import org.solace.world.game.Game;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class PlayerUpdateTask extends Task {
    
    @Override
    public void execute(){
        Iterator<Player> it = Game.playerRepository.values().iterator();
        while(it.hasNext()){
            Player player = it.next();
            if(player != null){
                player.getUpdater().updateMaster();
                player.update();
            }
        }
    }

}
