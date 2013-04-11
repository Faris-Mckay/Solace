package org.solace.game.item;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.logging.Logger;

import org.solace.game.content.skills.Skill;
import org.solace.game.item.container.impl.Equipment;
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
				def.req = getRequirements(def.name().toLowerCase(), pointer);
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
	private int[] req = new int[24];
	
	public static int[] getRequirements(String itemName, int itemId) {
		int[] req = new int[24];
		if (itemId == 4151) {
			req[0] = 70;
			return req;
		}
		if (itemName.contains("mystic") || itemName.contains("nchanted")) {
			if (itemName.contains("staff of light")) {
				req[Skill.MAGIC] = 75;
				req[Skill.ATTACK] = 75;
			}
			if (itemName.contains("staff")) {
				req[Skill.MAGIC] = 20;
				req[Skill.ATTACK] = 40;
			} else {
				req[Skill.MAGIC] = 20;
				req[Skill.DEFENCE] = 20;
			}
		}

		if (itemName.contains("infinity")) {
			req[Skill.MAGIC] = 50;
			req[Skill.DEFENCE] = 25;
		} else if (itemName.contains("splitbark")) {
			req[Skill.MAGIC] = 40;
			req[Skill.DEFENCE] = 40;
		} else if (itemName.contains("rune c'bow")) {
			req[Skill.RANGED] = 61;
		} else if (itemName.contains("black d'hide")) {
			req[Skill.RANGED] = 70;
		} else if (itemName.contains("tzhaar-ket-om")) {
			req[Skill.STRENGTH] = 60;
		} else if (itemName.contains("red d'hide")) {
			req[Skill.RANGED] = 60;
		} else if (itemName.contains("blue d'hide")) {
			req[Skill.RANGED] = 50;
		} else if (itemName.contains("green d'hide")) {
			req[Skill.RANGED] = 40;
		} else if (itemName.contains("snakeskin")) {
			req[Skill.RANGED] = 40;
			req[Skill.DEFENCE] = 30;
		} else if (itemName.contains("initiate")) {
			req[Skill.DEFENCE] = 20;
		} else if (itemName.contains("bronze")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				req[Skill.ATTACK] = req[Skill.DEFENCE] = 1;
			}

		} else if (itemName.contains("iron")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				req[Skill.ATTACK] = req[Skill.DEFENCE] = 1;
			}
