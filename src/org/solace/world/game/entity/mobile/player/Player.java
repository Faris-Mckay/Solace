package org.solace.world.game.entity.mobile.player;

import org.solace.world.game.content.PrivateMessaging;
import org.solace.event.impl.PlayerLoginEvent;
import org.solace.network.RSChannelContext;
import org.solace.network.packet.PacketDispatcher;
import org.solace.task.Task;
import org.solace.world.game.container.Equipment;
import org.solace.world.game.entity.mobile.Mobile;
import org.solace.world.map.Location;

/**
 *
 * @author Faris
 */
public class Player extends Mobile {
    
    private RSChannelContext channelContext;
    private PlayerAuthentication authenticator;
    private PacketDispatcher packetDispatcher = new PacketDispatcher(this);
    private PrivateMessaging playerMessaging = new PrivateMessaging(this);
    private PlayerUpdateFlags updateFlags = new PlayerUpdateFlags();
    private PlayerUpdating updating = new PlayerUpdating(this);
    private Equipment equipment = new Equipment();
        
    public Player(String username, String password, RSChannelContext channelContext) {
        super(new Location(3222,3222));
        this. authenticator = new PlayerAuthentication(username,password);
        this.channelContext = channelContext;
        setDefaultAppearance();
        getUpdateFlags().setUpdateRequired(true);
        getUpdateFlags().setAppearanceUpdateRequired(true);
    }
    
    /**
     * Can be used for content implementations
     * but only when absolutely necessary, this is NOT PI
     */
    @Override
    public void update() {
        getMobilityManager().processMovement();
    }
    
    private Task walkToAction;
    private int playerHeadIcon = -1;
    
    public Player channelContext(RSChannelContext channelContext) {
        this.channelContext = channelContext;
        return this;
    }

    public RSChannelContext channelContext() {
        return channelContext;
    }


    public PacketDispatcher getPacketDispatcher() {
        return packetDispatcher;
    }

    public void handleLoginData() {
       new PlayerLoginEvent(this).execute();
    }

    /**
     * @return the playerMessaging
     */
    public PrivateMessaging getPrivateMessaging() {
        return playerMessaging;
    }

    /**
     * @return the playerCredentials
     */
    public PlayerAuthentication getAuthentication() {
        return authenticator;
    }

    /**
     * @return the assistant
     */
    public PlayerUpdating getUpdater() {
        return updating;
    }

    /**
     * @return the updateFlags
     */
    public PlayerUpdateFlags getUpdateFlags() {
        return updateFlags;
    }

    /**
     * @return the playerHeadIcon
     */
    public int getPlayerHeadIcon() {
        return playerHeadIcon;
    }

    /**
     * @return the equipment
     */
    public Equipment getEquipment() {
        return equipment;
    }
    
    public Player walkToAction(Task walkToAction) {
            this.walkToAction = walkToAction;
            return this;
    }

    /**
     * Gets the walk to action task.
     * 
     * @return the walk to action task
     */
    public Task walkToAction() {
            return walkToAction;
    }
    

    private void setDefaultAppearance() {
        /**
         * Gender
         */
        getAuthentication().appearanceIndex[0] = 0;

        /**
         * Clothing
         */
        getAuthentication().appearanceIndex[1] = 0;
        getAuthentication().appearanceIndex[2] = 18;
        getAuthentication().appearanceIndex[3] = 26;
        getAuthentication().appearanceIndex[4] = 35;
        getAuthentication().appearanceIndex[5] = 36;
        getAuthentication().appearanceIndex[6] = 42;
        getAuthentication().appearanceIndex[7] = 10;
        /**
         * Colors
         */
        getAuthentication().appearanceIndex[8] = 7;
        getAuthentication().appearanceIndex[9] = 8;
        getAuthentication().appearanceIndex[10] = 9;
        getAuthentication().appearanceIndex[11] = 5;
        getAuthentication().appearanceIndex[12] = 0;

     }




}
