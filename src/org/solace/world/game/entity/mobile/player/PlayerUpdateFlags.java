package org.solace.world.game.entity.mobile.player;

import org.solace.world.game.Graphic;
import org.solace.world.map.Location;

/**
 *
 * @author Faris
 */
public class PlayerUpdateFlags {
    
    private boolean isUpdateRequired;
    private boolean chatUpdateRequired;
    private boolean isForceChatUpdate;
    private boolean appearanceUpdateRequired;
    private String forceChatMessage;
    private boolean graphicsUpdateRequired;
    private int graphicsId;
    private int graphicsDelay;
    private boolean animationUpdateRequired;
    private int animationId;
    private int animationDelay;
    private boolean entityFaceUpdate;
    private int entityFaceIndex = -1;
    private boolean faceToDirection;
    private Location face;
    private boolean hitUpdate;
    private boolean hitUpdate2;
    private boolean forceMovementUpdateRequired;
    private int damage;
    private int damage2;
    private int hitType;
    private int hitType2;
    private int startX, startY, endX, endY, speed1, speed2, direction;
    private boolean mapRegionChanging;
    private boolean teleporting;

    /**
     * Sends a forced movement mask
     * 
     * @param player
     * @param x
     * @param y
     * @param speed1
     * @param speed2
     * @param direction
     */
    public void sendForceMovement(Player player, final int x, final int y,final int speed1, final int speed2, final int direction) {
            this.startX = player.getLocation().getRegion().localX();
            this.startY = player.getLocation().getRegion().localY();
            this.endX = player.getLocation().getRegion().localX() + x;
            this.endY = player.getLocation().getRegion().localY() + y;
            this.speed1 = speed1;
            this.speed2 = speed2;
            this.direction = direction;
            forceMovementUpdateRequired = true;
            isUpdateRequired = true;
    }

    public void sendForceMovementDoubleSwing(Player player, final int x,final int y, final int startY, final int speed1, final int speed2,final int direction) {
            this.startX = player.getLocation().getRegion().localX();
            this.startY = player.getLocation().getRegion().localY();
            this.endX = player.getLocation().getRegion().localX() + x;
            this.endY = player.getLocation().getRegion().localY() + y;
            this.speed1 = speed1;
            this.speed2 = speed2;
            this.direction = direction;
            forceMovementUpdateRequired = true;
            isUpdateRequired = true;
    }

    /**
     * resets the forced movement
     */
    public void resetForceMovement() {
            startX = startY = endX = endY = speed1 = speed2 = direction = 0;
    }

    /**
     * sends a normal graphic
     * 
     * @param graphicsId
     */
    public void sendGraphic(int graphicsId) {
            this.graphicsId = graphicsId;
            this.graphicsDelay = 0;
            graphicsUpdateRequired = true;
            isUpdateRequired = true;
    }

    /**
     * sends a normal graphic with a delay
     * 
     * @param graphicsId
     * @param graphicsDelay
     */
    public void sendGraphic(int graphicsId, int graphicsDelay) {
            this.graphicsId = graphicsId;
            this.graphicsDelay = graphicsDelay;
            graphicsUpdateRequired = true;
            isUpdateRequired = true;
    }

    /**
     * send sa high graphic
     * 
     * @param graphicsId
     */
    public void sendHighGraphic(int graphicsId) {
            sendHighGraphic(graphicsId, 0); //common sense son
    }

    /**
     * sends a high graphic with a delay
     * 
     * @param graphicsId
     * @param graphicsDelay
     */
    public void sendHighGraphic(int graphicsId, int graphicsDelay) {
            this.graphicsId = graphicsId;
            this.graphicsDelay = 100 << 16 + graphicsDelay;
            graphicsUpdateRequired = true;
            isUpdateRequired = true;
    }

    /**
     * sends an animation
     * 
     * @param animationId
     */
    public void sendAnimation(int animationId) {
            sendAnimation(animationId, 0);
    }

    /**
     * sends an animation with a dleay
     * 
     * @param animationId
     * @param animationDelay
     */
    public void sendAnimation(int animationId, int animationDelay) {
            this.animationId = animationId;
            this.animationDelay = animationDelay;
            animationUpdateRequired = true;
            isUpdateRequired = true;
    }
    /**
     * sets the face update to face an entity
     * 
     * @param entityFaceIndex
     */
    public void faceEntity(int entityFaceIndex) {
            this.entityFaceIndex = entityFaceIndex;
            entityFaceUpdate = true;
            isUpdateRequired = true;
    }

    /**
     * sets the face update ot face a direction
     * 
     * @param face
     */
    public void sendFaceToDirection(Location face) {
            this.face = face;
            faceToDirection = true;
            isUpdateRequired = true;
    }

    /**
     * 
     * @param damage
     * @param hitType
     */
    public void sendHit(int damage, int hitType) {
            this.damage = damage;
            this.hitType = hitType;
            hitUpdate = true;
            isUpdateRequired = true;
    }

    /**
     * Sends a forced message string
     * 
     * @param forceChatMessage
     */
    public void sendForceMessage(String forceChatMessage) {
            this.forceChatMessage = forceChatMessage;
            isForceChatUpdate = true;
            isUpdateRequired = true;
    }

