package org.solace.world.game.entity.mobile.npc;

import org.solace.world.map.Location;
import org.solace.world.game.entity.mobile.Mobile;

/**
 *
 * @author Faris
 */
public class NPC extends Mobile {
    
    Location location;

    @Override
    public void update() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public void kill(){
        
    }

    @Override
    public Location getLocation() {
        return location;
    }

}
