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
           
    public static NIOSelector selector;
    
    public static MaintainedNetworkTask networkTask;

    private void init() throws Exception{
       selector = new NIOSelector();
       networkTask = new MaintainedNetworkTask(selector);
    }
    
    private void constructNetwork() throws IOException {
        NIOServer.bind(Constants.SERVER_LISTEN_PORT);
        TaskExecuter.get().schedule(networkTask);
        System.out.println("Constructing network backend...");
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Starting up Solace...");
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
        System.out.println("Solace listening on port: "+Constants.SERVER_LISTEN_PORT);
    }
}
