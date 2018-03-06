/**
 * This file is part of Zap Framework.
 *
 * Zap is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Zap is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Zap. If not, see <http://www.gnu.org/licenses/>.
 */
package org.solace.network.packet;

import org.jboss.netty.buffer.ChannelBuffers;
import org.solace.Server;
import org.solace.event.Event;
import org.solace.game.content.skills.SkillHandler;
import org.solace.game.entity.ground.GroundItem;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.object.GameObject;
import org.solace.game.item.Item;
import org.solace.game.item.container.impl.Banking;
import org.solace.game.item.container.impl.Equipment;
import org.solace.game.item.container.impl.Inventory;
import org.solace.game.map.Location;
import org.solace.network.packet.Packet.Type;

/**
 *
 * @author Faris
 */
public class PacketDispatcher {

    public PacketDispatcher(Player player) {
        this.player = player;
    }
    private Player player;

    public void flushOutStream() {
        if (!player.getSession().getChannel().isConnected() || player.disconnected || player.getOutStream().currentOffset == 0) {
            return;
        }
        byte[] temp = new byte[player.getOutStream().currentOffset];
        System.arraycopy(player.getOutStream().buffer, 0, temp, 0, temp.length);
        Packet packet = new Packet(-1, Type.FIXED, ChannelBuffers.wrappedBuffer(temp));
        player.getSession().getChannel().write(packet);
        player.getOutStream().currentOffset = 0;
    }
    
    public void sendSong(int id) {
        if (player.getOutStream() != null && id != -1) {
            player.getOutStream().createFrame(74);
            player.getOutStream().writeWordBigEndian(id);
        }
    }

    public void sendQuickSong(int id, int songDelay) {
        if (player.getOutStream() != null) {
            player.getOutStream().createFrame(121);
            player.getOutStream().writeWordBigEndian(id);
            player.getOutStream().writeWordBigEndian(songDelay);
            flushOutStream();
        }
    }

    public void playSong(int id) {
        sendSong(id);
    }

    public void tempSong(int songID, int delay) {
        player.getOutStream().createFrame(121);
        player.getOutStream().writeWordBigEndian(songID);
        player.getOutStream().writeWordBigEndian(delay);
        flushOutStream();
    }

    public void sendClan(String name, String message, String clan, int rights) {
        player.getOutStream().createFrameVarSizeWord(217);
        player.getOutStream().writeString(name);
        player.getOutStream().writeString(message);
        player.getOutStream().writeString(clan);
        player.getOutStream().writeWord(rights);
        player.getOutStream().endFrameVarSize();
    }

    public void writeInfoOnLogin() {
        player.getOutStream().createFrame(249);
        player.getOutStream().writeByteA(1);
        player.getOutStream().writeWordBigEndianA(player.getIndex());
    }


