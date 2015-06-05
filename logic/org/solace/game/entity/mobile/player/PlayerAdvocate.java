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
