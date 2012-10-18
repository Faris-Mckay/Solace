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
        /**
         * Loops through and handles all player movement
         */
        for (Player player : Game.playerRepository.values()) {
            if(player != null){
                player.getMobilityManager().processMovement();
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
         * Finally, resets all update flags
         */
        for (Player player : Game.playerRepository.values()) {
            if(player != null){
                player.getUpdater().resetUpdateVars();
            }
        }
    }

}
