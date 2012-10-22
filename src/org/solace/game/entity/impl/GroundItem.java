package org.solace.game.entity.impl;

import org.solace.game.map.Location;
import org.solace.game.entity.Entity;

/**
 *
 * @author Faris
 */
public class GroundItem extends Entity {
    
    public int itemID;
    public int privateTime;
    public String name;
    public int stackAmount;
    public int ownerIndex;
    
    public GroundItem(Location location, int itemID, int stackAmount, String name, int privateTime, int ownerIndex){
        super(location);
        this.itemID = itemID;
        this.privateTime = privateTime;
        this.stackAmount = stackAmount;
        this.name = name;
        this.ownerIndex = ownerIndex;
    }

    @Override
    public Location getLocation() {
        return super.getLocation();
    }

    @Override
    public void update() {
        if(privateTime > 0){
            privateTime--;
        }
    }

}
