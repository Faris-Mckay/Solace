package org.solace.network.packet.impl;

import org.solace.event.impl.PickupGroundItemService;
import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Location;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.task.Task;

public class PickupGroundItemPacket implements PacketHandler {

	@Override
	public void handlePacket(final Player player, Packet packet) {
		final int itemY = packet.getLEShort();
		final int itemIndex = packet.getUShort();
		final int itemX = packet.getLEShort();

		final Location location = new Location(itemX, itemY, player.getLocation().getH());
		
		/*
		 * Post new walk to action task.
		 */
		player.walkToAction(new Task(1) {
			boolean arrived = false;
			@Override
			public void execute() {
				if (arrived) {
					new PickupGroundItemService(player, location, itemIndex).execute();
					player.walkToAction(null);
					stop();
				}
				if (player.getLocation().sameAs(location)) {
					arrived = true;
				}
			}
		});
		Game.submit(player.walkToAction());
	}

}