    public void createPlayerHints(int type, int id) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(254);
            player.getOutStream().writeByte(type);
            player.getOutStream().writeWord(id);
            player.getOutStream().write3Byte(0);
            flushOutStream();
        }
    }

    public void createObjectHints(int x, int y, int height, int pos) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(254);
            player.getOutStream().writeByte(pos);
            player.getOutStream().writeWord(x);
            player.getOutStream().writeWord(y);
            player.getOutStream().writeByte(height);
            flushOutStream();
        }
    }

    public void setPrivateMessaging(int i) { // friends and ignore list status
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(221);
            player.getOutStream().writeByte(i);
            flushOutStream();
        }

    }

    public void setChatOptions(int publicChat, int privateChat, int tradeBlock) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(206);
            player.getOutStream().writeByte(publicChat);
            player.getOutStream().writeByte(privateChat);
            player.getOutStream().writeByte(tradeBlock);
            flushOutStream();
        }

    }

    public void setConfig(int id, int state) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(36);
            player.getOutStream().writeWordBigEndian(id);
            player.getOutStream().writeByte(state);
            flushOutStream();
        }
    }

    public void sendConfig(int id, int state) {
        if (state < Byte.MIN_VALUE || state > Byte.MAX_VALUE) {
            player.getOutStream().createFrame(87);
            player.getOutStream().writeWordBigEndian_dup(id);
            player.getOutStream().writeDWord_v1(state);
            flushOutStream();
        } else {
            player.getOutStream().createFrame(36);
            player.getOutStream().writeWordBigEndian(id);
            player.getOutStream().writeByte(state);
            flushOutStream();
        }

    }

    public void sendLink(String s) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrameVarSizeWord(187);
            player.getOutStream().writeString(s);
        }
    }
    
    public void showInterface(int i) {
        player.getOutStream().createFrame(97);
        player.getOutStream().writeWord(i);
        flushOutStream();
    }

    public  void sendQuest(String s, int i) {
        player.getOutStream().createFrameVarSizeWord(126);
        player.getOutStream().writeString(s);
        player.getOutStream().writeWordA(i);
        player.getOutStream().endFrameVarSizeWord();
        flushOutStream();
    }

    public void sendStillGraphics(int id, int heightS, int y, int x, int timeBCS) {
        player.getOutStream().createFrame(85);
        player.getOutStream().writeByteC(y - (player.getCachedRegion().regionY() * 8));
        player.getOutStream().writeByteC(x - (player.getCachedRegion().regionX() * 8));
        player.getOutStream().createFrame(4);
        player.getOutStream().writeByte(0);// Tiles away (X >> 4 + Y & 7)
        player.getOutStream().writeWord(id); // Graphic ID.
        player.getOutStream().writeByte(heightS); // Height of the graphic when
        player.getOutStream().writeWord(timeBCS); // Time before the graphic
        flushOutStream();
    }

    public void createArrow(int type, int id) {
        if (player != null) {
            player.getOutStream().createFrame(254); // The packet ID
            player.getOutStream().writeByte(type); // 1=NPC, 10=Player
            player.getOutStream().writeWord(id); // NPC/Player ID
            player.getOutStream().write3Byte(0); // Junk
        }
    }

    public void createArrow(int x, int y, int height, int pos) {
        if (player != null) {
            player.getOutStream().createFrame(254); // The packet ID
            player.getOutStream().writeByte(pos); // Position on Square(2 = middle, 3
            player.getOutStream().writeWord(x); // X-Coord of Object
            player.getOutStream().writeWord(y); // Y-Coord of Object
            player.getOutStream().writeByte(height); // Height off Ground
        }
    }

    public void createPlayersObjectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
        try {
            player.getOutStream().createFrame(85);
            player.getOutStream().writeByteC(Y - (player.getCachedRegion().regionY() * 8));
            player.getOutStream().writeByteC(X - (player.getCachedRegion().regionX() * 8));
            int x = 0;
            int y = 0;
            player.getOutStream().createFrame(160);
            player.getOutStream().writeByteS(((x & 7) << 4) + (y & 7));// tiles away
            player.getOutStream().writeByteS((tileObjectType << 2) + (orientation & 3));
            player.getOutStream().writeWordA(animationID);// animation id
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void multiWay(int i1) {
        player.getOutStream().createFrame(61);
        player.getOutStream().writeByte(i1);
        player.updateRequired = true;
        player.setAppearanceUpdateRequired(true);
    }

    public void changeToSidebar(int i1) {
        player.getOutStream().createFrame(106);
        player.getOutStream().writeByteC(i1);
    }

    public void PlayerMusic(int music) {
        player.getOutStream().createFrame(74);
        player.getOutStream().writeWordBigEndian(music);
    }

    public void flashSelectedSidebar(int i1) {
        player.getOutStream().createFrame(24);
        player.getOutStream().writeByteA(i1);
    }
    
    public void sendInitPacket() {
        player.getOutStream().createFrame(249);
        player.getOutStream().writeByteA(1);
        player.getOutStream().writeWordBigEndianA(player.getIndex());
        flushOutStream();
        for (int i = 0; i < SkillHandler.MAXIMUM_SKILLS; i++) {
            player.getSkills().refreshSkill(i);
        }
        player.getPrivateMessaging().updateFriendsListStatus();
        Server.getService().schedule(new Event(3) {
            @Override
            public void execute() {
                player.getPrivateMessaging().refresh(false);
                this.stop();
            }
        });
        player.getPrivateMessaging().refresh(false);
        setSidebarInterfaces();
        sendPlayerMenuOption(3, "Attack");
        sendPlayerMenuOption(4, "Trade With");
        sendPlayerMenuOption(5, "Follow");
        player.handleLoginData();
        sendLoginConfig();
    }
    
    private void setSidebarInterfaces() {
        int[] data = {2423, 3917, 638, 3213, 1644, 5608, 1151, -1, 5065, 5715,
            2449, 904, 147, 962};
        // TODO: player magic book
        for (int i = 0; i < data.length; i++) {
            sendSidebar(i, data[i]);
        }
    }

    public void sendLoginConfig() {
        sendConfig(152, player.getSettings().isAutoRetaliating() ? 1 : 0);
        sendConfig(173, player.getMobilityManager().running() ? 1 : 0);

    }
    
    /**
     * Sends a player right click menu option.
     *
     * @param menuIndex the menu index
     * @param menuName the menu option name
     */
    public void sendPlayerMenuOption(int menuIndex, String menuName) {
        player.getOutStream().createFrame(104);
        player.getOutStream().writeByteC(menuIndex);
        player.getOutStream().writeByteA(0);
        player.getOutStream().writeString(menuName);
        flushOutStream();
    }

    public void sendProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight, int endHeight, int lockon,
            int time, int slope) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(85);
            player.getOutStream().writeByteC((y - (player.getCachedRegion().regionY() * 8)) - 2);
            player.getOutStream().writeByteC((x - (player.getCachedRegion().regionX() * 8)) - 3);
            player.getOutStream().createFrame(117);
            player.getOutStream().writeByte(angle);
            player.getOutStream().writeByte(offY);
            player.getOutStream().writeByte(offX);
            player.getOutStream().writeWord(lockon);
            player.getOutStream().writeWord(gfxMoving);
            player.getOutStream().writeByte(startHeight);
            player.getOutStream().writeByte(endHeight);
            player.getOutStream().writeWord(time);
            player.getOutStream().writeWord(speed);
            player.getOutStream().writeByte(slope);
            player.getOutStream().writeByte(64);
            flushOutStream();
        }

    }
    
    public void stillGfx(int id, int x, int y, int height, int time) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(85);
            player.getOutStream().writeByteC(y - (player.getCachedRegion().regionY() * 8));
            player.getOutStream().writeByteC(x - (player.getCachedRegion().regionX() * 8));
            player.getOutStream().createFrame(4);
            player.getOutStream().writeByte(0);
            player.getOutStream().writeWord(id);
            player.getOutStream().writeByte(height);
            player.getOutStream().writeWord(time);
            flushOutStream();
        }

    }

    public void object(int objectId, int objectX, int objectY, int face, int objectType) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(85);
            player.getOutStream().writeByteC(objectY - (player.getCachedRegion().regionY() * 8));
            player.getOutStream().writeByteC(objectX - (player.getCachedRegion().regionX() * 8));
            player.getOutStream().createFrame(101);
            player.getOutStream().writeByteC((objectType << 2) + (face & 3));
            player.getOutStream().writeByte(0);
            if (objectId != -1) { // removing
                player.getOutStream().createFrame(151);
                player.getOutStream().writeByteS(0);
                player.getOutStream().writeWordBigEndian(objectId);
                player.getOutStream().writeByteS((objectType << 2) + (face & 3));
            }
            flushOutStream();
        }
    }

    public void checkObjectSpawn(int objectId, int objectX, int objectY, int face, int objectType) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(85);
            player.getOutStream().writeByteC(objectY - (player.getCachedRegion().regionY() * 8));
            player.getOutStream().writeByteC(objectX - (player.getCachedRegion().regionX() * 8));
            player.getOutStream().createFrame(101);
            player.getOutStream().writeByteC((objectType << 2) + (face & 3));
            player.getOutStream().writeByte(0);

            if (objectId != -1) { // removing
                player.getOutStream().createFrame(151);
                player.getOutStream().writeByteS(0);
                player.getOutStream().writeWordBigEndian(objectId);
                player.getOutStream().writeByteS((objectType << 2) + (face & 3));
            }
            flushOutStream();
        }
    }

    public void showOption(int i, int l, String s, int a) {
        if (player.getOutStream() != null && player != null) {
                player.getOutStream().createFrameVarSize(104);
                player.getOutStream().writeByteC(i);
                player.getOutStream().writeByteA(l);
                player.getOutStream().writeString(s);
                player.getOutStream().endFrameVarSize();
                flushOutStream();
        }
    }

    public void openUpBank() {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(248);
            player.getOutStream().writeWordA(5292);
            player.getOutStream().writeWord(5063);
            flushOutStream();
        }

    }

    /**
     * Show an arrow icon on the selected player.
     *
     * @Param i - Either 0 or 1; 1 is arrow, 0 is none.
     * @Param j - The player/Npc that the arrow will be displayed above.
     * @Param k - Keep this set as 0
     * @Param l - Keep this set as 0
     */
    public void drawHeadicon(int i, int j, int k, int l) {
        player.getOutStream().createFrame(254);
        player.getOutStream().writeByte(i);

        if (i == 1 || i == 10) {
            player.getOutStream().writeWord(j);
            player.getOutStream().writeWord(k);
            player.getOutStream().writeByte(l);
        } else {
            player.getOutStream().writeWord(k);
            player.getOutStream().writeWord(l);
            player.getOutStream().writeByte(j);
        }

    }

    public void loadPM(long playerName, int world) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(50);
            player.getOutStream().writeQWord(playerName);
            player.getOutStream().writeByte(world);
            flushOutStream();
        }

    }

    public void sendMessage(String string) {
        if (player.getOutStream() != null) {
            player.getOutStream().createFrameVarSize(253);
            player.getOutStream().writeString(string);
            player.getOutStream().endFrameVarSize();
        }
    }

    public void sendGroundItem(GroundItem groundItem) {
        //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendRemoveGroundItem(GroundItem groundItem) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendLogout() {
        player.getOutStream().createFrame(109);
        flushOutStream();
    }

    public void sendPMServer(int i) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendFriendList(long friend, int checkOnlineStatus) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendPrivateMessage(long name, int rights, byte[] message, int size) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrameVarSize(196);
            player.getOutStream().writeQWord(name);
            player.getOutStream().writeDWord(player.getPrivateMessaging().getPrivateMessageId());
            player.getOutStream().writeByte(rights);
            player.getOutStream().writeBytes(message, size, 0);
            player.getOutStream().endFrameVarSize();
            flushOutStream();
        }
    }

    public void sendProjectile(Location location, int offX, int offY, int id, int startHeight, int endHeight, int speed, int lockon) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(85);
            player.getOutStream().writeByteC((location.getY() - (player.getCachedRegion().regionY() * 8)) - 2);
            player.getOutStream().writeByteC((location.getX() - (player.getCachedRegion().regionX() * 8)) - 3);
            player.getOutStream().createFrame(117);
            player.getOutStream().writeByte(50);
            player.getOutStream().writeByte(offY);
            player.getOutStream().writeByte(offX);
            player.getOutStream().writeWord(lockon);
            player.getOutStream().writeWord(id);
            player.getOutStream().writeByte(startHeight);
            player.getOutStream().writeByte(endHeight);
            player.getOutStream().writeWord(51);
            player.getOutStream().writeWord(speed);
            player.getOutStream().writeByte(16);
            player.getOutStream().writeByte(64);
            flushOutStream();
        }
    }

    public void sendSidebar(int i, int i0) {
        if (player.getOutStream() != null) {
            player.getOutStream().createFrame(71);
            player.getOutStream().writeWord(i);
            player.getOutStream().writeByteA(i0);
        }
    }

    public void sendString(int i, String line1) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendChatInterface(int i) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendDialogueAnimation(int i, int emotion) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendPlayerDialogueHead(int i) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendNPCDialogueHead(int id, int i) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendSkill(int i) {
         if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(134);
            player.getOutStream().writeByte(i);
            player.getOutStream().writeDWord_v1(player.getSkills().getPlayerExp()[i]);
            player.getOutStream().writeByte(player.getSkills().getPlayerLevel()[i]);
            flushOutStream();
        }
    }

    public void sendWalkableInterface(int i) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(208);
            player.getOutStream().writeWordBigEndian_dup(i);
            flushOutStream();
        }
    }

    public void sendCloseInterface() {
       if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(219);
            flushOutStream();
        }
    }

    public void sendInterface(int i) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendObject(GameObject gameObject, boolean b) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendRemoveObject(GameObject o) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendInterfaceSet(int i, int i0) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendItemContainer(Inventory container, int interfaceIndex) {
        player.getOutStream().createFrame(53);
        player.getOutStream().writeWord(interfaceIndex);
        player.getOutStream().writeWord(container.capacity());
        for (Item item : container.items()) {
            if (item.getAmount() > 254) {
                player.getOutStream().writeByte(255);
                player.getOutStream().writeDWord(item.getAmount());
            } else {
                player.getOutStream().writeByte(item.getAmount());
            }
            player.getOutStream().writeWordBigEndianA(item.getIndex() + 1);
        }
        flushOutStream();
    }

    public void sendItemContainer(Banking aThis, int BANK_INTERFACE) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void moveComponent(int i, int i0, int i1) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void interfaceConfig(int i, int i0) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendItemOnInterface(int i, int i0, int id) {
        //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendItemContainer(Equipment aThis, int EQUIPMENT_INTERFACE) {
        //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendMapRegion() {
        player.getOutStream().createFrame(73);
        player.getOutStream().writeWordBigEndianA(player.getLocation().regionX() + 6);
        player.getOutStream().writeWordBigEndian(player.getLocation().regionY() + 6);
        flushOutStream();
        player.cacheRegion(player.getLocation().copy());
    }

}
