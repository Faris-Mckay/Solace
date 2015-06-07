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

import java.util.HashMap;
import java.util.Map;

import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.mobile.player.command.impl.ChangeLookCommand;
import org.solace.game.entity.mobile.player.command.impl.CheckPositionCommand;
import org.solace.game.entity.mobile.player.command.impl.EmptyInventoryCommand;
import org.solace.game.entity.mobile.player.command.impl.MapTeleportCommand;
import org.solace.game.entity.mobile.player.command.impl.MasterStatsCommand;
import org.solace.game.entity.mobile.player.command.impl.PlayAnimationCommand;
import org.solace.game.entity.mobile.player.command.impl.PlayGraphicCommand;
import org.solace.game.entity.mobile.player.command.impl.PlayerTeleportCommand;
import org.solace.game.entity.mobile.player.command.impl.PlayersCheckCommand;
import org.solace.game.entity.mobile.player.command.impl.SetLevelCommand;
import org.solace.game.entity.mobile.player.command.impl.SetSidebarCommand;
import org.solace.game.entity.mobile.player.command.impl.SpawnItemCommand;
import org.solace.game.entity.mobile.player.command.impl.SpawnNPCCommand;
import org.solace.game.entity.mobile.player.command.impl.SpawnObjectCommand;

/**
 * Handles the storing and processing of commands
 *
 * @author Faris <https://github.com/faris-mckay>
 *
 */
public class CommandHandler {
    
    private static Map<String, Command> commandMap = new HashMap<String, Command>();
    
    public static void loadCommands() {
        commandMap.put("item", new SpawnItemCommand());
        commandMap.put("npc", new SpawnNPCCommand());
        commandMap.put("players", new PlayersCheckCommand());
        commandMap.put("master", new MasterStatsCommand());
        commandMap.put("object", new SpawnObjectCommand());
        commandMap.put("anim", new PlayAnimationCommand());
        commandMap.put("gfx", new PlayGraphicCommand());
        commandMap.put("mypos", new CheckPositionCommand());
        commandMap.put("tele", new MapTeleportCommand());
        commandMap.put("teleto", new PlayerTeleportCommand());
        commandMap.put("setlevel", new SetLevelCommand());
        commandMap.put("sidebar", new SetSidebarCommand());
        commandMap.put("empty", new EmptyInventoryCommand());
        commandMap.put("changelook", new ChangeLookCommand());
    }
    
    public static void processCommand(Player player, String command) {
        String[] args = command.split(" ");
        try {
            Command comm = commandMap.get(args[0].toLowerCase());
            if (comm == null) {
                player.getPacketDispatcher().sendMessage("Command " + args[0] + " does not exist.");
                return;
            }
            comm.execute(player, command);
        } catch (Exception e) {
            player.getPacketDispatcher().sendMessage("Command " + args[0] + " does not exist.");
        }
    }
    
}
