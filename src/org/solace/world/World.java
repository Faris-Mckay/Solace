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
        if(Game.playerRepository.size() >= Constants.SERVER_MAX_PLAYERS){
            return;
        }
        Integer pIndex = IndexManager.getIndex();
        player.setIndex(pIndex);
        Game.registryQueue.add(player);
    }
    
    public void syncCycleRegistrys(){
        if (Game.registryQueue.size() == 0){
            return;
        }
        for(Player player : Game.registryQueue){
            if (player.isLogoutRequired()){
                Game.playerRepository.remove(player.getIndex()); 
                IndexManager.freeIndex(player.getIndex());
                player = null;
            } else {
                Game.playerRepository.put(player.getIndex(), player);               
            }
        }
        Game.registryQueue.clear();
    }
    
    public void deregister(Player player){
        player.setLogoutRequired(true);
        Game.registryQueue.add(player);
    }

}
