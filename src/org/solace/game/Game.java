package org.solace.world.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.solace.Server;
import org.solace.task.TaskExecuter;
import org.solace.task.impl.NPCUpdateTask;
import org.solace.task.impl.PlayerUpdateTask;
import org.solace.world.game.entity.mobile.npc.NPC;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class Game {
    public static Map<Integer,Player> playerRepository = new HashMap<Integer,Player>();
    public static List<Player> registryQueue = new LinkedList<Player>();
    public static Map<Integer, NPC> npcRepository = new HashMap<Integer, NPC>();
    
    public static void beginUpdatingMobiles(){
        Server.logger.info("Task update handlers intitializing...");
        TaskExecuter.get().schedule(new NPCUpdateTask());
        TaskExecuter.get().schedule(new PlayerUpdateTask());
    }

}
