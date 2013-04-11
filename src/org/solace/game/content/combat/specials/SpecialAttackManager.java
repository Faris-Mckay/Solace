package org.solace.game.content.combat.specials;

import java.util.HashMap;
import java.util.Map;

import org.solace.game.content.combat.specials.impl.AbyssalWhip;
import org.solace.game.content.combat.specials.impl.DragonDagger;
import org.solace.game.entity.mobile.player.Player;

/**
 * Loads the special attacks
 * @author Xynth
 * @author Arithium
 * 
 */
public class SpecialAttackManager {

	private static Map<Integer, SpecialAttackHandler> specials = new HashMap<Integer, SpecialAttackHandler>();

	public static void loadSpecials() {
		specials.put(DragonDagger.WEAPON_ID, new DragonDagger());
		specials.put(AbyssalWhip.ABYSSAL_WHIP, new AbyssalWhip());
	}

	public static void handleSpecial(Player player, int weaponId) {
		SpecialAttackHandler specialAttackHandler = specials.get(weaponId);

		if (specialAttackHandler == null) {
			System.out.println("Special Attack is not handled Weapon Id = " + weaponId);
			return;
		}

		try {
			specialAttackHandler.handle(player, weaponId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public interface SpecialAttackHandler {
		public void handle(Player attacker, int weaponId);
	}

}
