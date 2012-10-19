package org.solace.world.game.entity.mobile;

import org.solace.world.map.Location;
import org.solace.world.game.entity.Entity;
import org.solace.world.map.Region;

/**
 *
 * @author Faris
 */
public abstract class Mobile extends Entity {
    
    private WelfareStatus welfareStatus;
    private MovementStatus moveStatus;
    private Location targettedLocation;
    public Region cachedRegion;
    private Entity interactingEntity;
    private MobilityManager mobilityQueue = new MobilityManager(this);
    
    public Mobile(Location location){
        super(location);
    }
    
    private int index, maximumWalkingDistance, interactingEntityIndex;

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
    
    /**
     * Parses in a location and sets it to the Entity's location variable
     * @param location 
     */
    public void setLocation(Location location){
        super.location = location;
    }
    
    /**
     * Parses in a region, sets the entity's variable 
     * @param currentRegion
     * @return the player with the updated region
     */
    public Entity region(Region currentRegion) {
        this.cachedRegion = currentRegion;
        return this;
    }
    
    /**
     * returns cached region for player region checking
     * @return 
     */
    public Region cachedRegion(){
        return this.cachedRegion;
    }

    /**
     * @return the status
     */
    public WelfareStatus getStatus() {
        return welfareStatus;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(WelfareStatus status) {
        this.welfareStatus = status;
    }

    /**
     * @return the moveStatus
     */
    public MovementStatus getMoveStatus() {
        return moveStatus;
    }

    /**
     * @param moveStatus the moveStatus to set
     */
    public void setMoveStatus(MovementStatus moveStatus) {
        this.moveStatus = moveStatus;
    }

    /**
     * @return the targettedLocation
     */
    public Location getTargettedLocation() {
        return targettedLocation;
    }

    /**
     * @param targettedLocation the targettedLocation to set
     */
    public Mobile targettedLocation(Location targettedLocation) {
        this.targettedLocation = targettedLocation;
        return this;
    }
    
    /**
     * Returns the mobility/movement handler for the entity
     * @return 
     */
    public MobilityManager getMobilityManager() {
        return mobilityQueue;
    }
    
    /**
     * Returns current entity interacted with
     * @return 
     */
    public Entity getInteractingEntity() {
        return interactingEntity;
    }
    
    /**
     * Parses in a entity and sets as the current entity interaction
     * @param interactingEntity 
     */
    public void setInteractingEntity(Entity interactingEntity) {
        this.interactingEntity = interactingEntity;
    }

    /**
     * @return the maximumWalkingDistance
     */
    public int getMaximumWalkingDistance() {
        return maximumWalkingDistance;
    }

    /**
     * @param maximumWalkingDistance the maximumWalkingDistance to set
     */
    public void setMaximumWalkingDistance(int maximumWalkingDistance) {
        this.maximumWalkingDistance = maximumWalkingDistance;
    }

    /**
     * @return the interactingEntityIndex
     */
    public int getInteractingEntityIndex() {
        return interactingEntityIndex;
    }

    /**
     * @param interactingEntityIndex the interactingEntityIndex to set
     */
    public void setInteractingEntityIndex(int interactingEntityIndex) {
        this.interactingEntityIndex = interactingEntityIndex;
    }
    
    public enum WelfareStatus{
        /**
         * The Mobile Entity is 
         * current alive
         */
        ALIVE,
        
        /**
         * The Mobile Entity is
         * currently dead
         */
        DEAD,
        
        /**
         * The Mobile Entity is
         * currently in the process of dieing
         */
        DIEING;
    }
    
    public enum MovementStatus{
        /**
         * The Mobile Entity is
         * standing still
         */
        STATIONARY,
        
        /**
         * The Mobile Entity is
         * currently moving
         */
        MOBILE,
    }
 
 

}
