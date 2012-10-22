package org.solace.game.content.music;
 
import org.solace.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class MusicHandler {
     
    /**
     * Constructor allocates client to be updated
     * @param player 
     */
    public MusicHandler(Player player) {
        this.player = player;
    }
     
    Player player; WorldData worldData = new WorldData(); MusicData songData = new MusicData();
     
    private int currentArea, currentSong = 0;
     
    /**
     * Method which executes the music change
     */
    public void handleMusic() {
        updateAreaID(); 
        updateSongID();
        player.getPacketDispatcher().sendSong(currentSong);
    }
     
    /**
     * Sets the variable equal to the relevant Song ID
     */
    private void setSongID() {
        currentSong = songData.getSongID(currentArea);
    }
     
    /**
     * calls the songID to be update
     */
    private void updateSongID() {
        setSongID();
    }
     
    /**
     * Sets area ID as the currentArea variable
     */
    private void setAreaID() {
        currentArea = worldData.getAreaID(player);
    }
     
    /**
     * Calls area ID to be updated
     */
    private void updateAreaID() {
        setAreaID();
    }
}