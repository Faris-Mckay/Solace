package org.solace.task.impl;

import org.solace.game.Game;
import org.solace.game.entity.mobile.MobileUpdateExecutor;
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
		/*
		 * Loops through and handles all player content
		 */
		for (Player player : Game.playerRepository.values()) {
			if (player != null) {
				player.update();
			}
		}

		/*
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
		 * Loops through and first includes player, then NPCS in the update
		 */
		for (Player player : Game.playerRepository.values()) {
			if (player != null) {
                                //MobileUpdateExecutor.includeTask(new PlayerUpdateTask(player));
                               // MobileUpdateExecutor.includeTask(new NPCUpdateTask(player));
                                player.getUpdater().updateMobile();
                                player.getNpcUpdating().updateMobile();
			}
		}
                
                /**
                 * Updates all awaiting tasks
                 */
                MobileUpdateExecutor.getInstance().executeUpdates();

		/*
		 * resets all player update flags
		 */
		for (Player player : Game.playerRepository.values()) {
			if (player != null) {
				player.getUpdater().resetUpdateVars();
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
