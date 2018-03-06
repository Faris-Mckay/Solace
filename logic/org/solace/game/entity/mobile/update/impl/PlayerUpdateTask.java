/*
 * This file is part of Solace Framework.
 * Solace is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Solace is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Solace. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.solace.game.entity.mobile.update.impl;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.solace.game.Game;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.mobile.update.MobileUpdateTask;
import org.solace.game.item.ItemDefinition;
import org.solace.game.item.container.impl.Equipment;
import org.solace.network.util.Stream;
import org.solace.util.Constants;
import org.solace.util.ProtocolUtils;

/**
 *
 * @author Faris
 */
public class PlayerUpdateTask extends MobileUpdateTask {

    private Player master;
    private boolean teleporting = true;
    private boolean mapRegionChanging = true;
    private List<Player> localPlayers;
    public byte chatText[] = new byte[256];
    public int chatTextEffects = 0, chatTextColor = 0;

    private Stream out = new Stream(new byte[Constants.BUFFER_SIZE]);

    public PlayerUpdateTask(Player master) {
        this.master = master;
        localPlayers = new LinkedList<Player>();
    }

    public Player getMaster() {
        return master;
    }

    private void populateRegion(Stream out, Stream block) {
        for (Player player : Game.getPlayerRepository().values()) {
            if (getMaster().getUpdater().localPlayers.size() >= 255) {
                break;
            }
            if (player == getMaster()
                    || getMaster().getUpdater().localPlayers.contains(player)) {
                continue;
            }
            if (getMaster().getLocation().withinDistance(player.getLocation())) {
                getMaster().getUpdater().localPlayers.add(player);
                addPlayer(out, player);
                updateGivenPlayer(block, player, true);
            }
        }
    }

    /**
     * Handles the player update protocol
     */
    @Override
    public void synchronize() {
        if (mapRegionChanging) {
            master.getPacketDispatcher().sendMapRegion();
        }
        Stream block = new Stream(new byte[Constants.BUFFER_SIZE / 2]);
        getOut().createFrameVarSizeWord(81);
        getOut().initBitAccess();
        updateThisPlayerMovement(getOut());
        updateGivenPlayer(block, master, false);
        getOut().writeBits(8, localPlayers.size());

        for (Iterator<Player> i = localPlayers.iterator(); i.hasNext();) {
            Player player = i.next();
            if (Game.getPlayerRepository().values().contains(player)
                    && !player.getUpdater().teleporting
                    && player.getLocation().withinDistance(
                            getMaster().getLocation())) {
                updatePlayerMovement(getOut(), player);
                if (player.getUpdateFlags().isUpdateRequired()) {
                    updateGivenPlayer(block, player, false);
                }
            } else {
                i.remove();
                getOut().writeBits(1, 1); // Update Requierd
                getOut().writeBits(2, 3); // Remove Player
            }
        }
        populateRegion(getOut(), block);
        if (block.currentOffset > 0) {
            getOut().writeBits(11, 2047);
            getOut().finishBitAccess();
            getOut().writeBytes(block.buffer, block.currentOffset, 0);
        } else {
            getOut().finishBitAccess();
        }
        getOut().endFrameVarSizeWord();
    }

    public void updateGivenPlayer(Stream out, Player player,
            boolean force) {
        if (!player.getUpdateFlags().isUpdateRequired() && !force) {
            return;
        }
        if (player.hasCachedUpdateBlock() && player != getMaster() && !force) {
            return;
        }
        int mask = 0x0;
        if (player.getUpdateFlags().get(UpdateFlag.FORCE_MOVEMENT)) {
            mask |= UpdateFlag.FORCE_MOVEMENT.getMask();
        }
        if (player.getUpdateFlags().get(UpdateFlag.GRAPHICS)) {
            mask |= UpdateFlag.GRAPHICS.getMask();
        }
        if (player.getUpdateFlags().get(UpdateFlag.ANIMATION)) {
            mask |= UpdateFlag.ANIMATION.getMask();
        }
        if (player.getUpdateFlags().get(UpdateFlag.FORCED_CHAT)) {
            mask |= UpdateFlag.FORCED_CHAT.getMask();
        }
        if (player.getUpdateFlags().get(UpdateFlag.CHAT)
                && player != getMaster()) {
            mask |= UpdateFlag.CHAT.getMask();
        }
        if (player.getUpdateFlags().get(UpdateFlag.FACE_ENTITY)) {
            mask |= UpdateFlag.FACE_ENTITY.getMask();
        }
        if (player.getUpdateFlags().get(UpdateFlag.APPEARANCE) || force) {
            mask |= UpdateFlag.APPEARANCE.getMask();
        }
        if (player.getUpdateFlags().get(UpdateFlag.FACE_COORDINATE)) {
            mask |= UpdateFlag.FACE_COORDINATE.getMask();
        }
        if (player.getUpdateFlags().get(UpdateFlag.HIT)) {
            mask |= UpdateFlag.HIT.getMask();
        }
        if (player.getUpdateFlags().get(UpdateFlag.HIT_2)) {
            mask |= UpdateFlag.HIT_2.getMask();
        }
        if (mask >= 0x100) {
            mask |= 0x40;
            out.writeByte(mask & 0xFF);
            out.writeByte(mask >> 8);
        } else {
            out.writeByte(mask);
        }
        checkRequiredUpdates(out, player, force);
    }

