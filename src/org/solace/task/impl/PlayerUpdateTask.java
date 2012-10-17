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
        for (Player player : Game.playerRepository.values()) {
            if(player != null){
                player.update();
            }
        }
        
        for (Player player : Game.playerRepository.values()) {
            if(player != null){
                player.getUpdater().updateMaster();
            }
        }
        
        for (Player player : Game.playerRepository.values()) {
            if(player != null){
                player.getUpdater().resetUpdateVars();
            }
        }
    }

}
