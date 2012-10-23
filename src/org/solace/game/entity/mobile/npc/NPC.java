package org.solace.game.entity.mobile.npc;

import org.solace.game.content.combat.Combat;
import org.solace.game.content.combat.DelayedAttack;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.map.Location;
import org.solace.task.TaskExecuter;
import org.solace.event.impl.NpcDeathEvent;

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

	@Override
	public void update() {
		getMobilityManager().processMovement();
		if (getStatus() != WelfareStatus.DEAD) {
			Combat.handleCombatTick(this);
		}
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

	@Override
	public void hit(DelayedAttack hit) {
		int damage = hit.getDamage();
		if ((getHitpoints() - damage) <= 0) {
			damage = getHitpoints();
		}
		setHitpoints(getHitpoints() - damage);
		getUpdateFlags().setDamage(damage);
		getUpdateFlags().setHitMask(hit.getHitmask());
		if (getHitpoints() <= 0) {
			TaskExecuter.get().schedule(new NpcDeathEvent(this));
		}
	}

}
