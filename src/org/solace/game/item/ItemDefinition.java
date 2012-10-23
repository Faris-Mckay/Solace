package org.solace.game.item;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.logging.Logger;

import org.solace.game.item.container.Equipment;
import org.solace.util.FileUtils;
import org.solace.util.ProtocolUtils;

/**
 * Manager for item definitions.
 * 
 * @author kLeptO <http://www.rune-server.org/members/klepto/>
 */
public class ItemDefinition {

	private static ItemDefinition[] definitions;

	private static Logger logger = Logger.getLogger(ItemDefinition.class
			.getName());

	/**
	 * Loads up the item definitions.
	 */
	public static void loadDefinitions() {
		logger.info("Loading item definitions...");
		int pointer = 0;
		definitions = new ItemDefinition[MAXIMUM_ITEM_INDEX + 1];
		try {
			DataInputStream in = new DataInputStream(new FileInputStream(
					DEFINITIONS_FILE));
			while (in.available() > 0) {
				ItemDefinition def = new ItemDefinition();
				def.index = in.readShort();
				def.name = ProtocolUtils.formatString(FileUtils.readString(in));
				def.description = FileUtils.readString(in);
				def.stackable = in.readByte() == 1;
				def.noteableIndex = in.readShort();
				if (def.noteableIndex != -1) {
					def.note = in.readByte() == 1;
				}
				def.storePrice = in.readInt();
				def.lowAlcValue = in.readInt();
				def.highAlcValue = in.readInt();
				def.equipmentSlot = in.readByte();
				if (def.equipmentSlot != -1) {
					for (int i = 0; i < def.bonus.length; i++) {
						def.bonus[i] = in.readByte();
					}
				}
				if (def.name().endsWith("2h sword")
						|| def.name().endsWith("hortbow")
						|| def.name().endsWith("ongbow")) {
					def.twoHanded = true;
				}
				definitions[pointer] = def;
				pointer++;
			}
			in.close();
		} catch (Exception e) {
			for (int i = pointer; i < definitions.length; i++) {
				definitions[i] = new ItemDefinition();
			}
			e.printStackTrace();
		}
	}

	/**
	 * Gets definition of item with specified index.
	 * 
	 * @param index
	 *            the item index
	 * 
	 * @return the item definition
	 */
	public static ItemDefinition get(int index) {
		if (index < 0) {
			return new ItemDefinition();
		}
		return definitions[index];
	}

	/**
	 * Checks if body is full body that is covering arms.
	 * 
	 * @param index
	 *            the item index
	 * 
	 * @return true if item is a full body
	 */
	public static boolean fullBody(int index) {
		for (int i = 0; i < Equipment.PLATEBODY.length; i++) {
			if (index == Equipment.PLATEBODY[i]) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if hat is covering whole head.
	 * 
	 * @param index
	 *            the item index
	 * 
	 * @return true if item is a full hat
	 */
	public static boolean fullHat(int index) {
		for (int i = 0; i < Equipment.FULL_HELM.length; i++) {
			if (index == Equipment.FULL_HELM[i]) {
				return true;
			}
		}
		return false;
	}

	private int index = -1;
	private String name = "null", description = "";
	private boolean stackable = false, note = false, twoHanded = false;
	private int noteableIndex = -1, storePrice = 0, lowAlcValue = 0,
			highAlcValue = 0, equipmentSlot = -1;
	private int[] bonus = new int[12];

	/**
	 * Gets the item index.
	 * 
	 * @return the item index
	 */
	public int index() {
		return index;
	}

	/**
	 * Gets the item name.
	 * 
	 * @return the item name
	 */
	public String name() {
		return name;
	}

	/**
	 * Gets the item description that is shown with examine option.
	 * 
	 * @return the item description
	 */
	public String description() {
		return description;
	}

	/**
	 * Checks if item is stackable.
	 * 
	 * @return true if item is stackable
	 */
	public boolean stackable() {
		return stackable;
	}

	/**
	 * Checks if this item is noted.
	 * 
	 * @return true if item is note
	 */
	public boolean note() {
		return note;
	}

	/**
	 * Checks if this item is two handed.
	 * 
	 * @return true if item is two handed
	 */
	public boolean twoHanded() {
		return twoHanded;
	}

	/**
	 * Gets corresponding notable item index, if this is noted item, returns
	 * non-noted index and vice versa.
	 * 
	 * @return the notable item index
	 */
	public int noteableIndex() {
		return noteableIndex;
	}

	/**
	 * Gets the shop price.
	 * 
	 * @return the shop price
	 */
	public int storePrice() {
		return storePrice;
	}

	/**
	 * Gets the low alchemy spell value.
	 * 
	 * @return the low alchemy value
	 */
	public int lowAlcValue() {
		return lowAlcValue;
	}

	/**
	 * Gets the high alchemy spell value.
	 * 
	 * @return the high alchemy value
	 */
	public int highAlcValue() {
		return highAlcValue;
	}

	/**
	 * Gets the equipment slot.
	 * 
	 * @return the equipment slot
	 */
	public int equipmentSlot() {
		return equipmentSlot;
	}

	/**
	 * Gets array of item bonuses.
	 * 
	 * @return the bonus array
	 */
	public int[] bonus() {
		return bonus;
	}

	/**
	 * Gets a single bonus value by a given index.
	 * 
	 * @param bonusIndex
	 *            the bonus index
	 * 
	 * @return the value of bonus
	 */
	public int bonus(int bonusIndex) {
		return bonus[bonusIndex];
	}

	public static final int MAXIMUM_ITEM_INDEX = 11740;
	public static final String DEFINITIONS_FILE = "./data/items/itemDefinitions.dat";

}