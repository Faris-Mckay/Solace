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
package org.solace.game.entity.mobile.player;

/**
 * 
 * @author Faris
 */
public class PlayerSettings {

	private int brightness = 3;

	private int volume = 4;

	private int sound = 4;
	
	private boolean autoRetaliating;
	
	private boolean autoCasting;

	public int getBrightness() {
		return brightness;
	}

	public void setBrightness(int brightness) {
		this.brightness = brightness;
	}

	public int getVolume() {
		return volume;
	}

	public void setVolume(int volume) {
		this.volume = volume;
	}

	public int getSound() {
		return sound;
	}

	public void setSound(int sound) {
		this.sound = sound;
	}
	
	public boolean isAutoRetaliating() {
		return autoRetaliating;
	}

	public void setAutoRetaliating(boolean auto) {
		this.autoRetaliating = auto;
	}
	
	public boolean isAutoCasting() {
		return autoCasting;
	}
	
	public void setAutoCasting(boolean auto) {
		this.autoCasting = auto;
	}

	public static void handleButtons(Player player, int buttonId) {
		switch (buttonId) {
		/*
		 * Auto retaliate on
		 */
		case 150:
			player.getSettings().setAutoRetaliating(true);
			break;
		/*
		 * Auto retaliate off
		 */
		case 151:
			player.getSettings().setAutoRetaliating(false);
			break;
		/*
		 * Run toggle off
		 */
		case 152:
			player.getMobilityManager().running(false);
			player.getPacketDispatcher().sendConfig(173, 0);
			break;
		/*
		 * Run toggle on
		 */
		case 153:
			player.getMobilityManager().running(true);
			player.getPacketDispatcher().sendConfig(173, 1);
			break;
		}
	}

}
