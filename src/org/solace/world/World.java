package org.solace.world;

import java.util.LinkedList;
import java.util.List;
import org.solace.util.Constants;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class World {
    
    public static final List<Player> worldRepository = new LinkedList<Player>();
    
    public static World getSingleton(){
        return world;
    }
    
    final static World world = new World();
    
    public void register(Player player){
        if(worldRepository.size() >= Constants.SERVER_MAX_PLAYERS){
            return;
        }
        worldRepository.add(player);
        player.setIndex(worldRepository.size());
    }
    
    public void deregister(Player givenPlayer){
        for(Player player : worldRepository){
            if(player == givenPlayer){
                worldRepository.remove(givenPlayer);
            }
        }
    }

}
