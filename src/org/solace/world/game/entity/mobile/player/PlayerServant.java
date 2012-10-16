package org.solace.world.game.entity.mobile.player;

/**
 *
 * @author Faris
 */
public class PlayerServant {
    
    private Player master;
    
    public PlayerServant(Player master){
        this.master = master;
    }
    
    public Player getMaster(){
        return master;
    }
    
    /**
     * Handles the player update protocol
     */
    public void updateMaster(){

    }

}
