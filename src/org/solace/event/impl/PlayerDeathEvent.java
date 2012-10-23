package org.solace.event.impl;

import org.solace.game.content.combat.Combat;
import org.solace.game.entity.Animation;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.Mobile.WelfareStatus;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Location;
import org.solace.task.Task;

public class PlayerDeathEvent extends Task {

	private Player player;

	private int respawnTimer = 0;

	public PlayerDeathEvent(Player player) {
		this.player = player;
		respawnTimer = 8;
	}

	@Override
	public void execute() {
		if (player == null) {
			this.stop();
			return;
		}
		if (respawnTimer > 0) {
			Combat.resetCombat(player);
			respawnTimer--;
			if (respawnTimer == 7) {
				player.setStatus(WelfareStatus.DEAD);
				player.setAnimation(Animation.create(836));
				player.getUpdateFlags().flag(UpdateFlag.ANIMATION);
			} else if (respawnTimer == 2) {
				player.setStatus(WelfareStatus.ALIVE);
				player.setLocation(new Location(3222, 3222));
				player.getSkills().getPlayerLevel()[3] = player.getSkills()
						.getLevelForXP(player.getSkills().getPlayerExp()[3]) + 1;
				player.getSkills().refreshSkill(3);
			} else if (respawnTimer == 1) {
				player.setAnimation(Animation.create(65535));
				player.getUpdateFlags().flag(UpdateFlag.ANIMATION);
				player.getUpdater().setTeleporting(true);
			}
		} else {
			this.stop();
		}
	}

}
