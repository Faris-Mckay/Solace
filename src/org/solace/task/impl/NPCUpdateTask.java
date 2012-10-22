package org.solace.task.impl;

import org.solace.game.Game;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.task.Task;

/**
 * 
 * @author Faris
 */
public class NPCUpdateTask extends Task {

	@Override
	public void execute() {
            /**
             * Loops through all NPCs and handles all logic to be updated (e.g Combat and animations)
             */
            for (NPC npc : Game.npcRepository.values()) {
                    if (npc != null) {
                            npc.update();
                    }
            }
	}

}
