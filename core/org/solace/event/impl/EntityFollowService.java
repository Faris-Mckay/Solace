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
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Location;
import org.solace.util.ProtocolUtils;

public class EntityFollowService extends Event {

    private final Mobile leader, follower;
    private final boolean combatFollow;

    public EntityFollowService(Mobile leader, Mobile follower, boolean combatFollow) {
        this.leader = leader;
        this.follower = follower;
        this.combatFollow = combatFollow;
    }

    @Override
    public void execute() {
        if (follower.getInteractingEntity() == null || leader == null
                || follower == null) {
            this.stop();
            return;
        }
        if (ProtocolUtils.getDistance(follower.getLocation(), leader.getLocation()) > 16) {
            this.stop();
            return;
        }
        if (follower.getInteractingEntity() != leader) {
            this.stop();
            return;
        }
        if (!combatFollow) {
            Location last = leader.getLocation();
            if (last == null) {
                return;
            }
            if (last.lastX == 0 || last.lastY == 0) {
                return;
            }
            follower.getMobilityManager().prepare();
            follower.getMobilityManager().queueDestination(
                    new Location(last.lastX, last.lastY));
            follower.getMobilityManager().finish();
        } else {
            follower.getMobilityManager().prepare();
            int x = leader.getLocation().getX();
            if (follower.getLocation().getX() > x) {
                x += 1;
            } else if (follower.getLocation().getX() < x) {
                x -= 1;
            }
            int y = leader.getLocation().getY();
            if (follower.getLocation().getY() > y) {
                y += 1;
            } else if (follower.getLocation().getY() < y) {
                y -= 1;
            }
            follower.getMobilityManager().prepare();
            follower.getMobilityManager().queueDestination(
                    new Location(x, y));
            follower.getMobilityManager().finish();
        }
        follower.getUpdateFlags().faceEntity(leader.getIndex() + ((leader instanceof Player) ? 32768 : 0));
    }
}
