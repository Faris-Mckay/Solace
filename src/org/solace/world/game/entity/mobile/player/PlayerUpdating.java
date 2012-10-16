package org.solace.world.game.entity.mobile.player;

import java.util.List;
import org.solace.network.packet.PacketBuilder;
import org.solace.world.game.Game;

/**
 *
 * @author Faris
 */
public class PlayerUpdating {
    
    private Player master;
    private boolean teleporting = false;
    
    public PlayerUpdating(Player master){
        this.master = master;
    }
    
    public Player getMaster(){
        return master;
    }
    
    public List populateRegion(PacketBuilder out, PacketBuilder block){
        master.getLocation().getRegion().clearRegionContents();
        for(Player player : Game.playerRepository){
            if(player == null){
                break;
            }
            if (master.getLocation().getRegion().playersWithinRegion().size() >= 255) {
                break;
            }
            if(master.getLocation().getRegion().playersWithinRegion().contains(player)){
                break;
            }
            if (!player.getLocation().withinDistance(player.getLocation())) {
                break;
            }
            if(player != master){
                master.getLocation().getRegion().playersWithinRegion().add(player);
            }
        }
        return master.getLocation().getRegion().playersWithinRegion();
    }
    
    /**
     * Handles the player update protocol
     */
    public void updateMaster(){
        
        // Setup updating and update this player
        master.getPacketSender().sendMapRegion();
        PacketBuilder out = PacketBuilder.allocate(16384);
        PacketBuilder block = PacketBuilder.allocate(8192);
        out.createShortSizedFrame(81, master.channelContext().encryption());
        out.bitAccess();
        updatePlayerMovement(out, master);
        updateGivenPlayer(out, master);
        List<Player> localPlayers = populateRegion(out, block);
        out.putBits(8, localPlayers.size());
        
        //Begin updating local players
        for(Player player : localPlayers){
            if(Game.playerRepository.contains(player) && !player.getServant().teleporting && player.getUpdateFlags().isUpdateRequired()){
                updatePlayerMovement(out, player);
                updateGivenPlayer(out, player);              
            } else {
                out.putBits(1, 1); // Update Requierd
		out.putBits(2, 3); // Remove Player
            }
        }
        
        if (block.buffer().position() > 0) {
            out.putBits(11, 2047);
            out.byteAccess();
            out.put(block.buffer());
        } else {
            out.byteAccess();
        }
        out.finishShortSizedFrame();
        out.sendTo(master.channelContext().channel());
    }
    
    public void updateGivenPlayer(PacketBuilder out, Player player){
        int mask = 0x0;
        mask |= 0x100;
	if (mask >= 0x100) {
            mask |= 0x40;
            out.putByte(mask & 0xFF);
            out.putByte(mask >> 8);
        } else {
            out.putByte(mask);
        }
    }

    private void updatePlayerMovement(PacketBuilder out, Player player) {
        if(player.getServant().teleporting){
            out.putBits(1, 1); // Update Required
            out.putBits(2, 3); // Player Teleported
            out.putBits(2, player.getLocation().getH()); // current height
            out.putBits(1, teleporting); // if teleport discard walking
            out.putBits(1, player.getUpdateFlags().isUpdateRequired()); // update                                                                                                         // required
            out.putBits(7, player.getLocation().getRegion().localY());
            out.putBits(7, player.getLocation().getRegion().localX());
        } else{
            out.putBits(1, 1); // update required
            out.putBits(2, 0); // we didn't move
        }
    }
        
}
