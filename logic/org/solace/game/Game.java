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
package org.solace.game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.task.Task;
import org.solace.task.TaskExecuter;
import org.solace.task.impl.EngineCleanTask;
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
    private static Map<Integer, Player> playerRepository = new HashMap<Integer, Player>();
    private static Map<Integer, NPC> npcRepository = new HashMap<Integer, NPC>();
    private static List<Mobile> mobileRepository = new LinkedList<Mobile>();

    /**
     * List containing all players in queue waiting to be registered to game
     */
    public static LinkedList<Player> registryQueue = new LinkedList<Player>();
    

    /**
     * Stores the singleton instance of the game class
     */
    public static final Game game = new Game();

    /**
     * Initializes the game loop handles
     */
    public static void submitTasks() throws InterruptedException {
        TaskExecuter.get().schedule(new EntityUpdateTask());
        TaskExecuter.get().schedule(new EngineCleanTask());
        TaskExecuter.get().schedule(new LogicUpdateTask());
    }

    /**
     * Returns the game instances
     *
     * @return
     */
    public static Game getSingleton() {
        return game;
    }

    /**
     * @return the playerRepository
     */
    public static Map<Integer, Player> getPlayerRepository() {
        return playerRepository;
    }

    /**
     * @return the npcRepository
     */
    public static Map<Integer, NPC> getNpcRepository() {
        return npcRepository;
    }

    /**
     * @return the mobileRepository
     */
    public static List<Mobile> getMobileRepository() {
        return mobileRepository;
    }

    /**
     * Removes a player and logs them out of the game
     *
     * @param player
     */
    public void deregister(Player player) {
        player.setLogoutRequired(true);
        registryQueue.add(player);
    }

    /**
     * Synchronises the registry queue with the real game repository and clears
     * the registry after
     */
    public void syncCycleRegistrys() {
        if (registryQueue.isEmpty()) {
            return;
        }
        LinkedList<Player> queue = (LinkedList) registryQueue.clone();
        for (Player player : queue) {
            if (player.isLogoutRequired()) {
                if(!player.isGenuineDisconnection()){
                    player.handleDisconnection();
                }
                getPlayerRepository().remove(player.getIndex());
                IndexManager.freeIndex(player.getIndex());
                player = null;
            } else {
                getPlayerRepository().put(player.getIndex(), player);
            }
        }
        registryQueue.clear();
    }

    /**
     * Stores a players login credentials into the registry queue awaiting the
     * end of cycle where it will be stored in the main list
     *
     * @param player
     */
    public void register(Player player) {
        if (getPlayerRepository().size() >= Constants.SERVER_MAX_PLAYERS) {
            return;
        }
        Integer pIndex = IndexManager.getIndex();
        player.setIndex(pIndex);
        registryQueue.add(player);
    }

    private static TaskExecuter task = new TaskExecuter();

    public static TaskExecuter getTask() {
        return task;
    }

    public static void submit(Task task) {
        getTask().schedule(task);
    }
}
