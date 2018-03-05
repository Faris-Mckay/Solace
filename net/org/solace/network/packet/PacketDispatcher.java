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
import org.solace.game.entity.ground.GroundItem;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.object.GameObject;
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

    /**
     * MulitCombat icon
     *
     * @param i1 0 = off 1 = on
     */
    public void multiWay(int i1) {
        player.getOutStream().createFrame(61);
        player.getOutStream().writeByte(i1);
        player.updateRequired = true;
        player.setAppearanceUpdateRequired(true);
    }

    public void sendFrame126(String s, int id) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrameVarSizeWord(126);
            player.getOutStream().writeString(s);
            player.getOutStream().writeWordA(id);
            player.getOutStream().endFrameVarSizeWord();
            flushOutStream();
        }
    }
    
    public void sendMapRegion() {
            player.getOutStream().createFrame(73);
            player.getOutStream().writeWordA(player.getLocation().getRegion().regionX() + 6);
            player.getOutStream().writeWord(player.getLocation().getRegion().regionY() + 6);
            flushOutStream();
            player.setCachedRegion(player.getLocation().getRegion());
    }

    public void sendLink(String s) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrameVarSizeWord(187);
            player.getOutStream().writeString(s);
        }
    }

    public void setSkillLevel(int skillNum, int currentLevel, int XP) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(134);
            player.getOutStream().writeByte(skillNum);
            player.getOutStream().writeDWord_v1(XP);
            player.getOutStream().writeByte(currentLevel);
            flushOutStream();
        }
    }

    public void sendFrame106(int sideIcon) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(106);
            player.getOutStream().writeByteC(sideIcon);
            flushOutStream();
            //requestUpdates();
        }
    }

    public void sendFrame107() {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(107);
            flushOutStream();
        }
    }

    public void sendFrame36(int id, int state) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(36);
            player.getOutStream().writeWordBigEndian(id);
            player.getOutStream().writeByte(state);
            flushOutStream();
        }
    }

    public void sendFrame185(int Frame) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(185);
            player.getOutStream().writeWordBigEndianA(Frame);
        }
    }

    public void showInterface(int interfaceid) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(97);
            player.getOutStream().writeWord(interfaceid);
            flushOutStream();
        }
    }

    public void sendFrame248(int MainFrame, int SubFrame) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(248);
            player.getOutStream().writeWordA(MainFrame);
            player.getOutStream().writeWord(SubFrame);
            flushOutStream();
        }
    }

    public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(246);
            player.getOutStream().writeWordBigEndian(MainFrame);
            player.getOutStream().writeWord(SubFrame);
            player.getOutStream().writeWord(SubFrame2);
            flushOutStream();
        }
    }

    public void sendFrame171(int MainFrame, int SubFrame) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(171);
            player.getOutStream().writeByte(MainFrame);
            player.getOutStream().writeWord(SubFrame);
            flushOutStream();
        }
    }

    public void sendFrame200(int MainFrame, int SubFrame) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(200);
            player.getOutStream().writeWord(MainFrame);
            player.getOutStream().writeWord(SubFrame);
            flushOutStream();
        }
    }

    public void sendFrame70(int i, int o, int id) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(70);
            player.getOutStream().writeWord(i);
            player.getOutStream().writeWordBigEndian(o);
            player.getOutStream().writeWordBigEndian(id);
            flushOutStream();
        }
    }

    public void sendFrame75(int MainFrame, int SubFrame) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(75);
            player.getOutStream().writeWordBigEndianA(MainFrame);
            player.getOutStream().writeWordBigEndianA(SubFrame);
            flushOutStream();
        }
    }

    public void sendFrame164(int Frame) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(164);
            player.getOutStream().writeWordBigEndian_dup(Frame);
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

    public void sendFrame87(int id, int state) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(87);
            player.getOutStream().writeWordBigEndian_dup(id);
            player.getOutStream().writeDWord_v1(state);
            flushOutStream();
        }
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

    public void removeAllWindows() {
        if (player.getOutStream() != null && player != null) {
            //player.getPA().resetVariables();
            player.getOutStream().createFrame(219);
            flushOutStream();
        }
    }

    public void closeAllWindows() {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(219);
            flushOutStream();
        }
    }

    public void sendFrame34(int id, int slot, int column, int amount) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrameVarSizeWord(34); // init item to smith
            // screen
            player.getOutStream().writeWord(column); // Column Across Smith Screen
            player.getOutStream().writeByte(4); // Total Rows?
            player.getOutStream().writeDWord(slot); // Row Down The Smith Screen
            player.getOutStream().writeWord(id + 1); // item
            player.getOutStream().writeByte(amount); // how many there are?
            player.getOutStream().endFrameVarSizeWord();
        }
    }

    public void walkableInterface(int id) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(208);
            player.getOutStream().writeWordBigEndian_dup(id);
            flushOutStream();
        }
    }
    public int mapStatus = 0;

    public void sendFrame99(int state) { // used for disabling map
        if (player.getOutStream() != null && player != null) {
            if (mapStatus != state) {
                mapStatus = state;
                player.getOutStream().createFrame(99);
                player.getOutStream().writeByte(state);
                flushOutStream();
            }
        }
    }

    public void sendCrashFrame() { // used for crashing cheat clients
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(123);
            flushOutStream();
        }
    }

   
    public void createProjectile(int x, int y, int offX, int offY, int angle,
            int speed, int gfxMoving, int startHeight, int endHeight,
            int lockon, int time) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(85);
            player.getOutStream().writeByteC(y - (player.getLocation().getRegion().regionY() * 8));
            player.getOutStream().writeByteC(x - (player.getLocation().getRegion().regionX() * 8));
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
            player.getOutStream().writeByte(16);
            player.getOutStream().writeByte(64);
            flushOutStream();
        }
    }

    public void createProjectile2(int x, int y, int offX, int offY, int angle,
            int speed, int gfxMoving, int startHeight, int endHeight,
            int lockon, int time, int slope) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(85);
            player.getOutStream().writeByteC(y - (player.getLocation().getRegion().regionY() * 8));
            player.getOutStream().writeByteC(x - (player.getLocation().getRegion().regionX() * 8));
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
            player.getOutStream().writeByteC(y - (player.getLocation().getRegion().regionY() * 8));
            player.getOutStream().writeByteC(x - (player.getLocation().getRegion().regionX() * 8));
            player.getOutStream().createFrame(4);
            player.getOutStream().writeByte(0);
            player.getOutStream().writeWord(id);
            player.getOutStream().writeByte(height);
            player.getOutStream().writeWord(time);
            flushOutStream();
        }
    }

    public void object(int objectId, int objectX, int objectY, int face,
            int objectType) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(85);
            player.getOutStream().writeByteC(objectY - (player.getLocation().getRegion().regionY() * 8));
            player.getOutStream().writeByteC(objectX - (player.getLocation().getRegion().regionX() * 8));
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

    public void checkObjectSpawn(int objectId, int objectX, int objectY,
            int face, int objectType) {
        if (player.getOutStream() != null && player != null) {
            player.getOutStream().createFrame(85);
            player.getOutStream().writeByteC(objectY - (player.getLocation().getRegion().regionY() * 8));
            player.getOutStream().writeByteC(objectX - (player.getLocation().getRegion().regionX() * 8));
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
    /**
     * Show option, attack, trade, follow etc
	 *
     */
    public String optionType = "null";

    public void showOption(int i, int l, String s, int a) {
        if (player.getOutStream() != null && player != null) {
            if (!optionType.equalsIgnoreCase(s)) {
                optionType = s;
                player.getOutStream().createFrameVarSize(104);
                player.getOutStream().writeByteC(i);
                player.getOutStream().writeByteA(l);
                player.getOutStream().writeString(s);
                player.getOutStream().endFrameVarSize();
                flushOutStream();
            }
        }
    }

    public void openUpBank() {
        if (player.getOutStream() != null && player != null) {
            /*player.getItems().resetItems(5064);
            player.getItems().rearrangeBank();
            player.getItems().resetBank();
            player.getItems().resetTempItems();*/
            player.getOutStream().createFrame(248);
            player.getOutStream().writeWordA(5292);
            player.getOutStream().writeWord(5063);
            flushOutStream();
        }
    }

    public void sendMessage(String string) {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendGroundItem(GroundItem groundItem) {
    //    throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendRemoveGroundItem(GroundItem groundItem) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendLogout() {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendPMServer(int i) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendFriendList(long friend, int checkOnlineStatus) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendPrivateMessage(long usernameAsLong, int playerRights, byte[] message, int size) {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendConfig(int configId, int i) {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendProjectile(Location location, int offsetX, int offsetY, int id, int startHeight, int endHeight, int speed, int lockon) {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendSidebar(int i, int i0) {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendWalkableInterface(int i) {
      //  throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void sendCloseInterface() {
     //   throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

    public void sendItemContainer(Inventory aThis, int BANK_INTERFACE) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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

 

    
}
