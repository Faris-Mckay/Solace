package org.solace.game.content;

import java.util.Iterator;

import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;
import org.solace.util.ProtocolUtils;

/**
 * 
 * @author Faris
 */
public class PrivateMessaging {

	private Player player;

	/**
	 * The list that holds all of the friends
	 */
	private long[] friends = new long[200];

	/**
	 * The list that holds all of the ignores
	 */
	private long[] ignores = new long[100];

	private int privateMessageId = 1;

	public int getPrivateMessageId() {
		return privateMessageId++;
	}

	public void setPrivateMessageId(int id) {
		privateMessageId = id;
	}

	public boolean contains(long[] person, long name) {
		for (int i = 0; i < person.length; i++) {
			if (person[i] == name) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Updates the friends list upon login so no connecting to friends server
	 * issue
	 */
	public void updateFriendsListStatus() {
		player.getPacketDispatcher().sendPMServer(2);
		refresh(false);
	}

	/**
	 * sends the freshing of friends list
	 */
	public void refresh(boolean logout) {
		for (int i = 0; i < getFriends().length; i ++) {
			if (getFriends()[i] == 0) {
				continue;
			} 
			player.getPacketDispatcher().sendFriendList(getFriends()[i], checkOnlineStatus(getFriends()[i]));
		}
        long name = player.getAuthentication().getUsernameAsLong();
		int world = logout ? 0 : checkOnlineStatus(name);
		for (Player players : Game.playerRepository.values()) {
			if (players == null)
				continue;
			if (players.getPrivateMessaging().contains(players.getPrivateMessaging().getFriends(), name)) {
				players.getPacketDispatcher().sendFriendList(name, world);
			}
		}
	}

	/**
	 * Creates a new private messaging manager.
	 * 
	 * @param player
	 *            the player reference
	 */
	public PrivateMessaging(Player player) {
		this.player = player;
	}
	
	public void setFriends(long[] friends) {
		this.friends = friends;
	}

	public long[] getFriends() {
		return friends;
	}
	
	public void setIgnores(long[] ignores) {
		this.ignores = ignores;
	}

	public long[] getIgnores() {
		return ignores;
	}


	public void sendPm(Player player, long username, int size, byte[] message) {
		if (ProtocolUtils.longToName(username) == null) {
			player.getPacketDispatcher().sendMessage("That player is offline.");
			return;
		}
		Iterator<Player> it = Game.playerRepository.values().iterator();
		while (it.hasNext()) {
			Player p = it.next();
			if (p != null) {
				if (p.getAuthentication().getUsernameAsLong() == username) {
					if (!p.getPrivateMessaging().isIgnoringPlayer(player.getAuthentication().getUsernameAsLong()))
					p.getPacketDispatcher().sendPrivateMessage(player.getAuthentication().getUsernameAsLong(), player.getAuthentication().getPlayerRights(), message, size);
				}
			}
		}
	}
	
	public void addToFriendsList(long name) {
		if (getCount(getFriends()) >= 200) {
			player.getPacketDispatcher().sendMessage("Your friends list is full.");
			return;
		}
		if (contains(getFriends(), name)) {
			player.getPacketDispatcher().sendMessage(""+ ProtocolUtils.longToName(name) + " is already on your friends list.");
			return;
		}
		int slot = getFreeSlot(getFriends());
		getFriends()[slot] = name;
		player.getPacketDispatcher().sendFriendList(name, checkOnlineStatus(name));
	}
	
	public void addToIgnoresList(long name) {
		if (getCount(getIgnores()) >= 100) {
			player.getPacketDispatcher().sendMessage("Your ignores list is full.");
			return;
		}
		if (contains(getIgnores(), name)) {
			player.getPacketDispatcher().sendMessage(""+ ProtocolUtils.longToName(name) + " is already on your ignores list.");
			return;
		}
		int slot = getFreeSlot(getIgnores());
		getIgnores()[slot] = name;
	}
	
	public boolean isIgnoringPlayer(long name) {
		for (long p : getIgnores()) {
			if (p == name) {
				return true;
			}
		}
		return false;
	}
	
	public void removeFromList(long[] person, long name) {
		for (int i = 0; i < person.length; i ++) {
			if (person[i] == name) {
				person[i] = 0;
				break;
			}
		}
	}

	private int checkOnlineStatus(long friend) {
		for (Player p : Game.playerRepository.values()) {
			if (p != null) {
				if (p.getAuthentication().getUsernameAsLong() == friend) {
					return 1;
				}
			}
		}
		return 0;
	}
	
	public int getCount(long[] name) {
		int count = 0;
		for (long names : name) {
			if (names > 0) {
				count ++;
			}
		}
		return count;
	}
	
	public int getFreeSlot(long[] person) {
		for (int i = 0; i < person.length; i ++) {
			if (person[i] == 0) {
				return i;
			}
		}
		return -1;
	}

}
