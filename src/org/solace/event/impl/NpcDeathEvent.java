package org.solace.event.impl;

import org.solace.game.entity.Animation;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.Mobile.WelfareStatus;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.task.Task;

public class NpcDeathEvent extends Task {

	private NPC npc;

	private int respawnTimer;

	private int deathTimer;

	public NpcDeathEvent(NPC npc) {
		this.npc = npc;
		this.deathTimer = 7;
		npc.setStatus(WelfareStatus.DEAD);
		npc.setInCombat(false);
	}

	@Override
	public void execute() {
		if (deathTimer > 0) {
			deathTimer--;
			if (deathTimer == 5) {
				npc.setAnimation(Animation.create(npc.getDefinition().getDeathAnimation()));
				npc.getUpdateFlags().flag(UpdateFlag.ANIMATION);
			} else if (deathTimer == 1) {
				this.respawnTimer = npc.getDefinition().getRespawn();
				npc.setVisible(false);
			}
		} else {
			if (respawnTimer > 0) {
				respawnTimer--;
			} else {
				npc.setStatus(WelfareStatus.ALIVE);
				npc.setVisible(true);
				npc.setLocation(npc.getTargettedLocation());
				npc.setHitpoints(npc.getDefinition().getHitpoints());
				this.stop();
			}
		}
	}

}
