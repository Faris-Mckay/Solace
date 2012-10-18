package org.solace.task.impl;

import org.solace.task.Task;
import org.solace.world.game.Game;
import org.solace.world.game.entity.mobile.npc.NPC;

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
