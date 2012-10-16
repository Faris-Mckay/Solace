package org.solace.world.game.entity.mobile.player;

import org.solace.event.InstantExecutionEvent;
import org.solace.event.impl.PlayerLoginEvent;
import org.solace.network.RSChannelContext;
import org.solace.network.packet.PacketSender;
import org.solace.world.map.Location;
import org.solace.world.game.entity.mobile.Mobile;
import org.solace.world.game.PrivateMessaging;

/**
 *
 * @author Faris
 */
public class Player extends Mobile {
    
    private RSChannelContext channelContext;
    private PacketSender packetSender = new PacketSender(this);
    private PrivateMessaging playerMessaging = new PrivateMessaging(this);
    private Location location;
    private String username;
    private String password;
    private long usernameAsLong;
    private int rights, index;
        
    public Player(String username, String password, RSChannelContext channelContext) {
        this.username = username;
        this.password = password;
        this.channelContext = channelContext;
        this.location = new Location(3222, 3222);
        this.rights = 2;
    }

    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public int getPlayerRights(){
        return rights;
    }
    
    public void setInteractingEntityIndex(int index) {
		interactingEntityIndex = index;
	}
    
    private int interactingEntityIndex;

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
        new InstantExecutionEvent(new PlayerLoginEvent(this)).executeEvent();
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
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the usernameAsLong
     */
    public long getUsernameAsLong() {
        return usernameAsLong;
    }

    /**
     * @return the playerMessaging
     */
    public PrivateMessaging getPrivateMessaging() {
        return playerMessaging;
    }
    
    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }




}
