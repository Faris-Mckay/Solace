package org.solace.game.content.combat.impl;

import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;
import org.solace.util.ProtocolUtils;

/**
 * Fires a projectile to the location
 * @author Arithium
 *
 */
public class FireProjectile {

	public static void submit(Projectile projectile) {
		for (Player player : Game.playerRepository.values()) {
			if (player == null)
				continue;
			if (ProtocolUtils.getDistance(projectile.getLocation(),
					player.getLocation()) <= 60) {
				player.getPacketDispatcher().sendProjectile(
						projectile.getLocation(), projectile.getOffsetX(),
						projectile.getOffsetY(), projectile.getId(),
						projectile.getStartHeight(), projectile.getEndHeight(),
						projectile.getSpeed(), projectile.getLockon());
			}
		}
	}

}
