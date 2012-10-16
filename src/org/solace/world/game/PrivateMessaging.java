package org.solace.world.game;

import java.util.ArrayList;
import java.util.List;
import org.solace.util.ProtocolUtils;
import org.solace.world.World;
import org.solace.world.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class PrivateMessaging {
    
    	private Player player;

	/**
	 * The list that holds all of the friends
	 */
	private List<Long> friends = new ArrayList<Long>();

	/**
	 * The list that holds all of the ignores
	 */
	private List<Long> ignores = new ArrayList<Long>();

	private int privateMessageId = 1;

	public int getPrivateMessageId() {
		return privateMessageId++;
	}

	public void setPrivateMessageId(int id) {
		privateMessageId = id;
	}

	/**
	 * Checks if the friends list contains a name
	 * 
	 * @param username
	 * @return
	 */
	public boolean friendsListContains(long username) {
		return friends.contains(ProtocolUtils.longToName(username));
	}

	/**
	 * Checks of the ignore list contains a name
	 * 
	 * @param username
	 * @return
	 */
	public boolean ignoreListContains(long username) {
		return ignores.contains(username);
	}

	/**
	 * Updates the friends list upon login so no connecting to friends server
	 * issue
	 */
	public void updateFriendsListStatus() {
		player.getPacketSender().sendPMServer(2);
		sendFriendsData();
	}

	/**
	 * sends the freshing of friends list
	 */
	public void sendFriendsData() {
		for (int i = 0; i < friends.size(); i++) {
			if (i == 0)
				continue;
			player.getPacketSender().sendFriendList(i, getPlayersWorld(i));
		}
		long name = player.getUsernameAsLong();
		int world = getPlayersWorld(name);
		for (Player p : World.worldRepository) {
			if (p == null)
				continue;
			if (p.getPrivateMessaging().friendsListContains(name)) {
				if (!p.getPrivateMessaging().ignoreListContains(name)) {
					p.getPacketSender().sendFriendList(name, world);
				}
			}
		}
	}

	/**
	 * Gets the size of the friends list
	 * 
	 * @return
	 */
	public int getFriendsCount() {
		return friends.size();
	}

	/**
	 * Gets the size of the ignore list
	 * 
	 * @return
	 */
	public int getIgnoreCount() {
		return ignores.size();
	}

	/**
	 * Gets the world the friend is on
	 * 
	 * @param friend
	 *            the friend your checking for
	 * @return
	 */
	private int getPlayersWorld(long friend) {
		for (Player p : World.worldRepository) {
			if (p != null) {
				if (p.getUsernameAsLong() == friend) {
					return 1;
				}
			}
		}
		return 0;
	}

	/**
	 * Adds a username to the friends list
	 * 
	 * @param name
	 */
	public void addToFriendsList(long name) {
		if (friends.size() >= 200) {
			player.getPacketSender().sendMessage("Your friends list is full.");
			return;
		}
		if (friends.contains(name)) {
			player.getPacketSender().sendMessage(
					ProtocolUtils.longToName(name)
							+ " is already on your friends list.");
			return;
		}
		friends.add(name);
		player.getPacketSender().sendFriendList(name, getPlayersWorld(name));
		sendFriendsData();
	}

	/**
	 * Removes a name from the friends list
	 * 
	 * @param name
	 *            the name to remove
	 */
	public void removeFromFriendsList(long name) {
		if (friends.contains(name)) {
			friends.remove(name);
			player.getPacketSender().sendFriendList(name, getPlayersWorld(name));
			sendFriendsData();
		}
	}

	/**
	 * Adds a username to the ignore list
	 * 
	 * @param name
	 */
	public void addToIgnoreList(long name) {
		if (getIgnoreCount() >= 200) {
			player.getPacketSender().sendMessage("Your ignore list is full.");
			return;
		}
		if (ignoreListContains(name)) {
			player.getPacketSender().sendMessage(
					ProtocolUtils.longToName(name)
							+ " is already on your ignore list.");
			return;
		}
		if (friendsListContains(name)) {
			player.getPacketSender().sendMessage(
					"Please remove " + ProtocolUtils.longToName(name)
							+ " from your friends list first.");
			return;
		}
		ignores.add(name);
		sendFriendsData();
	}

	/**
	 * Removes a name from the ignore list
	 * 
	 * @param name
	 */
	public void removeFromIgnoreList(long name) {
		if (!ignoreListContains(name))
			return;
		friends.remove(ProtocolUtils.longToName(name));
		sendFriendsData();
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

	/**
	 * Gets the friends list.
	 * 
	 * @return the friends list
	 */
	public List<Long> friends() {
		return friends;
	}

	/**
	 * Gets the ignores list.
	 * 
	 * @return the ignores list
	 */
	public List<Long> ignores() {
		return ignores;
	}

	/**
	 * Gets the associated player.
	 * 
	 * @return the associated player
	 */
	public Player player() {
		return player;
	}

	public void sendPm(Player player, long username, int size, byte[] message) {
		if (ProtocolUtils.longToName(username) == null) {
			player.getPacketSender().sendMessage("That player is offline.");
			return;
		}
		for (Player p : World.worldRepository) {
			if (p != null) {
				if (ProtocolUtils.nameToLong(p.getUsername()) == username) {
					p.getPacketSender().sendPrivateMessage(
							ProtocolUtils.nameToLong(player.getUsername()), 0,
							message, size);
				}
			}
		}
		sendFriendsData();
	}

}
