package org.solace.world.game.entity.mobile.player;

import org.solace.event.impl.PlayerLoginEvent;
import org.solace.network.RSChannelContext;
import org.solace.network.packet.PacketSender;
import org.solace.world.game.PrivateMessaging;
import org.solace.world.game.entity.mobile.Mobile;
import org.solace.world.map.Location;

/**
 *
 * @author Faris
 */
public class Player extends Mobile {
    
    private RSChannelContext channelContext;
    private PlayerAuthentication playerCredentials;
    private PacketSender packetSender = new PacketSender(this);
    private PrivateMessaging playerMessaging = new PrivateMessaging(this);
    private Location location;
        
    public Player(String username, String password, RSChannelContext channelContext) {
        this. playerCredentials = new PlayerAuthentication(username,password);
        this.channelContext = channelContext;
        this.location = new Location(3222, 3222);
    }

    public Player channelContext(RSChannelContext channelContext) {
            this.channelContext = channelContext;
            return this;
    }

    public RSChannelContext channelContext() {
            return channelContext;
    }


    public PacketSender getPacketSender() {
            return packetSender;
    }

    public void handleLoginData() {
       new PlayerLoginEvent(this).execute();
    }

    @Override
    public void update() {
        
    }

    @Override
    public Location getLocation() {
        return location;
    }
    
    public Player currentRegion(Location currentRegion) {
        this.location = currentRegion;
        return this;
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
    public PlayerAuthentication getPlayerCredentials() {
        return playerCredentials;
    }




}
