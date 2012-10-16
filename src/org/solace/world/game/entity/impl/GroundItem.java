package org.solace.world.game.entity.impl;

import org.solace.world.map.Location;
import org.solace.world.game.entity.Entity;

/**
 *
 * @author Faris
 */
public class GroundItem extends Entity {
    
    public GroundItem(Location location){
        super(location);
    }

    @Override
    public Location getLocation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
