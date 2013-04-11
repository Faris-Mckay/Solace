package org.solace.game.content.combat.impl;

import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.Mobile;

/**
 * Sends an instant hit
 * 
 * @author Arithium
 * 
 */
public class InstantHit {

	public static void sendInstantHit(Mobile victim, Hit hit) {
		if (hit.getHitmask() == 1) {
			victim.hit(hit);
			victim.getUpdateFlags().flag(UpdateFlag.HIT);
		} else {
			victim.hit(hit);
			victim.getUpdateFlags().flag(UpdateFlag.HIT_2);
		}
	}

}
