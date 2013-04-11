package org.solace.game.content.combat.impl;

import org.solace.Server;
import org.solace.event.Event;
import org.solace.event.impl.EntityFollowService;
import org.solace.game.content.combat.Combat;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.Mobile.WelfareStatus;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;

/**
 * Submits a delayed hit
 * 
 * @author Arithium
 * 
 */
public class DelayedHit {

	/**
	 * Sends a delayed hit to the victim
	 * 
	 * @param victim
	 * @param attack
	 */
	public static void submit(final Mobile attacker, final Mobile victim, final Hit attack) {
		Server.getService().schedule(new Event(attack.getDelay()) {
			@Override
			public void execute() {
				if (victim.getStatus() == WelfareStatus.DEAD || attacker.getStatus() == WelfareStatus.DEAD) {
					this.stop();
					return;
				}
				handleAttackBack(attacker, victim);
				if (attack.getHitmask() == 1) {
					victim.hit(attack);
					victim.getUpdateFlags().flag(UpdateFlag.HIT);
				} else if (attack.getHitmask() == 2){
					victim.hit(attack);
					victim.getUpdateFlags().flag(UpdateFlag.HIT_2);
				}
				this.stop();
			}
		});

	}

	private static void handleAttackBack(final Mobile attacker, Mobile victim) {
		if (victim instanceof NPC) {
			NPC npc = (NPC) victim;
			if (!npc.isInCombat()) {
				npc.setInCombat(true);
				npc.setInteractingEntity(attacker);
				npc.setInteractingEntityIndex(attacker.getIndex());
				npc.getUpdateFlags().faceEntity(attacker.getIndex() + ((victim instanceof Player) ? 32768 : 0));
				Combat.handleCombatStyle(npc);
				Server.getService().schedule(new EntityFollowService(attacker, victim, true));
			}
		} else if (victim instanceof Player) {
			Player player = (Player) victim;
			if (player.getSettings().isAutoRetaliating()) {
				player.setInCombat(true);
				player.setInteractingEntity(attacker);
				player.setInteractingEntityIndex(attacker.getIndex());
				player.getUpdateFlags().faceEntity(attacker.getIndex() + ((victim instanceof Player) ? 32768 : 0));
				Combat.handleCombatStyle(player);
				Server.getService().schedule(new EntityFollowService(attacker, victim, true));
			}
		}
	}

}