    public void updateThisPlayerMovement(Stream out) {
        if (teleporting || mapRegionChanging) {
            out.writeBits(1, 1); // Update Required
            out.writeBits(2, 3); // Player Teleported
            out.writeBits(2, master.getLocation().getH()); // current height
            out.writeBits(1, teleporting); // teleporting);
            out.writeBits(1, master.getUpdateFlags().isUpdateRequired()); // update
            // required
            if (master.getCachedRegion() == null) {
                out.writeBits(7, master.getLocation().localY());
                out.writeBits(7, master.getLocation().localX());
            } else {
                out.writeBits(7, master.getCachedRegion().localY());
                out.writeBits(7, master.getCachedRegion().localX());
            }
        } else {
            if (master.getMobilityManager().walkingDirection() == -1) {
                if (master.getUpdateFlags().isUpdateRequired()) {
                    out.writeBits(1, 1); // update required
                    out.writeBits(2, 0); // we didn't move
                } else {
                    out.writeBits(1, 0); // Nothing changed
                }
            } else {
                if (master.getMobilityManager().runningDirection() == -1) {
                    out.writeBits(1, 1); // this is update required...
                    out.writeBits(2, 1); // walking
                    out.writeBits(3, master.getMobilityManager()
                            .walkingDirection()); // Direction
                    out.writeBits(1, master.getUpdateFlags().isUpdateRequired()); // Update
                    // block
                } else {
                    out.writeBits(1, 1); // updating required
                    out.writeBits(2, 2); // running - 2 seconds
                    out.writeBits(3, master.getMobilityManager()
                            .walkingDirection()); // Walking
                    out.writeBits(3, master.getMobilityManager()
                            .runningDirection()); // Running
                    out.writeBits(1, master.getUpdateFlags().isUpdateRequired()); // Update
                    // block
                }
            }
        }
    }

    private void updatePlayerMovement(Stream out, Player player) {
        if (player.getMobilityManager().walkingDirection() == -1) {
            if (player.getUpdateFlags().isUpdateRequired()) {
                out.writeBits(1, 1); // Update required
                out.writeBits(2, 0); // No movement
            } else {
                out.writeBits(1, 0); // Nothing changed
            }
        } else if (player.getMobilityManager().runningDirection() == -1) {
            out.writeBits(1, 1); // Update required
            out.writeBits(2, 1); // Player walking one tile
            out.writeBits(3, player.getMobilityManager().walkingDirection()); // Walking
            out.writeBits(1, player.getUpdateFlags().isUpdateRequired()); // Update
        } else {
            out.writeBits(1, 1); // Update Required
            out.writeBits(2, 2); // Moved two tiles
            out.writeBits(3, player.getMobilityManager().walkingDirection()); // Walking
            out.writeBits(3, player.getMobilityManager().runningDirection()); // Running
            out.writeBits(1, player.getUpdateFlags().isUpdateRequired()); // Update
        }
    }

    public void addPlayer(Stream out, Player otherPlayer) {
        out.writeBits(11, otherPlayer.getIndex()); // Writing player index.
        out.writeBits(1, 1); // Update required.
        out.writeBits(1, 1); // Discard walking.
        int yPos = otherPlayer.getLocation().getY()
                - master.getLocation().getY();
        int xPos = otherPlayer.getLocation().getX()
                - master.getLocation().getX();
        out.writeBits(5, yPos); // The relative coordinates.
        out.writeBits(5, xPos); // The relative coordinates.
    }

