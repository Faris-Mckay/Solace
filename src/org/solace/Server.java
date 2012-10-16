package org.solace;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.solace.event.update.ScheduledNPCUpdate;
import org.solace.event.update.ScheduledPlayerUpdate;
import org.solace.network.NIOSelector;
import org.solace.network.NIOServer;
import org.solace.task.TaskExecuter;
import org.solace.task.impl.MaintainedNetworkTask;
import org.solace.util.Constants;


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
        System.out.println("Task update handlers intitializing...");
        TaskExecuter.get().schedule(new ScheduledPlayerUpdate());
        TaskExecuter.get().schedule(new ScheduledNPCUpdate());
        System.out.println("Solace listening on port: "+Constants.SERVER_LISTEN_PORT);
    }
}
