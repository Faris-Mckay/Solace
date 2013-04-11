package org.solace.game.entity.mobile.player;

import org.solace.util.ProtocolUtils;

/**
 * 
 * @author Faris
 */
public class PlayerAuthentication {

	public PlayerAuthentication(String username, String password) {
		this.username = username;
		this.password = password;
		this.usernameAsLong = ProtocolUtils.nameToLong(username);
		this.playerRights = PrivilegeRank.STANDARD;
	}

	private String username;
	private String password;
	private long usernameAsLong;
	private PrivilegeRank playerRights;

	public void setUsername(String user) {
		this.username = user;
	}

	public void setPassword(String pass) {
		this.password = pass;
	}

	public void setPlayerRights(PrivilegeRank rank) {
		this.playerRights = rank;
	}

	public void setPlayerRights(int rank) {
		switch (rank) {
		case 0:
			this.playerRights = PrivilegeRank.STANDARD;
			break;
		case 1:
			this.playerRights = PrivilegeRank.MODERATOR;
			break;
		case 2:
			this.playerRights = PrivilegeRank.ADMINISTRATOR;
			break;
		case 3:
			this.playerRights = PrivilegeRank.OWNER;
			break;
		}
	}

	public int getPlayerRights() {
		switch (playerRights) {
		case MODERATOR:
			return 1;
		case ADMINISTRATOR:
			return 2;
		case OWNER:
			return 3;
		default:
			return 0;
		}
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the usernameAsLong
	 */
	public long getUsernameAsLong() {
		return usernameAsLong;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	public int[] appearanceIndex = new int[13];

	private boolean changeAppearance = false;

	public int playerGender() {
		return appearanceIndex[0];
	}

	public int playerHead() {
		return appearanceIndex[1];
	}

	public int playerTorso() {
		return appearanceIndex[2];
	}

	public int playerArms() {
		return appearanceIndex[3];
	}

	public int playerHands() {
		return appearanceIndex[4];
	}

	public int playerLegs() {
		return appearanceIndex[5];
	}

	public int playerFeet() {
		return appearanceIndex[6];
	}

	public int playerJaw() {
		return appearanceIndex[7];
	}

	public int playerHairColour() {
		return appearanceIndex[8];
	}

	public int playerTorsoColour() {
		return appearanceIndex[9];
	}

	public int playerLegColour() {
		return appearanceIndex[10];
	}

	public int playerFeetColour() {
		return appearanceIndex[11];
	}

	public int playerSkinColour() {
		return appearanceIndex[12];
	}

	public int getPlayerAppearanceIndex(int index) {
		return appearanceIndex[index];
	}

	public void setPlayerAppearanceIndex(int index, int appearance) {
		this.appearanceIndex[index] = appearance;
	}

	public boolean getChangeAppearance() {
		return changeAppearance;
	}

	public void setChangeAppearance(boolean status) {
		this.changeAppearance = status;
	}

	public enum PrivilegeRank {
		/**
		 * A standard (rights 0) account.
		 */
		STANDARD,

		/**
		 * A player moderator (rights 1) account.
		 */
		MODERATOR,

		/**
		 * An administrator (rights 2) account.
		 */
		ADMINISTRATOR,

		/**
		 * An owner (rights 3) account.
		 */
		OWNER;
	}

}