    /**
     * Resets the entities update flags
     */
    public void reset() {
            isForceChatUpdate = false;
            chatUpdateRequired = false;
            graphicsUpdateRequired = false;
            animationUpdateRequired = false;
            entityFaceUpdate = false;
            faceToDirection = false;
            hitUpdate = false;
            hitUpdate2 = false;
            mapRegionChanging = false;
            forceMovementUpdateRequired = false;
            teleporting = false;
    }

    public void setUpdateRequired(boolean isUpdateRequired) {
            this.isUpdateRequired = isUpdateRequired;
    }

    public boolean isUpdateRequired() {
            return isUpdateRequired;
    }

    public void setChatUpdateRequired(boolean chatUpdateRequired) {
            this.chatUpdateRequired = chatUpdateRequired;
    }

    public boolean isChatUpdateRequired() {
            return chatUpdateRequired;
    }

    public void setForceChatUpdate(boolean isForceChatUpdate) {
            this.isForceChatUpdate = isForceChatUpdate;
    }

    public boolean isForceChatUpdate() {
            return isForceChatUpdate;
    }

    public void setForceChatMessage(String forceChatMessage) {
            this.forceChatMessage = forceChatMessage;
            setForceChatUpdate(true);
            isUpdateRequired = true;
    }

    public String getForceChatMessage() {
            return forceChatMessage;
    }

    public void setGraphicsUpdateRequired(boolean graphicsUpdateRequired) {
            this.graphicsUpdateRequired = graphicsUpdateRequired;
    }

    public boolean isGraphicsUpdateRequired() {
            return graphicsUpdateRequired;
    }

    public void setGraphicsId(int graphicsId) {
            this.graphicsId = graphicsId;
    }

    public int getGraphicsId() {
            return graphicsId;
    }

    public void setGraphicsDelay(int graphicsDelay) {
            this.graphicsDelay = graphicsDelay;
    }

    public int getGraphicsDelay() {
            return graphicsDelay;
    }

    public void setAnimationUpdateRequired(boolean animationUpdateRequired) {
            this.animationUpdateRequired = animationUpdateRequired;
    }

    public boolean isAnimationUpdateRequired() {
            return animationUpdateRequired;
    }

    public int getAnimationId() {
            return animationId;
    }

    public void setAnimationDelay(int animationDelay) {
            this.animationDelay = animationDelay;
    }

    public int getAnimationDelay() {
            return animationDelay;
    }

    public void setEntityFaceUpdate(boolean entityFaceUpdate) {
            this.entityFaceUpdate = entityFaceUpdate;
    }

    public boolean isEntityFaceUpdate() {
            return entityFaceUpdate;
    }

    public void setEntityFaceIndex(int entityFaceIndex) {
            this.entityFaceIndex = entityFaceIndex;
    }

    public int getEntityFaceIndex() {
            return entityFaceIndex;
    }

    public void setFaceToDirection(boolean faceToDirection) {
            this.faceToDirection = faceToDirection;
    }

    public boolean isFaceToDirection() {
            return faceToDirection;
    }

    public void setFace(Location face) {
            this.face = face;
    }

    public Location getFace() {
            return face;
    }

    public void setHitUpdate(boolean hitUpdate) {
            this.hitUpdate = hitUpdate;
    }

    public boolean isHitUpdate() {
            return hitUpdate;
    }

    public void setHitUpdate2(boolean hitUpdate2) {
            this.hitUpdate2 = hitUpdate2;
    }

    public boolean isHitUpdate2() {
            return hitUpdate2;
    }

    public void setDamage(int damage) {
            this.damage = damage;
    }

    public int getDamage() {
            return damage;
    }

    public void setDamage2(int damage2) {
            this.damage2 = damage2;
    }

    public int getDamage2() {
            return damage2;
    }

    public void setHitType(int hitType) {
            this.hitType = hitType;
    }

    public int getHitType() {
            return hitType;
    }

    public void setHitType2(int hitType2) {
            this.hitType2 = hitType2;
    }

    public int getHitType2() {
            return hitType2;
    }

    public boolean isForceMovementUpdateRequired() {
            return forceMovementUpdateRequired;
    }

    public void setForceMovementUpdateRequired(
                    boolean forceMovementUpdateRequired) {
            this.forceMovementUpdateRequired = forceMovementUpdateRequired;
    }

    public int getStartX() {
            return startX;
    }

    public int getStartY() {
            return startY;
    }

    public int getEndX() {
            return endX;
    }

    public int getEndY() {
            return endY;
    }

    public int getSpeed1() {
            return speed1;
    }

    public int getSpeed2() {
            return speed2;
    }

    public int getDirection() {
            return direction;
    }

    public boolean isAppearanceUpdateRequired() {
            return appearanceUpdateRequired;
    }

    public void setAppearanceUpdateRequired(boolean b) {
            appearanceUpdateRequired = b;
    }

    public boolean isMapRegionChanging() {
            return mapRegionChanging;
    }

    public void setMapRegionChanging(boolean changing) {
            this.mapRegionChanging = changing;
    }

    public boolean isTeleporting() {
            return teleporting;
    }

    public PlayerUpdateFlags setTeleporting(boolean teleporting) {
            this.teleporting = teleporting;
            return this;
    }

}
