/*
 * This file is part of Solace Framework.
 * Solace is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Solace is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Solace. If not, see <http://www.gnu.org/licenses/>.
 *
 */
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
