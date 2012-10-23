package org.solace.game.entity.mobile.player;

/**
 * 
 * @author Faris
 */
public class PlayerSettings {

	private int brightness = 3;

	private int volume = 4;

	private int sound = 4;

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

	public static void handleButtons(Player player, int buttonId) {
		switch (buttonId) {
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
