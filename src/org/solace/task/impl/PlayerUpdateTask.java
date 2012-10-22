package org.solace.task.impl;

import org.solace.task.Task;
import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class PlayerUpdateTask extends Task {
    
    public PlayerUpdateTask(){
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
    }
    
    
    @Override
    public void execute(){
        
        /**
         * Loops through and handles all player movement
         */
        for (Player player : Game.playerRepository.values()) {
            if(player != null){
                player.update();
            }
        }
        
        /**
         * Loops through and first updates player, then NPCS 
         */
        for (Player player : Game.playerRepository.values()) {
            if(player != null){
                player.getUpdater().updateMaster();
            	player.getNpcUpdating().updateThisNpc();
            }
        }
        
        /**
         * resets all update flags
         */
        for (Player player : Game.playerRepository.values()) {
            if(player != null){
                player.getUpdater().resetUpdateVars();
            }
        }
        
        /**
         * Finally, register all players waiting to log in
         */
        Game.getSingleton().syncCycleRegistrys();
    }

}
