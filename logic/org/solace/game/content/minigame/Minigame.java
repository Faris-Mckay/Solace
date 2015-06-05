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
package org.solace.game.content.minigame;

import java.util.List;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Boundary;
import org.solace.game.map.Location;

/**
 * This is only and idea of how your mini game should look
 * your are not expected to follow this design, but if you can
 * you probably should.
 * Use the INDEPENDANT event type to execute mini game logic
 * 
 * @author Faris
 */
public interface Minigame {
     
    /**
     * List stores of all of the active players within the game
     * @return 
     */
    public List<Player> gamePlayers();
     
    /**
     * Begins the game
     * @param game 
     */
    public void begin();
     
    /**
     * Ends the game
     * @param game 
     */
    public void end();
     
    /**
     * Handles the tick of the active game
     */
    public void process();
     
    /**
     * Checks and returns whether the game event is active
     * @param game
     * @return 
     */
    public boolean gameStarted();
    
    /**
     * Used to flag when a mini game is ready to begin
     * @return 
     */
    public boolean gameShouldBegin();
    
    /**
     * Used to flag when a mini game is ready to be ended
     * @return 
     */
    public boolean gameShouldEnd();
     
    /**
     * removes player given from the list of players within the game
     * @param player
     * @param game 
     */
    public void removePlayer(Player player);
     
    /**
     * Adds player to the list of player inside the game
     * @param player
     * @param game 
     */
    public void addPlayer(Player player);
     
    /**
     * Gets the location that the player is taken to upon starting the game.
     * @return The location that the player is taken to upon starting the game.
     */
    public Location getStartLocation();
     
    /**
     * Gets the name of this game.
     * @return The name of this game.
     */
    public String getName();
     
    /**
     * Gets the boundary in which this game takes place.
     * @return The boundary in which this game takes place.
     */
    public Boundary getBoundary();
     
    /**
     * returns if this instance of a game is safe or not.
     * @return
     */
    public MinigameSafety getMinigameSafety();
     
    /**
     * Decides the repercussions of death based on game safety
     * @param safety 
     */
    public void handleDeath(Player player);
    
    /**
     * Handles the reseting of necessary variables at the end of each mini game
     */
    public void resetMinigameVars();
     
    /**
     * An enumeration that represents item safety within a game
     * Set either Safe or Unsafe to lose/keep items within
     */
    public enum MinigameSafety{
         
       /**
         * This game is a safe zone, items will not be lost.
         */
        SAFE,
         
        /**
         * This game is a danger zone, items will be lost.
         */
        NOT_SAFE
    }

}
