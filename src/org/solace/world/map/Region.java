package org.solace.world.map;

import java.util.LinkedList;
import java.util.List;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class Region {
    
    public Region(Location location){
        this.location = location;
    }
    
    private Location location;
    private List<Player> playersInRegion = new LinkedList<Player>();
    
    public List playersWithinRegion(){
        return playersInRegion;
    }
    
    public void addPlayerToRegion(Player player){
        playersInRegion.add(player);
    }
    
    public void clearRegionContents(){
        playersInRegion.clear();
    }
    
    private Location getLocation(){
        return location;
    }
    
    /**
     * Gets the region coordinate of this location.
     * @return the region x coordinate
     */
    public int regionX() {
        return (getLocation().x >> 3) - 6;
    }

    /**
     * Gets the region coordinate of this location.
     * @return the region y coordinate
     */
    public int regionY() {
        return (getLocation().y >> 3) - 6;
    }

    /**
     * Gets the local coordinate of this location.
     * @return the local x coordinate
     */
    public int localX() {
        return localX(getLocation());
    }

    /**
     * Gets the local coordinate of this location.
     * @return the local y coordinate
     */
    public int localY() {
        return localY(getLocation());
    }

    /**
     * Gets the relative local coordinate to another region.
     * @return the local x coordinate
     */
    public int localX(Location location) {
        return location.x - 8 * location.getRegion().regionX();
    }

    /**
     * Gets the relative local coordinate to another region.
     * @return the local y coordinate
     */
    public int localY(Location location) {
        return location.y - 8 * location.getRegion().regionY();
    }

}
