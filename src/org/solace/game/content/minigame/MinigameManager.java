package org.solace.game.content.minigame;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Faris
 */
public class MinigameManager {
    
    private static List<Minigame> minigames = new ArrayList<Minigame>();
    
    public static void submitMinigame(Minigame game){
        minigames.add(game);
    }
    
    public static void cycle(){
        
    }

}
