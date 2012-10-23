package org.solace.task.impl;

import org.solace.Server;
import org.solace.event.impl.PlayerSaveEvent;
import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class ShutdownExecutionTask extends Thread {
    
    @Override
    public void run(){
        Server.logger.warning("Server unexpected shutdown, hook executed");
        for(Player player : Game.playerRepository.values()){
            if (player == null){
                continue;
            }
            new PlayerSaveEvent(player).execute();
        }
    }

}
