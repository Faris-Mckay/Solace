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
		for (Player player : Game.getPlayerRepository().values()) {
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
