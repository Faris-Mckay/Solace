package org.solace.task.impl;

import org.solace.game.Game;
import org.solace.game.entity.GroundItemHandler;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.task.Task;

/**
 * 
 * @author Faris
 */
public class EntityUpdateTask extends Task {

	@Override
	public void execute() {
            /**
             * Loops through and handles all player movement
             */
            for (Player player : Game.playerRepository.values()) {
                    if (player != null) {
                            player.update();
                    }
            }

            /**
             * Loops through and first updates player, then NPCS
             */
            for (Player player : Game.playerRepository.values()) {
                    if (player != null) {
                            player.getUpdater().updateMaster();
                            player.getNpcUpdating().updateThisNpc();
                    }
            }
            
            /**
             * resets all update flags
             */
            for (Player player : Game.playerRepository.values()) {
                    if (player != null) {
                            player.getUpdater().resetUpdateVars();
                    }
            }
            
            /**
             * Loops through all NPCs and handles all logic to be updated (e.g
             * Combat and animations)
             */
            for (NPC npc : Game.npcRepository.values()) {
                    if (npc != null) {
                            try {
                                    npc.update();
                            } catch (Exception e) {
                                    e.printStackTrace();
                            }
                    }
            }
            
            
            /*
             * Resets the npcs update flags
             */
            for (NPC npc : Game.npcRepository.values()) {
                    if (npc == null)
                            continue;
                    try {
                            npc.getUpdateFlags().reset();
                    } catch (Exception e) {
                            e.printStackTrace();
                    }
            }


            /**
             * Finally, register all players waiting to log in
             */
            Game.getSingleton().syncCycleRegistrys();
	}

}
