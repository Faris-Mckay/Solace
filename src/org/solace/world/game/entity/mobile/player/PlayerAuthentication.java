package org.solace.world.game.entity.mobile.player;

import org.solace.util.ProtocolUtils;

/**
 *
 * @author Faris
 */
public class PlayerAuthentication {
    
    public PlayerAuthentication(String username, String password){
        this.username = username;
        this.password = password;
        this.usernameAsLong = ProtocolUtils.nameToLong(username);
        this.playerRights = 0;
    }
    
    private String username;
    private String password;
    private long usernameAsLong;
    private int playerRights;
    
    public int getPlayerRights(){
        return playerRights;
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
     * @return the password
     */
    public String getPassword() {
        return password;
    }

}
