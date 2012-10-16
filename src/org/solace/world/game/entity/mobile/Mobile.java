package org.solace.world.game.entity.mobile;

import org.solace.world.map.Location;
import org.solace.world.game.entity.Entity;

/**
 *
 * @author Faris
 */
public abstract class Mobile extends Entity {
    
    
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
 

}
