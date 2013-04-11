package org.solace.game.entity.mobile.player;

import org.solace.game.entity.Animation;
import org.solace.game.entity.Graphic;

/**
 *
 * @author Faris
 */
public class PlayerAdvocate {
    
    /**
     * Caches the player object
     * @param player 
     */
    public PlayerAdvocate(Player player){
        this.player = player;
    }
    
    Player player;
    
    /**
     * Plays a player animation
     * @param animId 
     */
    public void queueAnim(int animId){
        player.setAnimation(Animation.create(animId));
    }
    
    /**
     * Stops the player at its current coordinates
     */
    public void haltMovement(){
        player.getMobilityManager().prepare();
    }
    
    /**
     * Plays a graphic to the players
     * @param gfx is the id
     * @param high or low setting
     */
    public void queueGFX(int gfx, boolean high){
        if(high){
            player.setGraphic(Graphic.highGraphic(gfx, 0));
        } else {
            player.setGraphic(Graphic.lowGraphic(gfx, 0)); 
        }
    }
    
    /**
     * Closes the players interfaces on the screen
     */
    public void closeAllInterfaces(){
        player.getPacketDispatcher().sendCloseInterface();
    }
    
    /**
     * Sends a message to the players chat box
     * @param message 
     */
    public void displayChatboxText(String message){
        player.getPacketDispatcher().sendMessage(message);
    }
    
    

}
