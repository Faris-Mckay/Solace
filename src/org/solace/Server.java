package org.solace;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import mint.event.EventManager;
import mint.event.UniversalEventManager;
import org.solace.event.EventSystemHandler;
import org.solace.event.listener.AttackListener;
import org.solace.event.listener.PlayerLoginListener;
import org.solace.event.listener.PlayerLogoutListener;
import org.solace.event.listener.PlayerSaveListener;
import org.solace.event.listener.ProcessCommandListener;
import org.solace.event.listener.SpecialAttackListener;
import org.solace.game.Game;
import org.solace.game.content.combat.specials.SpecialAttackManager;
import org.solace.game.entity.mobile.player.command.CommandHandler;
import org.solace.network.NIOSelector;
import org.solace.network.NIOServer;
import org.solace.task.TaskExecuter;
import org.solace.task.impl.MaintainedNetworkTask;
import org.solace.task.impl.ShutdownExecutionTask;
import org.solace.util.Constants;
import org.solace.util.XStreamUtil;

/**
 *
 * @author Faris
 */
public class Server {

    /**
     * The logger object for clean printing
     */
    public static Logger logger = Logger.getLogger(Server.class.getName());
    /**
     * Creates a new instance of the event system handler
     */
    private static EventSystemHandler service = new EventSystemHandler(600);
    /**
     * Creates a new instance of the event manager
     */
    private static EventManager eventManager = new UniversalEventManager();
    /**
     * NIOselector instance for networking
     */
    public static NIOSelector selector;
    /**
     * Handles the timed listening for new connections
     */
    public static MaintainedNetworkTask networkTask;

    /**
     * Pre-loads any required data for game
     *
     * @throws Exception
     */
    private void init() throws Exception {
        Runtime.getRuntime().addShutdownHook(new ShutdownExecutionTask());
        // networking
        selector = new NIOSelector();

        // logic loading
        XStreamUtil.loadAllXmlData();

        SpecialAttackManager.loadSpecials();

        /*
         * Loads all of the commands
         */
        CommandHandler.loadCommands();
    }

    /**
     * Starts the full NIO networking and connection listening
     *
     * @throws IOException
     */
    private void constructNetwork() throws IOException {
        NIOServer.bind(Constants.SERVER_LISTEN_PORT);
        networkTask = new MaintainedNetworkTask(selector);
        TaskExecuter.get().schedule(networkTask);
        logger.info("Constructing network backend...");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        logger.info("Starting up Solace...");
        new Server().serverStartUp();
    }

    /**
     * Registers all of the event listeners
     */
    public static void registerEventListeners() {
        eventManager.registerListener(new PlayerLoginListener());
        eventManager.registerListener(new PlayerSaveListener());
        eventManager.registerListener(new PlayerLogoutListener());
        eventManager.registerListener(new SpecialAttackListener());
        eventManager.registerListener(new AttackListener());
        eventManager.registerListener(new ProcessCommandListener());
    }

    /**
     * Sequentially starts up all required aspects of Solace
     */
    private void serverStartUp() {
        try {
            init();
            constructNetwork();
            start();
            registerEventListeners();
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Starts up the main parts of game logic
     *
     * @throws InterruptedException
     */
    private void start() throws InterruptedException {
        logger.info("Task update handlers intitializing...");
        /**
         * The main game engine tasks
         */
        Game.submitTasks();
        logger.info("Solace listening on port: " + Constants.SERVER_LISTEN_PORT);
    }

    public static EventSystemHandler getService() {
        return service;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }
}
