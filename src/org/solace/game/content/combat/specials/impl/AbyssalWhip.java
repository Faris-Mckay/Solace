package org.solace.game.content.combat.specials.impl;

import org.solace.game.content.combat.Combat;
import org.solace.game.content.combat.PrayerHandler.Prayer;
import org.solace.game.content.combat.impl.DelayedHit;
import org.solace.game.content.combat.impl.Hit;
import org.solace.game.content.combat.melee.MeleeCalculations;
import org.solace.game.content.combat.specials.SpecialAttackManager.SpecialAttackHandler;
import org.solace.game.entity.Animation;
import org.solace.game.entity.Graphic;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.player.Player;
import org.solace.util.ProtocolUtils;

/**
 * Represents the abyssal whip special attack
 * @author Arithium
 *
 */
public class AbyssalWhip implements SpecialAttackHandler {
	
	public static int ABYSSAL_WHIP = 4151;

	@Override
	public void handle(Player attacker, int weaponId) {
		Mobile victim = (Mobile) attacker.getInteractingEntity();
		if (attacker.getSpecialAmount() >= 50) {
			
			int damage = ProtocolUtils.random(MeleeCalculations.calculateMeleeMaxHit(attacker, true));
			
			if (ProtocolUtils.random(attacker.grabAttackBonus()) < ProtocolUtils.random(victim.grabDefenceBonus())) {
				damage = 0;
			}
			
			if (victim instanceof Player) {
				Player otherPlayer = (Player) victim;
				if (otherPlayer.isActivePrayer(Prayer.PROTECT_FROM_MELEE)) {
					damage = (int) (damage * 0.60);
				}
			}
			
			attacker.setAnimation(Animation.create(1658));
			
			victim.setGraphic(Graphic.lowGraphic(341, 0));
			
			DelayedHit.submit(attacker, victim, new Hit(damage, damage > 0 ? 1 : 0, 1, 1));
			
			attacker.setSpecialAmount(attacker.getSpecialAmount() - 50);
			
			Combat.addExperience(attacker, damage);
		} else {
			attacker.getPacketDispatcher().sendMessage("You don't have enough special.");
		}
		attacker.getEquipment().setUsingSpecial(false);
		attacker.getEquipment().sendWeapon(attacker);
		attacker.getEquipment().updateSpecialBar(attacker);
	}

}
