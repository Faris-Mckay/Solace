package org.solace.event.impl;

import org.solace.event.Event;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class PlayerLoginEvent extends Event {
    
    public PlayerLoginEvent(Player player){
        this.player = player;
    }
    
    Player player;


    @Override
    public void execute() {
        System.out.println("Connection recieved from "+player.getUsername());
    }


}
