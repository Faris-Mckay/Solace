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
package org.solace.game.entity.mobile.player.command;

import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.mobile.player.PrivilegeRank;

/**
 *
 * @author Faris <https://github.com/faris-mckay>
 */
public abstract class Command {
	
    /**
     * Checks if the user can use the command, and passes it to be executed 
     * @param player
     * @param command 
     */
    public void execute(Player player, String command){
        if(!checkRequirements(player)){
            player.getPacketDispatcher().sendMessage("You do not have the required rights for this command.");
            return;
        }
        handle(player, command);
    }
    
    public abstract void handle(Player player, String command);
    
    /**
     * must return the rank required by player to execute command
     * @return 
     */
    public abstract PrivilegeRank rightsRequired();

    /**
     * Tells us if the user can or not not use the given command
     * @param player
     * @return 
     */
    public boolean checkRequirements(Player player){
        if(rightsRequired() == PrivilegeRank.MODERATOR){
            if(player.getAuthentication().getPlayerRank() == PrivilegeRank.STANDARD){
                return false;
            }
        }
        if(rightsRequired() == PrivilegeRank.ADMINISTRATOR){
            if(player.getAuthentication().getPlayerRank() == PrivilegeRank.STANDARD || player.getAuthentication().getPlayerRank() == PrivilegeRank.MODERATOR){
                return false;
            }
        }
        if(rightsRequired() == PrivilegeRank.OWNER){
            if(player.getAuthentication().getPlayerRank() != PrivilegeRank.OWNER){
                return false;
            }
        }
        return true;
    }

}
