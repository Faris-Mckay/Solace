package org.solace.world.map;

import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class Boundary {
    
    /**
     * The given parameters for the boundary
     * these vary per instance
     */
    private int lowestX, 
                lowestY, 
                highestX, 
                highestY;
    
    /**
     * Constructor set to assign the variables in a new instance of this class
     * @param lowest
     * @param highest 
     */
    public Boundary(Location lowest, Location highest){
        this.lowestX = lowest.getX();
        this.lowestY = lowest.getY();
        this.highestX = highest.getX();
        this.highestY = highest.getY();
    }
    
    /**
     * Standard method for usage of this class
     * @param c
     * @return 
     */
    public boolean withinBoundry(Player c){
        return (c.getLocation().getX() >= lowestX) & (c.getLocation().getX() <= highestX) & (c.getLocation().getY() >= lowestY) & (c.getLocation().getY() <= highestY);
    }

}