    private void checkRequiredUpdates(Stream out, Player player,
            boolean force) {
        if (player.getUpdateFlags().get(UpdateFlag.GRAPHICS)) {
            appendGraphicMask(player, out);
        }
        if (player.getUpdateFlags().get(UpdateFlag.ANIMATION)) {
            appendAnimationMask(player, out);
        }
        if (player.getUpdateFlags().get(UpdateFlag.FORCED_CHAT)) {
            out.writeString(player.getUpdateFlags().getForceChatMessage());
        }
        if (player.getUpdateFlags().get(UpdateFlag.CHAT)
                && player != getMaster()) {
            updatePlayerChat(out, player);
        }
        if (player.getUpdateFlags().get(UpdateFlag.FACE_ENTITY)) {
            out.writeWordBigEndian(player.getUpdateFlags().getFaceIndex());
        }
        if (player.getUpdateFlags().get(UpdateFlag.APPEARANCE) || force) {
            updatePlayerAppearance(out, player);
        }
        if (player.getUpdateFlags().get(UpdateFlag.FACE_COORDINATE)) {
            out.writeWordBigEndianA(player.getUpdateFlags().getFaceLocation().getX() * 2 + 1);
            out.writeWordBigEndian(player.getUpdateFlags().getFaceLocation().getY() * 2 + 1);
        }
        if (player.getUpdateFlags().get(UpdateFlag.HIT)) {
            updateHit(out, player);
        }
        if (player.getUpdateFlags().get(UpdateFlag.HIT_2)) {
            updatingHit2(out, player);
        }
        if (player != getMaster() && !force) {
            player.setCachedUpdateBlock(out);
        }
    }

    /**
     * Updates the hit mask
     *
     * @param out The output packet builder
     * @param player The player instance
     */
    public void updateHit(Stream out, Player player) {
        out.writeByte(player.getUpdateFlags().getDamage());
        out.writeByteA(player.getUpdateFlags().getHitType());
        out.writeByteC(player.getSkills().getPlayerLevel()[3]);
        out.writeByte(player.getSkills().getLevelForXP(
                player.getSkills().getPlayerExp()[3]));
    }

    private void updatingHit2(Stream out, Player player) {
        out.writeByte(player.getUpdateFlags().getDamage2());
        out.writeByteS(player.getUpdateFlags().getHitType2());
        out.writeByte(player.getSkills().getPlayerLevel()[3]);
        out.writeByteC(player.getSkills().getLevelForXP(
                player.getSkills().getPlayerExp()[3]));
    }

    public void updatePlayerChat(Stream out, Player player) {
        int effects = ((player.getUpdater().chatTextColor & 0xff) << 8)
                + (player.getUpdater().chatTextEffects & 0xff);
        out.writeWordBigEndian(effects);
        out.writeByte(player.getAuthentication().getPlayerRights());
        out.writeByteC(player.getUpdater().chatText.length);
        out.writeBytes(player.getUpdater().chatText,player.getUpdater().chatText.length ,0);
    }

