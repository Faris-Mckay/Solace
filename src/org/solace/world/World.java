package org.solace.world;

import java.util.LinkedList;
import java.util.List;
import org.solace.util.Constants;
import org.solace.world.game.entity.mobile.Mobile;
import org.solace.world.game.entity.mobile.player.Player;
import org.solace.world.game.entity.mobile.player.PlayerAdvocate;

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
        if(PlayerAdvocate.playerList.size() >= Constants.SERVER_MAX_PLAYERS){
            return;
        }
        PlayerAdvocate.playerList.add(player);
        PlayerAdvocate.playerList.add(player);
        player.setIndex(PlayerAdvocate.playerList.size());
    }
    
    public void deregister(Player givenPlayer){
        for(Player player : PlayerAdvocate.playerList){
            if(player == givenPlayer){
                PlayerAdvocate.playerList.remove(givenPlayer);
            }
        }
    }

}
