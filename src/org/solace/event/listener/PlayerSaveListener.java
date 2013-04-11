package org.solace.event.listener;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import mint.event.EventHandler;
import mint.event.EventListener;
import org.solace.event.impl.PlayerSaveEvent;
import org.solace.game.item.Item;
import org.solace.util.Constants;

public class PlayerSaveListener implements EventListener {
	
	@EventHandler
	public void saveGame(PlayerSaveEvent event) throws IOException {
		String folder = Constants.PLAYER_SAVE_DIRECTORY
				+ event.getPlayer().getAuthentication().getUsername().toLowerCase()
						.charAt(0) + "/";
		File save = new File(folder + event.getPlayer().getAuthentication().getUsername()
				+ ".dat");
		if (!save.exists()) {
			try {
				new File(folder).mkdir();
				save.createNewFile();
			} catch (IOException e) {
			}
		} else {
			save.delete();
		}
		FileOutputStream outFile = new FileOutputStream(save);
		DataOutputStream write = new DataOutputStream(outFile);
		write.writeUTF(event.getPlayer().getAuthentication().getUsername());
		write.writeUTF(event.getPlayer().getAuthentication().getPassword());
		write.writeInt(event.getPlayer().getLocation().getX());
		write.writeInt(event.getPlayer().getLocation().getY());
		write.writeInt(event.getPlayer().getLocation().getH());
		write.writeInt(event.getPlayer().getAuthentication().getPlayerRights());
		write.writeBoolean(event.getPlayer().getMobilityManager().running());
		write.writeBoolean(event.getPlayer().getSettings().isAutoRetaliating());
		write.writeInt(event.getPlayer().getSettings().getVolume());
		write.writeInt(event.getPlayer().getSettings().getSound());
		write.writeInt(event.getPlayer().getSettings().getBrightness());
		for (int i = 0; i < event.getPlayer().getAuthentication().appearanceIndex.length; i++) {
			write.writeInt(event.getPlayer().getAuthentication().getPlayerAppearanceIndex(
					i));
		}
		/*
		 * Saves the players levels
		 */
		for (int i = 0; i < event.getPlayer().getSkills().getPlayerLevel().length; i++) {
			write.writeInt(event.getPlayer().getSkills().getPlayerLevel()[i]);
		}
		/*
		 * Saves the players experience
		 */
		for (int i = 0; i < event.getPlayer().getSkills().getPlayerExp().length; i++) {
			write.writeInt((int) event.getPlayer().getSkills().getPlayerExp()[i]);
		}
		/*
		 * Saves the players items
		 */
		for (int i = 0; i < 28; i++) {
			Item item = event.getPlayer().getInventory().items()[i];
			if (item == null) {
				write.writeInt(65535);
			} else {
				write.writeInt(item.getIndex());
				write.writeInt(item.getAmount());
			}
		}
		/*
		 * Saves the players equipment
		 */
		for (int equip = 0; equip < 14; equip++) {
			Item item = event.getPlayer().getEquipment().items()[equip];
			if (item == null) {
				write.writeInt(65535);
			} else {
				write.writeInt(item.getIndex());
				write.writeInt(item.getAmount());
			}
		}
		/*
		 * Saves the players bank
		 */
		for (int bank = 0; bank < 352; bank++) {
			Item item = event.getPlayer().getBanking().items()[bank];
			if (item == null) {
				write.writeInt(65535);
			} else {
				write.writeInt(item.getIndex());
				write.writeInt(item.getAmount());
			}
		}
		/*
		 * Saves the players friends
		 */
		for (int i = 0; i < event.getPlayer().getPrivateMessaging().getFriends().length; i++) {
			write.writeLong(event.getPlayer().getPrivateMessaging().getFriends()[i]);
		}
		/*
		 * Saves the players ignore list
		 */
		for (int i = 0; i < event.getPlayer().getPrivateMessaging().getIgnores().length; i++) {
			write.writeLong(event.getPlayer().getPrivateMessaging().getIgnores()[i]);
		}
		write.close();
	}

}
