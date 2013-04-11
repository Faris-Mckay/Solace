package org.solace.event.impl;

import org.solace.event.Event;
import org.solace.game.content.combat.Combat;
import org.solace.game.content.skills.SkillHandler;
import org.solace.game.entity.Animation;
import org.solace.game.entity.mobile.Mobile.WelfareStatus;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Location;

public class PlayerDeathService extends Event {

	private Player player;

	private int respawnTimer = 0;

	public PlayerDeathService(Player player) {
		this.player = player;
		respawnTimer = 7;
		Combat.resetCombat(player);
		player.setStatus(WelfareStatus.DEAD);
	}

	@Override
	public void execute() {
		if (respawnTimer > 0) {
			respawnTimer--;
			if (respawnTimer == 6) {
				player.setAnimation(Animation.create(836));
			} else if (respawnTimer == 1) {
				revivePlayer(player);
			}
		} else {
			this.stop();
		}
	}

	public void revivePlayer(Player player) {
		/*
		 * Stops the movement
		 */
		player.getMobilityManager().stopMovement();
		/*
		 * Sets the new location
		 */
		player.getMobilityManager().processTeleport(player, new Location(3222, 3222));

		/*
		 * Resets the animation
		 */
		player.setAnimation(Animation.create(65535));
		/*
		 * Refreshes the stats
		 */
		for (int i = 0; i < SkillHandler.MAXIMUM_SKILLS; i++) {
			player.getSkills().getPlayerLevel()[i] = player.getSkills().getLevelForXP(player.getSkills().getPlayerExp()[i] + 1);
			player.getSkills().refreshSkill(i);
		}
		/*
		 * Sets the player as alive
		 */
		player.setStatus(WelfareStatus.ALIVE);
	}

}
