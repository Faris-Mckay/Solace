package org.solace.world.game.entity.mobile;

import org.solace.world.map.Location;
import org.solace.world.game.entity.Entity;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public abstract class Mobile extends Entity {
    
    public Mobile(){
        super(new Location(3222, 3222));
    }
    
    public abstract void update();
    
    private int index;

    /**
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param index the index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }
    
    public Mobile currentRegion(Location currentRegion) {
        location = currentRegion;
        return this;
    }
 

}
