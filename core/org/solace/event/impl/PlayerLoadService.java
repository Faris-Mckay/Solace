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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import org.solace.event.Event;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.util.Constants;

/**
 *
 * @author Faris
 */
public class PlayerLoadService extends Event {

    public PlayerLoadService(Player player) {
        this.player = player;
    }

    Player player;

    @Override
    public void execute() {
    }

    public boolean load() {
        return loadgame(player);
    }

    /**
     * Loads the players details from the binary file
     *
     * @param player The player to be loaded
     * @return
     */
    public boolean loadgame(Player player) {
        String folder = Constants.PLAYER_SAVE_DIRECTORY
                + player.getAuthentication().getUsername().toLowerCase()
                .charAt(0) + "/";
        File file = new File(folder + player.getAuthentication().getUsername()
                + ".dat");
        if (file.exists()) {
            try {
                FileInputStream inFile = new FileInputStream(file);
                DataInputStream load = new DataInputStream(inFile);
                String username = load.readUTF();
                if (!username.equalsIgnoreCase(player.getAuthentication()
                        .getUsername())) {
                    load.close();
                    return false;
                }
                player.getAuthentication().setUsername(username);
                String password = load.readUTF();
                if (!password.equalsIgnoreCase(player.getAuthentication()
                        .getPassword())) {
                    load.close();
                    return false;
                }
                player.getAuthentication().setPassword(password);
                player.getLocation().setX(load.readInt());
                player.getLocation().setY(load.readInt());
                player.getLocation().setH(load.readInt());
                player.getAuthentication().setPlayerRights(load.readInt());
                player.getMobilityManager().running(load.readBoolean());
                player.getSettings().setAutoRetaliating(load.readBoolean());
                player.getSettings().setVolume(load.readInt());
                player.getSettings().setSound(load.readInt());
                player.getSettings().setBrightness(load.readInt());
                for (int i = 0; i < player.getAuthentication().appearanceIndex.length; i++) {
                    player.getAuthentication().setPlayerAppearanceIndex(i,
                            load.readInt());
                }
                for (int i = 0; i < player.getSkills().getPlayerLevel().length; i++) {
                    player.getSkills().getPlayerLevel()[i] = load.readInt();
                }
                for (int i = 0; i < player.getSkills().getPlayerExp().length; i++) {
                    player.getSkills().getPlayerExp()[i] = load.readInt();
                }
                for (int i = 0; i < 28; i++) {
                    int id = load.readInt();
                    if (id != 65535) {
                        int amount = load.readInt();
                        Item item = new Item(id, amount);
                        player.getInventory().set(i, item);
                    }
                }
                for (int i = 0; i < 14; i++) {
                    int id = load.readInt();
                    if (id != 65535) {
                        int amount = load.readInt();
                        Item item = new Item(id, amount);
                        player.getEquipment().set(i, item);
                    }
                }
                for (int i = 0; i < 352; i++) {
                    int id = load.readInt();
                    if (id != 65535) {
                        int amount = load.readInt();
                        Item item = new Item(id, amount);
                        player.getBanking().set(i, item);
                    }
                }
                for (int i = 0; i < player.getPrivateMessaging().getFriends().length; i++) {
                    player.getPrivateMessaging().getFriends()[i] = load.readLong();
                }
                for (int i = 0; i < player.getPrivateMessaging().getIgnores().length; i++) {
                    player.getPrivateMessaging().getIgnores()[i] = load.readLong();
                }
                load.close();
            } catch (Exception e) {
            }
        }
        return true;
    }
}
