package org.solace.network.packet;

import org.solace.world.game.entity.mobile.player.Player;
import org.solace.world.map.Location;

/**
 * Protocol packets sender.
 * @author Faris
 */
public class PacketDispatcher {

	private final Player player;

	/**
	 * Creates a new packet sender for player.
	 * @param player
	 *            the player object
	 */
	public PacketDispatcher(Player player) {
		this.player = player;
	}

	/**
	 * sends the frame id to the client for chat box
	 * @param frame
	 * @return
	 */

	public PacketDispatcher sendChatInterface(int frame) {
		PacketBuilder out = PacketBuilder.allocate(128);
		out.createFrame(164, player.channelContext().encryption());
		out.putLEShort(frame);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	public PacketDispatcher sendCoordinates2(Location position) {
		PacketBuilder out = PacketBuilder.allocate(3);
		out.createFrame(85, player.channelContext().encryption());
		int y = position.getY() - player.getLocation().getRegion().regionY() * 8 - 2;
		int x = position.getX() - player.getLocation().getRegion().regionX() * 8 - 3;
		out.putByteC(y);
		out.putByteC(x);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	public PacketDispatcher sendProjectile(Location position, int offsetX,int offsetY, int id, int startHeight, int endHeight, int speed,int lockon) {
		PacketBuilder out = PacketBuilder.allocate(32);
		sendCoordinates2(position);
		out.createFrame(117, player.channelContext().encryption());
		out.putByte(50);
		out.putByte(offsetY);
		out.putByte(offsetX);
		out.putShort(lockon);
		out.putShort(id);
		out.putByte(startHeight);
		out.putByte(endHeight);
		out.putShort(51);
		out.putShort(speed);
		out.putByte(16);
		out.putByte(64);
		out.sendTo(player.channelContext().channel());
		return this;
	}
	
	public PacketDispatcher sendDialogueAnimation(int animId, int interfaceId) {
            PacketBuilder out = PacketBuilder.allocate(5);
            out.createFrame(200, player.channelContext().encryption());
            out.putShort(animId);
            out.putShort(interfaceId);
            out.sendTo(player.channelContext().channel());
            return this;
	}

	public PacketDispatcher sendPlayerDialogueHead(int interfaceId) {
		PacketBuilder out = PacketBuilder.allocate(5);
		out.createFrame(185, player.channelContext().encryption());
		out.putLEShortA(interfaceId);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	public PacketDispatcher sendNPCDialogueHead(int npcId, int interfaceId) {
		PacketBuilder out = PacketBuilder.allocate(100);
		out.createFrame(75, player.channelContext().encryption());
		out.putLEShortA(npcId);
		out.putLEShortA(interfaceId);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	/**
	 * Sends online status to the friends list for any player on the list
	 * @param name
	 * @param world
	 * @return
	 */
	public PacketDispatcher sendFriendList(long name, int world) {
		PacketBuilder out = PacketBuilder.allocate(10);
		out.createFrame(50, player.channelContext().encryption());
		if (world != 0) {
			world += 9;
		}
		out.putLong(name);
		out.putByte(world);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	public PacketDispatcher sendItemOnInterface(int id, int zoom, int model) {
		PacketBuilder out = PacketBuilder.allocate(7);
		out.createFrame(246, player.channelContext().encryption());
		out.putLEShort(id);
		out.putShort(zoom);
		out.putShort(model);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	public PacketDispatcher sendPMServer(int state) {
		PacketBuilder out = PacketBuilder.allocate(2);
		out.createFrame(221, player.channelContext().encryption());
		out.putByte(state);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	public PacketDispatcher createPlayerHints(int type, int id, int k, int l) {
		PacketBuilder out = PacketBuilder.allocate(2028);
		out.createFrame(254, player.channelContext().encryption());
		out.putByte(type);
		if (type == 1 || type == 10) {
			out.putShort(id);
			out.putShort(k);
			out.putByte((byte) l);
		} else {
			out.putShort(k);
			out.putShort(l);
			out.putByte((byte) id);
		}
		out.sendTo(player.channelContext().channel());
		return this;
	}

	public PacketDispatcher sendPrivateMessage(long name, int rights, byte[] message, int messageSize) {
		PacketBuilder out = PacketBuilder.allocate(2048);
		out.createSizedFrame(196, player.channelContext().encryption());
		out.putLong(name);
		out.putInt(player.getPrivateMessaging().getPrivateMessageId());
		out.putByte(rights);
		out.writeBytes(message, messageSize);
		out.finishSizedFrame();
		out.sendTo(player.channelContext().channel());
		return this;
	}

	
	public PacketDispatcher sendInitPacket() {
		PacketBuilder out = PacketBuilder.allocate(4);
		out.createFrame(249, player.channelContext().encryption());
		out.putByteA(1);
		out.putLEShortA(player.getIndex());
		out.sendTo(player.channelContext().channel());
		//for (int i = 0; i < Skill.SKILL_COUNT; i++) {
		//	sendSkill(i);
		//}
		sendSidebar(0, 2423); // attack tab
		sendSidebar(1, 3917); // skills tab
		sendSidebar(2, 638); // quest tab
		sendSidebar(3, 3213); // backpack tab
		sendSidebar(4, 1644); // items wearing tab
		sendSidebar(5, 5608); // pray tab
		//sendSidebar(6, player.getMagicBook() == 1 ? 1151 : 12855); // magic tab
		sendSidebar(7, -1); // clan chat
		sendSidebar(8, 5065); // friend
		sendSidebar(9, 5715); // ignore
		sendSidebar(10, 2449); // logout tab
		sendSidebar(11, 904); // wrench tab
		sendSidebar(12, 147); // run tab
		sendSidebar(13, -1); // harp tab
		sendPlayerMenuOption(3, "Attack");
		sendPlayerMenuOption(4, "Trade With");
		sendPlayerMenuOption(5, "Follow");
		//sendItemContainer(player.getInventory(), Inventory.INVENTORY_INTERFACE);
		//sendItemContainer(player.getEquipment(), Equipment.EQUIPMENT_INTERFACE);
		player.handleLoginData();
		sendLoginConfig();
		return this;
	}
	
	public PacketDispatcher sendLoginConfig() {
		//sendConfig(152, player.isAutoRetaliating() ? 1 : 0);
                //sendConfig(173, player.isAutoRetaliating() ? 1 : 0);
		//sendConfig(173, player.movementQueue().running() ? 1 : 0);
		return this;
	}

	/**
	 * Sends the logout packet.
	 */
	public PacketDispatcher sendLogout() {
		PacketBuilder out = PacketBuilder.allocate(1);
		out.createFrame(109, player.channelContext().encryption());
		out.sendTo(player.channelContext().channel());
		return this;
	}

	
	public PacketDispatcher sendMapRegion() {
		PacketBuilder out = PacketBuilder.allocate(5);
		out.createFrame(73, player.channelContext().encryption());
		out.putShortA(player.getLocation().getRegion().regionX() + 6);
		out.putShort(player.getLocation().getRegion().regionY() + 6);
		out.sendTo(player.channelContext().channel());
		player.currentRegion(player.getLocation().copy());
		return this;
	}

	/**
	 * Sends message to the chat window.
	 * 
	 * @param message
	 *            the message
	 */
	public PacketDispatcher sendMessage(String message) {
		PacketBuilder out = PacketBuilder.allocate(256);
		out.createSizedFrame(253, player.channelContext().encryption());
		out.putString(message);
		out.finishSizedFrame();
		out.sendTo(player.channelContext().channel());
		return this;
	}
        /*
	public PacketDispatcher sendSpecialBar(boolean usingSpecial, boolean flag) {
		sendConfig(300, player.getSpecialAmount());
		if (flag) {
			sendConfig(301, usingSpecial ? 1 : 0);
			player.setUsingSpecial(usingSpecial);
			player.getEquipment().refreshItems();
			player.getEquipment().sendWeapon(player);
		}
		return this;
	}*/

	/**
	 * Sends string to the interface.
	 * 
	 * @param stringIndex
	 *            the string index
	 * 
	 * @param string
	 *            the string
	 */
	public PacketDispatcher sendString(int stringIndex, String string) {
		PacketBuilder out = PacketBuilder.allocate(2048);
		out.createShortSizedFrame(126, player.channelContext().encryption());
		out.putString(string);
		out.putShortA(stringIndex);
		out.finishShortSizedFrame();
		out.sendTo(player.channelContext().channel());
		return this;
	}

        
	public PacketDispatcher sendCloseInterface() {
		PacketBuilder out = PacketBuilder.allocate(1);
		out.createFrame(219, player.channelContext().encryption());
		//player.getDialogue().setNewDialogue(-1);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	/**
	 * Sends interface set to the client.
	 * 
	 * @param interfaceIndex
	 *            the game window interface
	 * 
	 * @param sidebarInterfaceIndex
	 *            the sidebar interface
	 */
	public PacketDispatcher sendInterfaceSet(int interfaceIndex,
			int sidebarInterfaceIndex) {
		PacketBuilder out = PacketBuilder.allocate(5);
		out.createFrame(248, player.channelContext().encryption());
		out.putShortA(interfaceIndex);
		out.putShort(sidebarInterfaceIndex);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	/**
	 * Opens an interface
	 * 
	 * @param interfaceIndex
	 *            The interface id
	 * @return The packet sender
	 */
	public PacketDispatcher sendInterface(int interfaceIndex) {
		PacketBuilder out = PacketBuilder.allocate(5);
		out.createFrame(97, player.channelContext().encryption());
		out.putShort(interfaceIndex);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	/**
	 * Sends a player right click menu option.
	 * 
	 * @param menuIndex
	 *            the menu index
	 * 
	 * @param menuName
	 *            the menu option name
	 */
	public PacketDispatcher sendPlayerMenuOption(int menuIndex, String menuName) {
		PacketBuilder out = PacketBuilder.allocate(256);
		out.createSizedFrame(104, player.channelContext().encryption());
		out.putByteC(menuIndex).putByteA(0);
		out.putString(menuName);
		out.finishSizedFrame();
		out.sendTo(player.channelContext().channel());
		return this;
	}

	/**
	 * Sends the interface which shuold be associated with the sidebar.
	 * 
	 * @param sidebarId
	 *            the sidebar index
	 * 
	 * @param interfaceId
	 *            the interface index
	 */
	public PacketDispatcher sendSidebar(int sidebarId, int interfaceId) {
		PacketBuilder out = PacketBuilder.allocate(4);
		out.createFrame(71, player.channelContext().encryption());
		out.putShort(interfaceId);
		out.putByteA(sidebarId);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	public PacketDispatcher moveComponent(int i, int o, int id) {
		PacketBuilder out = PacketBuilder.allocate(128);
		out.createFrame(70, player.channelContext().encryption());
		out.putShort(i);
		out.putLEShort(o);
		out.putLEShort(id);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	public PacketDispatcher interfaceConfig(int MainFrame, int SubFrame) {
		PacketBuilder out = PacketBuilder.allocate(4);
		out.createFrame(171, player.channelContext().encryption());
		out.putByte(MainFrame);
		out.putShort(SubFrame);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	/**
	 * Sends the client configuration.
	 * 
	 * @param configIndex
	 *            the configuration index
	 * 
	 * @param state
	 *            the configuration state
	 */
	public PacketDispatcher sendConfig(int configIndex, int state) {
		PacketBuilder out = PacketBuilder.allocate(4);
		out.createFrame(36, player.channelContext().encryption());
		out.putLEShort(configIndex);
		out.putByte(state);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	/*
	public PacketDispatcher sendItemContainer(ItemContainer container,int interfaceIndex) {
		PacketBuilder out = PacketBuilder.allocate(5 + (container.capacity() * 7));
		out.createShortSizedFrame(53, player.channelContext().encryption());
		out.putShort(interfaceIndex);
		out.putShort(container.capacity());
		for (Item item : container.items()) {
			if (item.amount() > 254) {
				out.putByte(255);
				out.putMESmallInt(item.amount());
			} else {
				out.putByte(item.amount());
			}
			out.putLEShortA(item.getId() + 1);
		}
		out.finishShortSizedFrame();
		out.sendTo(player.channelContext().channel());
		return this;
	}

	
	public PacketDispatcher sendEntityLocation(Location location) {
		PacketBuilder out = PacketBuilder.allocate(3);
		out.createFrame(85, player.channelContext().encryption());
		out.putByteC(location.getY() - 8 * player.currentRegion().regionY());
		out.putByteC(location.getX() - 8 * player.currentRegion().regionX());
		out.sendTo(player.channelContext().channel());
		return this;
	}

	
	public PacketDispatcher sendGroundItem(GroundItem groundItem) {
		sendEntityLocation(groundItem.getLocation());
		PacketBuilder out = PacketBuilder.allocate(6);
		out.createFrame(44, player.channelContext().encryption());
		out.putLEShortA(groundItem.item().getId());
		out.putShort(groundItem.item().amount());
		out.putByte(0);
		out.sendTo(player.channelContext().channel());
		return this;
	}

	
	public PacketDispatcher sendRemoveGroundItem(GroundItem groundItem) {
		sendEntityLocation(groundItem.getLocation());
		PacketBuilder out = PacketBuilder.allocate(4);
		out.createFrame(156, player.channelContext().encryption());
		out.putByteS(0);
		out.putShort(groundItem.item().getId());
		out.sendTo(player.channelContext().channel());
		return this;
	}


	public PacketDispatcher sendObject(Object object, final boolean expiredObject) {
		sendEntityLocation(object.objectLocation);
		PacketBuilder out = PacketBuilder.allocate(6);
		out.createFrame(151, player.channelContext().encryption());
		out.putByteS(0);
		out.putLEShort(expiredObject ? object.replacementId : object.objectId);
		out.putByteS((10 << 2) + (0 & 3));
		out.sendTo(player.channelContext().channel());
		return this;
	}


	public PacketDispatcher sendRemoveObject(Object object) {
		sendEntityLocation(object.objectLocation);
		PacketBuilder out = PacketBuilder.allocate(4);
		out.createFrame(101, player.channelContext().encryption());
		out.putByteC((10 << 2) + (0 & 3));
		out.putByte(0);
		out.sendTo(player.channelContext().channel());
		return this;
	}


	public PacketDispatcher sendSkill(final int skill) {
		PacketBuilder out = PacketBuilder.allocate(16);
		out.createFrame(134, player.channelContext().encryption());
		out.putByte(skill);
		out.putMESmallInt((int) player.getSkill().getXP(skill));
		out.putByte(player.getSkill().getLevel(skill));
		out.sendTo(player.channelContext().channel());
		return this;
	}*/
        
        /**
         * Sends a music to the client
         * 
         * @param id
         *          this music ID
         * @return The packet sender
         */
        public PacketDispatcher sendSong(int id) {
            PacketBuilder out = PacketBuilder.allocate(3);
                out.createFrame(74, player.channelContext().encryption());
                out.putLEShort(id);
		out.sendTo(player.channelContext().channel());
                return this;
        }
        

	/**
	 * Gets the associated player
	 * 
	 * @return the associated player
	 */
	public Player player() {
		return player;
	}

}