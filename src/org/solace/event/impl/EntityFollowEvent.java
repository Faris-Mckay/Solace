package org.solace.event.impl;

import org.solace.game.entity.mobile.Mobile;
import org.solace.game.map.Location;
import org.solace.task.Task;

public class EntityFollowEvent extends Task {

	private final Mobile leader, follower;

	private final boolean combatFollow;

	public EntityFollowEvent(Mobile leader, Mobile follower, boolean combatFollow) {
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
		if (!combatFollow) {
			Location last = leader.getLocation();
			if (last == null)
				return;
			if (last.lastX == 0 || last.lastY == 0)
				return;
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
		follower.getUpdateFlags().faceEntity();
	}

}
