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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Logger;

import org.solace.util.XStreamUtil;

public class NPCLoading {

    public static Logger logger = Logger.getLogger(NPCLoading.class.getName());

    @SuppressWarnings("unchecked")
    public static void loadSpawns() throws FileNotFoundException {
        logger.info("Loading NPC Spawns...");
        List<NPC> list = (List<NPC>) XStreamUtil.getXStream().fromXML(new FileInputStream("./data/xml/npcs/npcspawns.xml"));
        for (NPC spawn : list) {
            try {
                NPCDefinition def = NPCDefinition.getDefinitions()[spawn.getNpcId()];
                if (def == null) {
                    continue;
                }
                NPC npc = new NPC(def, spawn.getNpcId());
                npc.setLocation(spawn.getLocation());
                npc.targettedLocation(spawn.getLocation());
                npc.setMaximumWalkingDistance(spawn.getMaximumWalkingDistance());
                npc.setMoveStatus(spawn.getMoveStatus());
                NPCAdvocate.addNpc(npc);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
