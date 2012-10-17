package org.solace.world.game.entity.mobile;

import org.solace.world.map.Location;
import org.solace.world.game.entity.Entity;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public abstract class Mobile extends Entity {
    
    private WelfareStatus welfareStatus;
    private MovementStatus moveStatus;
    private Location targettedLocation;
    public Location cachedRegion;
    private Entity interactingEntity;
    private MobilityManager mobilityQueue = new MobilityManager(this);
    
    public Mobile(Location location){
        super(location);
    }
    
    public abstract void update();
    
    private int index;
    private int maximumWalkingDistance;
    private int interactingEntityIndex;

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
    
    public void setLocation(Location location){
        super.location = location;
    }
    
    public Entity region(Location currentRegion) {
        this.cachedRegion = currentRegion;
        return this;
    }
    
    public Location cachedRegion(){
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
    
    public MobilityManager getMobilityManager() {
        return mobilityQueue;
    }

    public Entity getInteractingEntity() {
        return interactingEntity;
    }

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
        
        ALIVE,
        
        DEAD,
        
        DIEING;
    }
    
    public enum MovementStatus{
        STATIONARY,
        MOBILE,
    }
 

}
