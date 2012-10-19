package org.solace;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.solace.event.impl.EngineCleanEvent;
import org.solace.network.NIOSelector;
import org.solace.network.NIOServer;
import org.solace.task.TaskExecuter;
import org.solace.task.impl.MaintainedNetworkTask;
import org.solace.util.Constants;
import org.solace.world.game.Game;


/**
 *
 * @author Faris
 */
public class Server {
    
    public static Logger logger = Logger.getLogger(Server.class.getName());
           
    public static NIOSelector selector;
    
    public static MaintainedNetworkTask networkTask;

    private void init() throws Exception{
       selector = new NIOSelector();
    }
    
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
    
    private void serverStartUp(){
        try {
            init();
            constructNetwork();
            start();
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void start() {
        EngineCleanEvent.init();
        Game.beginUpdatingMobiles();
        logger.info("Solace listening on port: "+Constants.SERVER_LISTEN_PORT);
    }
}
