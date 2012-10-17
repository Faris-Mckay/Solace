package org.solace.world.game.entity.mobile.npc;

import org.solace.world.game.entity.mobile.Mobile;
import org.solace.world.map.Location;

/**
 *
 * @author Faris
 */
public class NPC extends Mobile {
    
    public NPC(){
        super(new Location(3222, 3222));
    }

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void kill(){
        
    }


}
