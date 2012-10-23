package org.solace.game.content.combat;

import org.solace.game.content.combat.impl.Melee;
import org.solace.game.content.combat.impl.Ranged;
import org.solace.game.entity.mobile.Mobile;

public abstract class Combat {

	public static void handleCombatTick(Mobile attacker) {
		if (attacker.getHitDelay() > 0) {
			attacker.setHitDelay(attacker.getHitDelay() - 1);
		} else {
			handleCombatStyle(attacker);
		}
	}

	public static void handleCombatStyle(Mobile attacker) {
		switch (attacker.getAttackType()) {
                    case MELEE:
                            Melee.getSingleton().handle(attacker);
                            break;
                    case RANGED:
                            Ranged.getSingleton().handle(attacker);
                            break;
                    default:
                            Melee.getSingleton().handle(attacker);
                            break;
		}
	}

	public abstract void handle(Mobile attacker);

	public abstract boolean checkRequirements(Mobile attacker, Mobile victim);

	public abstract int distanceRequired(Mobile attacker);
	
	public abstract int calculateDamage(Mobile attacker, Mobile victim);

	public static void resetCombat(Mobile attacker) {
		attacker.setInteractingEntity(null);
		attacker.setInteractingEntityIndex(-1);
		attacker.setInCombat(false);
	}

}
