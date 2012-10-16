package org.solace.task.impl;

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
        for(Player player : Game.playerRepository){
            if(player != null){
                player.getServant().updateMaster();
                player.update();
            }
        }
    }

}
