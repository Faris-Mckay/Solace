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