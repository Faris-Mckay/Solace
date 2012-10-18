package org.solace.world;

import java.util.LinkedList;
import java.util.List;

import org.solace.util.Constants;
import org.solace.util.IndexManager;
import org.solace.world.game.Game;
import org.solace.world.game.entity.mobile.Mobile;
import org.solace.world.game.entity.mobile.npc.NPC;
import org.solace.world.game.entity.mobile.player.Player;

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
        synchronized(player){
            if(Game.playerRepository.size() >= Constants.SERVER_MAX_PLAYERS){
                return;
            }
            Integer pIndex = IndexManager.getIndex();
            Game.playerRepository.put(pIndex, player);
            player.setIndex(pIndex);
        }
    }
    
    public void deregister(Player player){
        Game.playerRepository.remove(player.getIndex());
        IndexManager.freeIndex(player.getIndex());
    }

}
