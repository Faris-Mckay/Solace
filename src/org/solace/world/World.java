package org.solace.world;

import java.util.LinkedList;
import java.util.List;
import org.solace.util.Constants;
import org.solace.world.game.Game;
import org.solace.world.game.entity.mobile.Mobile;
import org.solace.world.game.entity.mobile.player.Player;
import org.solace.world.game.entity.mobile.player.PlayerServant;

/**
 *
 * @author Faris
 */
public class World {
    
    public static final List<Mobile> mobileRepository = new LinkedList<Mobile>();
    
    public static World getSingleton(){
        return world;
    }
    
    final static World world = new World();
    
    public void register(Player player){
        if(Game.playerRepository.size() >= Constants.SERVER_MAX_PLAYERS){
            return;
        }
        Game.playerRepository.add(player);
        player.setIndex(Game.playerRepository.size());
    }
    
    public void deregister(Player givenPlayer){
        for(Player player : Game.playerRepository){
            if(player == givenPlayer){
                Game.playerRepository.remove(givenPlayer);
            }
        }
    }

}
