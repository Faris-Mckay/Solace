/*
 * This file is part of Solace Framework.
 * Solace is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Solace is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Solace. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.solace.game.entity.mobile.npc;

import java.io.IOException;
import org.solace.util.IndexManager;
import org.solace.game.Game;
import org.solace.game.entity.mobile.Mobile.MovementStatus;
import org.solace.game.map.Location;

/**
 *
 * @author Faris
 */
public class NPCAdvocate {

    public static void loadNPCDefs() throws IOException {
        NPCDefinition.init();
    }

    public static void loadNPCDropData() {
        //NPCDropController.init();
    }

    public static void addNpc(NPC npc) {
        Integer npcIndex = IndexManager.getNpcIndex();
        Game.getNpcRepository().put(npcIndex, npc);
        npc.setIndex(npcIndex);
    }

    public static void removeNpc(NPC givenNpc) {
        for (NPC npc : Game.getNpcRepository().values()) {
            if (npc == givenNpc) {
                npc.setIndex(npc.getIndex());
                Game.getNpcRepository().remove(givenNpc);
                npc = null;
            }
        }
    }

    public static void spawnNPC(int id, Location location, int walkDistance) {
        NPCDefinition def = NPCDefinition.getDefinitions()[id];
        if (def == null) {
            return;
        }
        NPC npc = new NPC(def, id);
        npc.setLocation(location);
        npc.targettedLocation(location);
        npc.setMaximumWalkingDistance(walkDistance);
        npc.setMoveStatus(MovementStatus.STATIONARY);
        NPCAdvocate.addNpc(npc);
    }

}
