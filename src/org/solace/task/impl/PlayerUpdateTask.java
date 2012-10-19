package org.solace.task.impl;

import java.util.Iterator;
import org.solace.task.Task;
import org.solace.world.World;
import org.solace.world.game.Game;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class PlayerUpdateTask extends Task {
    
    @Override
    public void execute(){
        
        long time = System.currentTimeMillis();
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
        World.getSingleton().syncCycleRegistrys();
        
        System.out.println(System.currentTimeMillis()-time+"ms taken for cycle");
    }

}
