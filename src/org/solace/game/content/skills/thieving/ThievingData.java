package org.solace.game.content.skills.thieving;

public class ThievingData {
	
	/**
	 * The npcs ids
	 */
	private int npcId;
	
	/**
	 * The level required to steal from the npc
	 */
	private int levelRequired;
	
	/**
	 * The list of items your able to obtain
	 */
	private int[] items;
	
	/**
	 * The amount of the item you'll obtain
	 */
	private int amount;
	
	/**
	 * The amount of experience you'll get 
	 */
	private int experience;
	
	/**
	 * The forced message sent by the npc upon failing
	 */
	private String forcedText;
	
	/**
	 * Returns the npcs id
	 * @return
	 */
	public int getNpcId() {
		return npcId;
	}
	
	/**
	 * returns the level required to steal from the npc
	 * @return
	 */
	public int getLevelRequired() {
		return levelRequired;
	}
	
	/**
	 * returns the list of items possible to obtain
	 * @return
	 */
	public int[] getItems() {
		return items;
	}
	
	/**
	 * Returns the amount 
	 * @return
	 */
	public int getAmount() {
		return amount;
	}
	
	public int getExperience() {
		return experience;
	}
	
	/**
	 * Returns the message the npc will send
	 * @return
	 */
	public String getForcedText() {
		return forcedText;
	}

}
