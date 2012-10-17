package org.solace.world.game.entity.mobile.player;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.solace.world.game.container.Equipment;
import org.solace.network.packet.PacketBuilder;
import org.solace.util.ProtocolUtils;
import org.solace.world.game.Game;

/**
 *
 * @author Faris
 */
public class PlayerUpdating {
    
    private Player master;
    private boolean teleporting = true;
    private boolean mapRegionChanging = true;
    private List<Player> localPlayers;
    
    public PlayerUpdating(Player master){
        this.master = master;
        localPlayers = new LinkedList();
    }
    
    public Player getMaster(){
        return master;
    }
    
    public List populateRegion(PacketBuilder out, PacketBuilder block){
        master.getLocation().getRegion().clearRegionContents();
        for(Player player : Game.playerRepository){
            if(player == null){
                continue;
            }
            if (master.getLocation().getRegion().playersWithinRegion().size() >= 255) {
                break;
            }
            if(master.getLocation().getRegion().playersWithinRegion().contains(player)){
                continue;
            }
            if (!player.getLocation().withinDistance(player.getLocation())) {
                continue;
            }
            master.getLocation().getRegion().playersWithinRegion().add(player);
            addPlayer(out, player);
            updateGivenPlayer(block, player, true);
            }
            return master.getLocation().getRegion().playersWithinRegion();
    }
    
