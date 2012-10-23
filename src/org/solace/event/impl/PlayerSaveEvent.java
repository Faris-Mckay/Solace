package org.solace.event.impl;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.solace.event.Event;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.util.Constants;

/**
 *
 * @author Faris
 */
public class PlayerSaveEvent extends Event {
    
    public PlayerSaveEvent(Player player){
        super(EventType.Standalone, 0 , false);
        this.player = player;
    }
    
    Player player;

    @Override
    public void execute() {
        try {
            saveGame(player);
        } catch (Exception ex) {
            Logger.getLogger(PlayerSaveEvent.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    /**
     * Saves all of the data for the player
     * @param player The player being saved
     * @throws Exception Catches any exception that may be sent
     */
    public void saveGame(Player player) throws Exception {
            String folder = Constants.PLAYER_SAVE_DIRECTORY + player.getAuthentication().getUsername().toLowerCase() .charAt(0) + "/";
            File save = new File(folder + player.getAuthentication().getUsername()+ ".dat");
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
            write.writeUTF(player.getAuthentication().getUsername());
            write.writeUTF(player.getAuthentication().getPassword());
            write.writeInt(player.getLocation().getX());
            write.writeInt(player.getLocation().getY());
            write.writeInt(player.getLocation().getH());
            write.writeBoolean(player.getMobilityManager().running());
            write.writeInt(player.getAuthentication().getPlayerRights());
            for (int i = 0; i < player.getAuthentication().appearanceIndex.length; i++) {
                    write.writeInt(player.getAuthentication().getPlayerAppearanceIndex(i));
            }
            for (int i = 0; i < player.getSkills().getPlayerLevel().length; i++) {
                    write.writeInt(player.getSkills().getPlayerLevel()[i]);
            }
            for (int i = 0; i < player.getSkills().getPlayerExp().length; i++) {
                    write.writeInt((int) player.getSkills().getPlayerExp()[i]);
            }
            for (int i = 0; i < 28; i++) {
                    Item item = player.getInventory().items()[i];
                    if (item == null) {
                            write.writeInt(65535);
                    } else {
                            write.writeInt(item.getIndex());
                            write.writeInt(item.getAmount());
                    }
            }
            for (int equip = 0; equip < 14; equip++) {
                    Item item = player.getEquipment().items()[equip];
                    if (item == null) {
                            write.writeInt(65535);
                    } else {
                            write.writeInt(item.getIndex());
                            write.writeInt(item.getAmount());
                    }
            }
            write.close();
    }

}
