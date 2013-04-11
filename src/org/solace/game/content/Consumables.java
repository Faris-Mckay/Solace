package org.solace.game.content;

import org.solace.game.content.skills.Skill;
import org.solace.game.entity.Animation;
import org.solace.game.entity.mobile.Mobile.WelfareStatus;
import org.solace.game.entity.mobile.player.Player;

/**
 * 
 * @author animeking1120
 * 
 */
public class Consumables {

	/**
	 * Contains all of the data for food
	 * @author animeking1120
	 * 
	 */
	private static enum Food {
		ANCHOVIES(319, 1, "Anchovies"), SHRIMP(315, 3, "Shrimps"), SARDINE(325,
                4, "Sardine"), HERRING(347, 5, "Herring"), TROUT(333, 7,
                "Trout"), PIKE(351, 8, "Pike"), SALMON(329, 9, "Salmon"), TUNA(
                361, 10, "Tuna"), LOBSTER(379, 12, "Lobster"), BASS(365, 13,
                "Bass"), SWORDFISH(373, 14, "Swordfish"), SHARK(385, 20,
                "Shark"), SEA_TURTLE(397, 21, "Sea Turtle"), MANTA_RAY(391, 22,
                "Manta Ray");

		int heal, itemId;
		String name;

		Food(int itemId, int heal, String name) {
			this.itemId = itemId;
			this.heal = heal;
			this.name = name;
		}

		int getHealAmount() {
			return heal;
		}

		int getItemId() {
			return itemId;
		}

		String getName() {
			return name;
		}

		private static Food forID(final int ID) {
			for (Food food : values()) {
				if (food.getItemId() == ID) {
					return food;
				}
			}
			return null;
		}
	}

	/**
	 * Controls eating the food
	 * @param player  the player eating the food
	 * @param itemId the item id the player is eating
	 */
	public static void eatFood(Player player, int itemId, int slot) {
		if (player.getStatus() == WelfareStatus.DEAD) {
			// we cannot eat because we are already dead
			return;
		}
            try {
                Food food = Food.forID(itemId);
                if (food != null) {
                    if (System.currentTimeMillis() - player.foodDelay > 1400) {
                        if (player.getInventory().contains(food.getItemId())) {
                            player.getPacketDispatcher().sendMessage("You eat the " + food.getName() + ".");
                            player.setAnimation(Animation.create(829, 0));
                            player.getInventory().delete(food.getItemId(), slot, 1);
                            int maxLevel = player.getSkills().getLevelForXP(player.getSkills().getPlayerExp()[Skill.HITPOINTS]) ;
                            if (player.getSkills().getPlayerLevel()[Skill.HITPOINTS]+ food.getHealAmount() > maxLevel) {
                                    player.getSkills().getPlayerLevel()[Skill.HITPOINTS] = maxLevel;
                            } else {
                                    player.getSkills().getPlayerLevel()[Skill.HITPOINTS] += food.getHealAmount();
                            }
                            player.getSkills().refreshSkill(Skill.HITPOINTS);
                            player.setHitDelay(player.getHitDelay() + 2);
                            player.getPacketDispatcher().sendMessage("It heals some health...");
                            player.foodDelay = System.currentTimeMillis();
                        }
                    }
                }
            } catch (Exception e) {
                    e.printStackTrace();
            }
	}
}
