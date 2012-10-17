package org.solace.event.impl;

import org.solace.event.Event;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class PlayerLoginEvent extends Event {
    
    public PlayerLoginEvent(Player player){
        super(EventType.Standalone);
        this.player = player;
    }
    
    Player player;

    @Override
    public void execute() {
        System.out.println("login request recieved from player:  "+player.getAuthentication().getUsername());
    }


}
