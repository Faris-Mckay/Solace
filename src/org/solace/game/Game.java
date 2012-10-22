package org.solace.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.solace.Server;
import org.solace.task.TaskExecuter;
import org.solace.task.impl.NPCUpdateTask;
import org.solace.task.impl.PlayerUpdateTask;
import org.solace.util.Constants;
import org.solace.util.IndexManager;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class Game {
    
    public static Map<Integer,Player> playerRepository = new HashMap<Integer,Player>();
    public static List<Player> registryQueue = new LinkedList<Player>();
    public static Map<Integer, NPC> npcRepository = new HashMap<Integer, NPC>();
    public static final List<Mobile> mobileRepository = new LinkedList<Mobile>();
    public static final Game game = new Game();
    
    public static void submitMobileTask(){
        Server.logger.info("Task update handlers intitializing...");
        TaskExecuter.get().schedule(new NPCUpdateTask());
        TaskExecuter.get().schedule(new PlayerUpdateTask());
    }

    public static Game getSingleton() {
        return game;
    }

    public void deregister(Player player) {
        player.setLogoutRequired(true);
        registryQueue.add(player);
    }

    public void syncCycleRegistrys() {
        if (registryQueue.isEmpty()) {
            return;
        }
        for (Player player : registryQueue) {
            if (player.isLogoutRequired()) {
                playerRepository.remove(player.getIndex());
                IndexManager.freeIndex(player.getIndex());
                player = null;
            } else {
                playerRepository.put(player.getIndex(), player);
            }
        }
        registryQueue.clear();
    }

    public void register(Player player) {
        if (playerRepository.size() >= Constants.SERVER_MAX_PLAYERS) {
            return;
        }
        Integer pIndex = IndexManager.getIndex();
        player.setIndex(pIndex);
        registryQueue.add(player);
    }

}
