package org.solace.game.content.minigame;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.solace.util.Constants;

/**
 *
 * @author Faris
 */
public class MinigameExecutor extends Thread {
    
    public static List<Minigame> gamesToBegin = new LinkedList();
    public static List<Minigame> activeGames = new LinkedList();

    @Override
    public void run() {
        while(true){
            executeMinigames();
            try {
                Thread.sleep(Constants.SERVER_CYCLE_RATE);
            } catch (InterruptedException ex) {
                Logger.getLogger(MinigameExecutor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void executeMinigames() {
        if(activeGames.isEmpty()){
            return;
        }
        Iterator<Minigame> it = activeGames.iterator();
        while(it.hasNext()){
            Minigame game = it.next();
            if(game.gameShouldEnd()){
                activeGames.remove(game);
            }
            game.process();
        }
        if(gamesToBegin.isEmpty()){
            return;
        }
        for(Minigame game : gamesToBegin){
            activeGames.add(game);
        }
        gamesToBegin.clear();
    }
    
}