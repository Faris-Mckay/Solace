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
        this.playerRights = PrivilegeRank.STANDARD;
    }
    
    private String username;
    private String password;
    private long usernameAsLong;
    private PrivilegeRank playerRights;
    
    public int getPlayerRights(){
        switch(playerRights){
            case MODERATOR:
                return 1;
            case ADMINISTRATOR:
                return 2;
            case OWNER:
                return 3;
            default:
                return 0;
        }
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
    
    public enum PrivilegeRank{
        /**
         * A standard (rights 0) account.
         */
        STANDARD,

        /**
         * A player moderator (rights 1) account.
         */
        MODERATOR,

        /**
         * An administrator (rights 2) account.
         */
        ADMINISTRATOR,
        
        /**
         * An owner (rights 3) account.
         */
        OWNER;
    }

}
