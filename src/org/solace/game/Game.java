package org.solace.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.solace.Server;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.task.TaskExecuter;
import org.solace.task.impl.EntityUpdateTask;
import org.solace.task.impl.LogicUpdateTask;
import org.solace.util.Constants;
import org.solace.util.IndexManager;

/**
 * 
 * @author Faris
 */
public class Game {
        
        /**
         * Lists containing all registered mobile entities
         */
	public static Map<Integer, Player> playerRepository = new HashMap<Integer, Player>();
	public static Map<Integer, NPC> npcRepository = new HashMap<Integer, NPC>();
	public static final List<Mobile> mobileRepository = new LinkedList<Mobile>();
        
        /**
         * List containing all players in queue waiting to be registered to game
         */
        public static List<Player> registryQueue = new LinkedList<Player>();
        
        /**
         * Stores the singleton instance of the game class
         */
	public static final Game game = new Game();
        
        /**
         * Initializes the game loop handles
         */
	public static void submitTasks() throws InterruptedException {
		TaskExecuter.get().schedule(new EntityUpdateTask());
                TaskExecuter.get().schedule(new LogicUpdateTask());
	}
        
        /**
         * Returns the game instances
         * @return 
         */
	public static Game getSingleton() {
		return game;
	}
        
        /**
         * Removes a player and logs them out of the game
         * @param player 
         */
	public void deregister(Player player) {
		player.setLogoutRequired(true);
		registryQueue.add(player);
	}
        
        /**
         * Synchronizes the registry queue with the real game repository and clears the registry after
         */
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
        
        /**
         * Stores a players login credentials into the registry queue
         * awaiting the end of cycle where it will be stored in the main list
         * @param player 
         */
	public void register(Player player) {
		if (playerRepository.size() >= Constants.SERVER_MAX_PLAYERS) {
			return;
		}
		Integer pIndex = IndexManager.getIndex();
		player.setIndex(pIndex);
		registryQueue.add(player);
	}
}
