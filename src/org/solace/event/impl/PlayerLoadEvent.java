package org.solace.event.impl;

import org.solace.event.Event;
import org.solace.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class PlayerLoadEvent extends Event {
    
    public PlayerLoadEvent(){
        super(EventType.Standalone, 0 , false);
    }

    public static boolean loadGame(Player player){
        return true;
    }

    @Override
    public void init() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void stop() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