    public void updatePlayerAppearance(Stream out, Player player) {
        Stream props = new Stream(new byte[128]);
        props.writeByte(player.getAuthentication().playerGender());
        props.writeByte(player.getPrayerIcon());
        props.writeByte(player.getPlayerHeadIcon());

        int[] equip = new int[player.getEquipment().capacity()];
        for (int i = 0; i < player.getEquipment().capacity(); i++) {
            equip[i] = player.getEquipment().items()[i].getIndex();
        }

        if (equip[Equipment.HAT_SLOT] > -1) {
            props.writeWord(0x200 + equip[Equipment.HAT_SLOT]);
        } else {
            props.writeByte(0); // Player Hat
        }

        if (equip[Equipment.CAPE_SLOT] > -1) {
            props.writeWord(0x200 + equip[Equipment.CAPE_SLOT]);
        } else {
            props.writeByte(0); // Player Cape
        }

        if (equip[Equipment.AMULET_SLOT] > -1) {
            props.writeWord(0x200 + equip[Equipment.AMULET_SLOT]);
        } else {
            props.writeByte(0); // Player Amulet
        }

        if (equip[Equipment.WEAPON_SLOT] > -1) {
            props.writeWord(0x200 + equip[Equipment.WEAPON_SLOT]);
        } else {
            props.writeByte(0); // Player Weapon
        }

        if (equip[Equipment.BODY_SLOT] > -1) {
            props.writeWord(0x200 + equip[Equipment.BODY_SLOT]);
        } else {
            props.writeWord(0x100 + player.getAuthentication().playerTorso()); // Player
            // Body
        }

        if (equip[Equipment.SHIELD_SLOT] > -1) {
            props.writeWord(0x200 + equip[Equipment.SHIELD_SLOT]);
        } else {
            props.writeByte(0); // Player Shield
        }

        if (ItemDefinition.fullBody(equip[Equipment.BODY_SLOT])) {
            props.writeByte(0);
        } else {
            props.writeWord(0x100 + player.getAuthentication().playerArms()); // Player
            // Arms
        }

        if (equip[Equipment.LEGS_SLOT] > -1) {
            props.writeWord(0x200 + equip[Equipment.LEGS_SLOT]);
        } else {
            props.writeWord(0x100 + player.getAuthentication().playerLegs()); // Player
            // Legs
        }

        if (ItemDefinition.fullHat(equip[Equipment.HAT_SLOT])) {
            props.writeByte(0);
        } else {
            props.writeWord(0x100 + player.getAuthentication().playerHead()); // Player
            // Head
        }

        if (equip[Equipment.HANDS_SLOT] > -1) {
            props.writeWord(0x200 + equip[Equipment.HANDS_SLOT]);
        } else {
            props.writeWord(0x100 + player.getAuthentication().playerHands()); // Player
            // Hands
        }

        if (equip[Equipment.FEET_SLOT] > -1) {
            props.writeWord(0x200 + equip[Equipment.FEET_SLOT]);
        } else {
            props.writeWord(0x100 + player.getAuthentication().playerFeet()); // Player
            // Feet
        }
        if (player.getAuthentication().getPlayerAppearanceIndex(0) == 0) {
            props.writeWord(0x100 + player.getAuthentication().playerJaw());
        } else {
            props.writeByte(0);
        }
        props.writeByte(player.getAuthentication().playerHairColour());
        props.writeByte(player.getAuthentication().playerTorsoColour());
        props.writeByte(player.getAuthentication().playerLegColour());
        props.writeByte(player.getAuthentication().playerFeetColour());
        props.writeByte(player.getAuthentication().playerSkinColour());
        props.writeWord(0x328); // TODO: standAnimIndex
        props.writeWord(0x337); // TODO: standTurnAnimIndex
        props.writeWord(0x333); // TODO: walkAnimIndex
        props.writeWord(0x334); // TODO: turn180AnimIndex
        props.writeWord(0x335); // TODO: turn90CWAnimIndex
        props.writeWord(0x336); // TODO: turn90CCWAnimIndex
        props.writeWord(0x338); // TODO: runAnimIndex
        /**
         * Sends player name as long
         */
        props.writeQWord(ProtocolUtils.getLongString(player.getAuthentication().getUsername()));
        props.writeByte(player.getSkills().calculateCombatLevel()); // send combat level
        props.writeWord(0); // games room title crap
        out.writeByteC(props.currentOffset);
        out.writeBytes(props.buffer, props.currentOffset, 0);
    }

    /**
     * Sends the graphic to the client to be displayed
     *
     * @param player
     * @param out
     */
    public void appendGraphicMask(Player player, Stream out) {
        if (player.getGraphic() != null) {
            out.writeWordBigEndian(player.getGraphic().getId());
            out.writeDWord(player.getGraphic().getValue());
        }
    }

    /**
     * Sends the animation to the client to be displayed
     *
     * @param player
     * @param out
     */
    private void appendAnimationMask(Player player, Stream out) {
        if (player.getAnimation() == null) {
            return;
        }
        out.writeWordBigEndian(player.getAnimation().getId());
        out.writeByteC(player.getAnimation().getDelay());
    }

    public PlayerUpdateTask localPlayers(List<Player> localPlayers) {
        this.localPlayers = localPlayers;
        return this;
    }

    public PlayerUpdateTask setMapRegionChanging(boolean status) {
        this.mapRegionChanging = status;
        return this;
    }

    public PlayerUpdateTask setTeleporting(boolean status) {
        this.teleporting = status;
        return this;
    }

    public PlayerUpdateTask chatText(byte[] chatText) {
        this.chatText = chatText;
        return this;
    }

    public PlayerUpdateTask chatTextEffects(int chatTextEffects) {
        this.chatTextEffects = chatTextEffects;
        return this;
    }

    public PlayerUpdateTask chatTextColor(int chatTextColor) {
        this.chatTextColor = chatTextColor;
        return this;
    }

    public void resetUpdateVars() {
        chatTextEffects = chatTextColor = 0;
        chatText = new byte[256];
        setTeleporting(false);
        setMapRegionChanging(false);
        master.getUpdateFlags().reset();
        master.resetCachedUpdateBlock();
        getMaster().getMobilityManager().walkingDirection(-1)
                .runningDirection(-1);
    }

    @Override
    public void run() {
        synchronize();
    }

    /**
     * @return the out
     */
    public Stream getOut() {
        return out;
    }

}
