package org.solace.world.game.entity.mobile.player;

import org.solace.network.packet.PacketBuilder;
import org.solace.world.game.Game;

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
    
    public void populateRegion(PacketBuilder out, PacketBuilder block){
        master.getLocation().getRegion().clearRegionContents();
        for(Player player : Game.playerRepository){
            if(player == null){
                return;
            }
            if (master.getLocation().getRegion().playersWithinRegion().size() >= 255) {
                return;
            }
            if(master.getLocation().getRegion().playersWithinRegion().contains(player)){
                return;
            }
            if (!player.getLocation().withinDistance(player.getLocation())) {
                return;
            }
            master.getLocation().getRegion().playersWithinRegion().add(player);
        }
    }
    
    /**
     * Handles the player update protocol
     */
    public void updateMaster(){
        master.getPacketSender().sendMapRegion();
        PacketBuilder out = PacketBuilder.allocate(16384);
        PacketBuilder block = PacketBuilder.allocate(8192);
        out.createShortSizedFrame(81, master.channelContext().encryption());
        out.bitAccess();
        updateGivenMaster(out, master);
        populateRegion(out, block);
        if (block.buffer().position() > 0) {
            out.putBits(11, 2047);
            out.byteAccess();
            out.put(block.buffer());
        } else {
            out.byteAccess();
        }
        out.finishShortSizedFrame();
        out.sendTo(master.channelContext().channel());
        master.getLocation().getRegion().clearRegionContents();
    }
    
    public void updateGivenMaster(PacketBuilder out, Player player){
        int mask = 0x0;
        
        if (mask >= 0x100) {
            mask |= 0x40;
            out.putByte(mask & 0xFF);
            out.putByte(mask >> 8);
        } else {
            out.putByte(mask);
        }
    }
        
}