    /**
     * Handles the player update protocol
     */
    public void updateMaster(){
        if(mapRegionChanging){
            master.getPacketSender().sendMapRegion(); 
        }
        PacketBuilder out = PacketBuilder.allocate(16384);
        PacketBuilder block = PacketBuilder.allocate(8192);
        out.createShortSizedFrame(81, master.channelContext().encryption());
        out.bitAccess();
        updateThisPlayerMovement(out);
        updateGivenPlayer(block, master, false);
        out.putBits(8, localPlayers.size());
        
        //Begin updating local players
        for (Iterator<Player> i = localPlayers.iterator(); i.hasNext();) {
            Player player = i.next();
            if(Game.playerRepository.contains(player) && !player.getServant().teleporting&& getMaster().getLocation().withinDistance(player.getLocation())){
                updatePlayerMovement(out, player);
                updateGivenPlayer(block, player, false);              
            } else {
                out.putBits(1, 1); // Update Requierd
		out.putBits(2, 3); // Remove Player
                i.remove();
            }
        }
        localPlayers = populateRegion(out, block);
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
    
    public void updateGivenPlayer(PacketBuilder out, Player player, boolean force){
        if (!player.getUpdateFlags().isUpdateRequired() && !force) {
            return;
        }
        int mask = 0x0;
        if (player.getUpdateFlags().isForceMovementUpdateRequired()) {
                mask |= 0x400;
        }
        if (player.getUpdateFlags().isGraphicsUpdateRequired()) {
                mask |= 0x100;
        }
        if (player.getUpdateFlags().isAnimationUpdateRequired()) {
                mask |= 0x8;
        }
        if (player.getUpdateFlags().isForceChatUpdate()) {
                mask |= 0x4;
        }
        if (player.getUpdateFlags().isChatUpdateRequired() && player != master) {
                mask |= 0x80;
        }
        if (player.getUpdateFlags().isEntityFaceUpdate()) {
                mask |= 0x1;
        }
        if (player.getUpdateFlags().isAppearanceUpdateRequired() || force) {
                mask |= 0x10;
        }
        if (player.getUpdateFlags().isFaceToDirection()) {
                mask |= 0x2;
        }
        if (player.getUpdateFlags().isHitUpdate()) {
                mask |= 0x20;
        }
        if (player.getUpdateFlags().isHitUpdate2()) {
                mask |= 0x200;
        }
	if (mask >= 0x100) {
            mask |= 0x40;
            out.putByte(mask & 0xFF);
            out.putByte(mask >> 8);
        } else {
            out.putByte(mask);
        }
        checkRequiredUpdates(out, player, force);
    }
    
    public void updateThisPlayerMovement(PacketBuilder out) {
        if (teleporting || mapRegionChanging) {
            out.putBits(1, 1); // Update Required
            out.putBits(2, 3); // Player Teleported
            out.putBits(2, master.getLocation().getH()); // current height
            out.putBits(1, teleporting); // if teleport discard walking
            out.putBits(1, master.getUpdateFlags().isUpdateRequired()); // update												// required
            out.putBits(7, master.getLocation().getRegion().localY()); //these might be wrong print them out
            out.putBits(7, master.getLocation().getRegion().localX());
        } else {
            if (master.getUpdateFlags().isUpdateRequired()) {
                out.putBits(1, 1); // update required
                out.putBits(2, 0); // we didn't move
            } else {
                out.putBits(1, 0); // Nothing changed
            }
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
    
    public void addPlayer(PacketBuilder out, Player otherPlayer) {
        out.putBits(11, otherPlayer.getIndex()); // Writing player index.
        out.putBits(1, 1); // Update required.
        out.putBits(1, 1); // Discard walking.
        int yPos = otherPlayer.getLocation().getY() - master.getLocation().getY();
        int xPos = otherPlayer.getLocation().getX() - master.getLocation().getX();
        out.putBits(5, yPos); // The relative coordinates.
        out.putBits(5, xPos); // The relative coordinates.
    }

    private void checkRequiredUpdates(PacketBuilder out, Player player, boolean force) {
        if (player.getUpdateFlags().isAppearanceUpdateRequired() || force) {
                updatePlayerAppearance(out, player);
        }
    }

    public void updatePlayerAppearance(PacketBuilder out, Player player) {
        PacketBuilder props = PacketBuilder.allocate(128);
        props.putByte(player.getAuthentication().playerGender());
        props.putByte(player.getPlayerHeadIcon());
        props.putByte(-1);// TODO: Player Skull
        props.putByte(0); // Player Hat
        props.putByte(0); // Player Cape
        props.putByte(0); // Player Amulet
        props.putByte(0); // Player Weapon
        props.putShort(0x100 + player.getAuthentication().playerTorso()); // Body
        props.putByte(0); // Player Shield
        props.putShort(0x100 + player.getAuthentication().playerArms());  //Arms
        props.putShort(0x100 + player.getAuthentication().playerLegs()); // Legs
        props.putShort(0x100 + player.getAuthentication().playerHead()); // Head
        props.putShort(0x100 + player.getAuthentication().playerHands()); // Hands
        props.putShort(0x100 + player.getAuthentication().playerFeet()); // Feet
        if (player.getAuthentication().getPlayerAppearanceIndex(0) == 0) {
                props.putShort(0x100 + player.getAuthentication().playerJaw());
        } else {
                props.putByte(0);
        }
        props.putByte(player.getAuthentication().playerHairColour());
        props.putByte(player.getAuthentication().playerTorsoColour());
        props.putByte(player.getAuthentication().playerLegColour());
        props.putByte(player.getAuthentication().playerFeetColour());
        props.putByte(player.getAuthentication().playerSkinColour());
        props.putShort(0x328); // TODO: standAnimIndex
        props.putShort(0x337); // TODO: standTurnAnimIndex
        props.putShort(0x333); // TODO: walkAnimIndex
        props.putShort(0x334); // TODO: turn180AnimIndex
        props.putShort(0x335); // TODO: turn90CWAnimIndex
        props.putShort(0x336); // TODO: turn90CCWAnimIndex
        props.putShort(0x338); // TODO: runAnimIndex
        /**
         * Sends player name as long
         */
        props.putLong(ProtocolUtils.getLongString(player.getAuthentication().getUsername()));
        props.putByte(3); // send combat level
        props.putShort(0); //games room title crap
        out.putByteC(props.buffer().position());
        out.put(props.buffer());
    }
        
}