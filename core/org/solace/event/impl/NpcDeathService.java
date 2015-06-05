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
package org.solace.event.impl;

import org.solace.event.Event;
import org.solace.game.content.combat.Combat;
import org.solace.game.entity.Animation;
import org.solace.game.entity.mobile.Mobile.WelfareStatus;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.npc.NPCAdvocate;

public class NpcDeathService extends Event {

    private NPC npc;

    private int respawnTimer;

    private int deathTimer;

    public NpcDeathService(NPC npc) {
        this.npc = npc;
        this.deathTimer = 7;
        Combat.resetCombat(npc);
    }

    @Override
    public void execute() {
        if (deathTimer > 0) {
            deathTimer--;
            if (deathTimer == 5) {
                npc.setAnimation(Animation.create(npc.getDefinition().getDeathAnimation()));
            } else if (deathTimer == 1) {
                this.respawnTimer = npc.getDefinition().getRespawn();
                npc.setVisible(false);
            }
        } else {
            if (respawnTimer > 0) {
                respawnTimer--;
            } else {
                if (!npc.isNpcSpawned()) {
                    npc.setStatus(WelfareStatus.ALIVE);
                    npc.setVisible(true);
                    npc.setLocation(npc.getTargettedLocation());
                    npc.setHitpoints(npc.getDefinition().getHitpoints());
                } else {
                    NPCAdvocate.removeNpc(npc);
                }
                this.stop();
            }
        }
    }

}