//removing req
		} else if (itemName.contains("steel")) {
		} else if (itemName.contains("bolts")) {
		} else if (itemName.contains("beret")) {
		} else if (itemName.contains("arrow")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("scimitar")) {
				req[Skill.DEFENCE] = 0;
			} else if (itemName.contains("scimitar")) {
				req[Skill.ATTACK] = 5;
			}

		} else if (itemName.contains("black")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("vamb") && !itemName.contains("chap")
					&& !itemName.contains("scimitar")) {
				req[Skill.DEFENCE] = 10;
			} else if (itemName.contains("scimitar")) {
				req[Skill.ATTACK] = 10;
			}

		} else if (itemName.contains("mithril")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("scimitar")
					&& !itemName.contains("arrow")) {
				req[Skill.DEFENCE] = 20;
			} else if (itemName.contains("scimitar")) {
				req[Skill.ATTACK] = 20;
			}
		} else if (itemName.contains("polypore")) {
			req[Skill.MAGIC] = 80;
		} else if (itemName.contains("adamant")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("scimitar")) {
				req[Skill.DEFENCE] = 30;
			} else if (itemName.contains("scimitar")) {
				req[Skill.ATTACK] = 30;
			}

		} else if (itemName.contains("rune")) {
			if (!itemName.contains("knife") && !itemName.contains("dart")
					&& !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")
					&& !itemName.contains("'bow")
					&& !itemName.contains("arrow")
					&& !itemName.contains("scimitar")
					&& !itemName.contains("2h")) {
				req[Skill.DEFENCE] = 40;
			} else if (itemName.contains("scimitar") || itemName.contains("2h")) {
				req[Skill.ATTACK] = 40;
			}

		} else if (itemName.contains("granite shield")) {
			if (!itemName.contains("maul")) {
				req[Skill.DEFENCE] = 50;
			}

		} else if (itemName.contains("granite maul")) {
			if (!itemName.contains("shield")) {
				req[Skill.ATTACK] = 50;
			}

		} else if (itemName.contains("warrior")) {
			if (!itemName.contains("ring")) {
				req[Skill.DEFENCE] = 40;
			}

		} else if (itemName.contains("dragonfire")) {
			req[Skill.DEFENCE] = 75;
		} else if (itemName.contains("enchanted")) {
			req[Skill.DEFENCE] = 40;
		} else if (itemName.contains("d'hide")) {
			if (!itemName.contains("chaps")) {
				req[Skill.DEFENCE] = req[Skill.RANGED] = 40;
			}

		} else if (itemName.contains("dragon dagger")) {

			req[Skill.ATTACK] = 60;
		} else if (itemName.contains("drag dagger")) {

			req[Skill.ATTACK] = 60;
		} else if (itemName.contains("ancient")) {

			req[Skill.ATTACK] = 50;
		} else if (itemName.contains("hardleather")) {
			req[Skill.DEFENCE] = 10;
		} else if (itemName.contains("studded")) {
			req[Skill.DEFENCE] = 20;
		} else if (itemName.contains("bandos")) {
			if (!itemName.contains("godsword")) {
				req[Skill.STRENGTH] = req[Skill.DEFENCE] = 65;
			}
		} else if (itemName.contains("dragon")) {
			if (!itemName.contains("nti-") && !itemName.contains("fire")
					&& !itemName.contains("scimitar")
					&& !itemName.contains("claws")) {
				req[Skill.ATTACK] = req[Skill.DEFENCE] = 60;
			} else if (itemName.contains("scimitar")
					|| itemName.contains("claws")) {
				req[Skill.ATTACK] = 60;
			}
		} else if (itemName.contains("crystal")) {
			if (itemName.contains("shield")) {
				req[Skill.DEFENCE] = 70;
			} else {
				req[Skill.RANGED] = 70;
			}
		} else if (itemName.contains("torva")) {
			req[Skill.DEFENCE] = req[Skill.STRENGTH] = req[Skill.HITPOINTS] = 80;
		} else if (itemName.contains("pernix")) {
			req[Skill.DEFENCE] = req[Skill.RANGED] = req[Skill.HITPOINTS] = 80;
		} else if (itemName.contains("virtus")) {
			req[Skill.DEFENCE] = req[Skill.MAGIC] = req[Skill.HITPOINTS] = 80;
		} else if (itemName.contains("ahrim")) {
			if (itemName.contains("staff")) {
				req[Skill.MAGIC] = 70;
				req[Skill.ATTACK] = 70;
			} else {
				req[Skill.MAGIC] = 70;
				req[Skill.DEFENCE] = 70;
			}
		} else if (itemName.contains("karil")) {
			if (itemName.contains("crossbow")) {
				req[Skill.RANGED] = 70;
			} else {
				req[Skill.RANGED] = 70;
				req[Skill.DEFENCE] = 70;
			}
		} else if (itemName.contains("armadyl")) {
			if (itemName.contains("godsword")) {
				req[Skill.ATTACK] = 75;

			} else {
				if (!itemName.contains("staff")) {
					req[Skill.RANGED] = req[Skill.DEFENCE] = 65;
				}
			}
		} else if (itemName.contains("saradomin")) {
			if (itemName.contains("sword")) {
				req[Skill.ATTACK] = 70;
			}
			if (itemName.contains("crozier")) {
				req[Skill.ATTACK] = 1;
				if (itemName.contains("robe")) {
					req[Skill.ATTACK] = 1;

				} else {
					req[Skill.DEFENCE] = 40;

				}
			}
		} else if (itemName.contains("godsword")) {
			req[Skill.ATTACK] = 75;
		} else if (itemName.contains("3rd age") && !itemName.contains("amulet")) {
			req[Skill.DEFENCE] = 60;
		} else if (itemName.contains("verac") || itemName.contains("guthan")
				|| itemName.contains("dharok") || itemName.contains("torag")) {
			if (itemName.contains("hammers")) {
				req[Skill.ATTACK] = 70;
				req[Skill.STRENGTH] = 70;
			} else if (itemName.contains("axe")) {
				req[Skill.ATTACK] = 70;
				req[Skill.STRENGTH] = 70;
			} else if (itemName.contains("warspear")) {
				req[Skill.ATTACK] = 70;
				req[Skill.STRENGTH] = 70;
			} else if (itemName.contains("flail")) {
				req[Skill.ATTACK] = 70;
				req[Skill.STRENGTH] = 70;
			} else {
				req[Skill.DEFENCE] = 70;
			}
		}
		switch (itemId) {
		case 19478:
			req[Skill.ATTACK] = 80;
			break;
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			req[Skill.ATTACK] = 42;
			req[Skill.RANGED] = 42;
			req[Skill.STRENGTH] = 42;
			req[Skill.MAGIC] = 42;
			req[Skill.DEFENCE] = 42;
			break;
		case 6528:
			req[Skill.STRENGTH] = 50;
			break;
		case 10551:
		case 2503:
		case 2501:
		case 2499:
		case 1135:
			req[Skill.DEFENCE] = 40;
			break;
		case 11235:
		case 6522:
			req[Skill.RANGED] = 60;
			break;
		case 6524:
			req[Skill.DEFENCE] = 60;
			break;
		case 11284:
		case 11283:
			req[Skill.DEFENCE] = 75;
			break;
		case 6889:
		case 6914:
			req[Skill.MAGIC] = 60;
			break;
		case 13905:
		case 13899:
		case 13902:
			req[Skill.ATTACK] = 78;
			break;
		case 13893: // Pvp armor
		case 13877:
		case 13890:
		case 13884:
		case 13896:
			req[Skill.DEFENCE] = 78;
			break;

		case 13876:
		case 13870:
		case 13873:
			req[Skill.DEFENCE] = 78;
			break;
			
		case 13944:
		case 13950:
		case 13947:
			req[Skill.DEFENCE] = 20;
			break;

		case 13858:
		case 13862:
		case 13864:
			req[Skill.MAGIC] = 78;
			req[Skill.DEFENCE] = 78;
			break;

		case 13736:
		case 13734:
		case 13740:
		case 13742:
		case 13738:
		case 13744:
			req[Skill.DEFENCE] = 75;
			break;

		case 13882:
		case 13881:
		case 13880:
		case 13879:
		case 13883:
			req[Skill.RANGED] = 78;
			break;

		case 861:
			req[Skill.RANGED] = 50;
			break;
		case 20171:
			req[Skill.RANGED] = 80;
			break;

		case 10828:
			req[Skill.DEFENCE] = 55;
			break;
		case 11724:
		case 11726:
		case 11728:
			req[Skill.DEFENCE] = 65;
			break;
		case 3751:
		case 3749:
		case 3755:
			req[Skill.DEFENCE] = 40;
			break;

		case 7462:
		case 7461:
			req[Skill.DEFENCE] = 40;
			break;
		case 8846:
			req[Skill.DEFENCE] = 5;
			break;
		case 8847:
			req[Skill.DEFENCE] = 10;
			break;
		case 8848:
			req[Skill.DEFENCE] = 20;
			break;
		case 8849:
			req[Skill.DEFENCE] = 30;
			break;

		case 8850:
			req[Skill.DEFENCE] = 40;
			break;
		case 20072:
			req[Skill.DEFENCE] = 60;
			break;
		case 10887:
			req[Skill.STRENGTH] = 60;
			break;

		case 7460:
			req[Skill.DEFENCE] = 20;
			break;

		case 15071:
			req[Skill.DEFENCE] = 60;
			break;

		case 837:
			req[Skill.RANGED] = 61;
			break;

		case 15441:
		case 15442:
		case 15443:
		case 15444:
		case 4151: // if you don't want to use names
			req[Skill.ATTACK] = 70;
			break;

		case 6724: // seercull
			req[Skill.RANGED] = 60; // idk if that is correct
			break;

		case 1319: // rune 2h
			req[Skill.ATTACK] = 40;
			break;

		case 14484: // dclaw
			req[Skill.ATTACK] = 60;
			break;

		case 15241: // hand cannon
			req[Skill.RANGED] = 61;
			break;

		case 18349: // c rapier
			req[Skill.ATTACK] = 80;
			break;

		case 18351: // cls
			req[Skill.ATTACK] = 80;
			break;

		case 18353: // c maul
			req[Skill.ATTACK] = 80;
			break;
		case 16425:
			req[Skill.STRENGTH] = 99;
			break;

		case 18355: // c staff
			req[Skill.ATTACK] = 80;
			req[Skill.MAGIC] = 80;
			break;
			
		case 19476: // c staff
			req[Skill.MAGIC] = 80;
			break;

		case 18357: // c cbow
			req[Skill.RANGED] = 80;
			break;

		case 15042: // eagle-eye
			req[Skill.RANGED] = 80;
			req[Skill.DEFENCE] = 80;
			break;

		case 15043: // c kite
			req[Skill.DEFENCE] = 80;
			break;

		case 4153:
			req[Skill.ATTACK] = 50;
			req[Skill.STRENGTH] = 50;
			break;
		}
		return req;
	}

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

	public int[] getReq() {
		return req;
	}

	public void setReq(int[] req) {
		this.req = req;
	}

	public static final int MAXIMUM_ITEM_INDEX = 11740;
	public static final String DEFINITIONS_FILE = "./data/items/itemDefinitions.dat";

}