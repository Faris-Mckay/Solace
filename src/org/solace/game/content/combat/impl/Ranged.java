package org.solace.game.content.combat.impl;

import org.solace.game.content.combat.Combat;
import org.solace.game.entity.mobile.Mobile;

public class Ranged extends Combat {
	
	private static final Ranged ranged = new Ranged();
	
	public static Ranged getSingleton() {
		return ranged;
	}

	@Override
	public void handle(Mobile attacker) {
		Mobile victim = (Mobile) attacker.getInteractingEntity();
		if (victim == null)
			return;
		if (!checkRequirements(attacker, victim))
			return;
	}

	@Override
	public boolean checkRequirements(Mobile attacker, Mobile victim) {
		
		return true;
	}

	@Override
	public int distanceRequired(Mobile attacker) {

		return 7;
	}

	@Override
	public int calculateDamage(Mobile attacker, Mobile victim) {
		// TODO Auto-generated method stub
		return 0;
	}

}
