package org.solace.game.content;

import org.solace.Server;
import org.solace.event.Event;
import org.solace.game.entity.Animation;
import org.solace.game.entity.Graphic;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Location;

/**
 * Handles the teleporting function
 * 
 * @author Arithium
 * 
 */
public class TeleportHandler {
	
	public static void handleTeleport(final Player player, final Location location, final int teleportTime, final int gfxDelay) {
		player.setAnimation(Animation.create(714));
		player.addAttribute("TELEPORTING", Boolean.TRUE);
		Server.getService().schedule(new Event(1) {
			int teleportTimer = teleportTime;
			@Override
			public void execute() {
				if (teleportTimer > 0) {
					teleportTimer--;
					if (teleportTimer == gfxDelay) {
						player.setGraphic(Graphic.highGraphic(308, 0));
					} else if (teleportTimer == 2) {
						player.setAnimation(Animation.create(715));
						player.getMobilityManager().processTeleport(player, location);
					}
				} else {
					player.addAttribute("TELEPORTING", Boolean.FALSE);
					this.stop();
				}
			}
		});
	}
	
	private static final int[][] MODERN_TELEPORT_BUTTONS = { 
		{ 1164, 3210, 3424, 0 }, // varrock
		{ 1167, 3234, 3218, 0 }, // lumbridge
		{ 1170, 2964, 3378, 0 }, // falador
		{ 1174, 2757, 3478, 0 }, // camelot
	};
	
	public static void processModernTeleport(Player player, int buttonId) {
		if ((Boolean) player.getAttribute("TELEPORTING")) {
			return;
		}
		for (int i = 0; i < MODERN_TELEPORT_BUTTONS.length; i++) {
			if (buttonId == MODERN_TELEPORT_BUTTONS[i][0]) {
				Location location = new Location(MODERN_TELEPORT_BUTTONS[i][1], MODERN_TELEPORT_BUTTONS[i][2], MODERN_TELEPORT_BUTTONS[i][3]);
				handleTeleport(player, location, 4, 3);
			}
		}
	}

}
