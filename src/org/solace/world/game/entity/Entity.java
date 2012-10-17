package org.solace.world.game.entity;

import org.solace.world.map.Location;

/**
 *
 * @author Faris
 */
public abstract class Entity {
    
    public Entity(Location location){
        this.location = location;
    }
    
    public Location location;
    
    public Location getLocation(){
        return location;
    }
    
    
}
