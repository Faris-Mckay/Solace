package org.solace.game.content.combat;

import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.task.Task;
import org.solace.task.TaskExecuter;

/**
 * A delayed hit
 * 
 * @author Arithium
 * 
 */
public class SendDelayedHit {

	/**
	 * Sends a delayed hit to the victim
	 * 
	 * @param victim
	 * @param attack
	 */
	public static void sendDelayedHit(final Mobile attacker,
			final Mobile victim, final DelayedAttack attack) {
		TaskExecuter.get().schedule(new Task(attack.getDelay()) {
			@Override
			public void execute() {
				handleAttackBack(attacker, victim);
				victim.hit(attack);
				victim.getUpdateFlags().flag(UpdateFlag.HIT);
				this.stop();
			}
		});

	}

	private static void handleAttackBack(final Mobile attacker, Mobile victim) {
		if (victim instanceof NPC) {
			int modifier = (attacker instanceof Player) ? 32768 : 0;
			NPC npc = (NPC) victim;
			if (!npc.isInCombat()) {
				npc.setInCombat(true);
				npc.setInteractingEntity(attacker);
				npc.setInteractingEntityIndex(attacker.getIndex() + modifier);
				Combat.handleCombatStyle(npc);
			}
		}
	}

}
