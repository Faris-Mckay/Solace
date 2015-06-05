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

import org.solace.Server;
import org.solace.event.impl.NpcDeathService;
import org.solace.game.content.combat.Combat;
import org.solace.game.content.combat.impl.Hit;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.map.Location;

/**
 * Represents a single NPC mobile
 *
 * @author Faris
 * @author Arithium
 */
public class NPC extends Mobile {

    /**
     * Constructs a new NPC mobile
     */
    public NPC(NPCDefinition def, int npcId) {
        super(new Location(3222, 3222));
        this.definition = def;
        this.npcId = npcId;
        setHitpoints(definition.getHitpoints());
        isVisible = true;
        handleNpcAttributes();
    }

    private NPCDefinition definition;

    /**
     * Determines whether the npc is visible or not
     */
    private boolean isVisible;

    /**
     * The npcs current npc id
     */
    private int npcId;

    /**
     * The npcs current hitpoints amount
     */
    private int hitpoints;

    private boolean spawnedNpc;

    @Override
    public void update() {
        getMobilityManager().processMovement();
        if (getStatus() != WelfareStatus.DEAD) {
            Combat.handleCombatTick(this);
        }
    }

    private void handleNpcAttributes() {
        addAttribute("FROZEN", Boolean.FALSE);
        addAttribute("IMMUNE", Boolean.FALSE);
    }

    /**
     * Returns whether the npc is visible or not
     *
     * @return
     */
    public boolean isNpcVisible() {
        return isVisible;
    }

    /**
     * Sets whether the npc is visible or not
     *
     * @param visible
     */
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    /**
     * returns the npcs id
     *
     * @return
     */
    public int getNpcId() {
        return npcId;
    }

    public int getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(int hitpoints) {
        this.hitpoints = hitpoints;
    }

    public NPCDefinition getDefinition() {
        return definition;
    }

    public boolean isNpcSpawned() {
        return spawnedNpc;
    }

    public void setNpcSpawned(boolean spawned) {
        this.spawnedNpc = spawned;
    }

    @Override
    public void hit(Hit hit) {
        if (getStatus() != WelfareStatus.DEAD) {
            int damage = hit.getDamage();
            if ((getHitpoints() - damage) <= 0) {
                damage = getHitpoints();
            }
            setHitpoints(getHitpoints() - damage);
            if (hit.getHitmask() == 1) {
                getUpdateFlags().setDamage(damage);
                getUpdateFlags().setHitType(hit.getHitType());
            } else if (hit.getHitmask() == 2) {
                getUpdateFlags().setDamage2(damage);
                getUpdateFlags().setHitType2(hit.getHitType());
            }
            if (getHitpoints() <= 0) {
                setStatus(WelfareStatus.DEAD);
                Server.getService().schedule(new NpcDeathService(this));
            }
        }
    }

}
