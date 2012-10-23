package org.solace.game.content.combat;

import org.solace.game.entity.Animation;
import org.solace.game.entity.Graphic;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.util.ProtocolUtils;

public class WeaponSpecialHandler extends Combat {

	private static WeaponSpecialHandler weapon = new WeaponSpecialHandler();

	public static WeaponSpecialHandler getSingleton() {
		return weapon;
	}

	@Override
	public void handle(Mobile attacker) {
		Mobile victim = (Mobile) attacker.getInteractingEntity();
		if (!checkRequirements(attacker, victim))
			return;
		Player player = (Player) attacker;
		WeaponSpecialDefinition def = WeaponSpecialDefinition.getWeapons().get(
				player.getEquipment().get(3));
		if (def == null)
			return;
		player.setAnimation(Animation.create(def.getAttackAnimation()));
		player.getUpdateFlags().flag(UpdateFlag.ANIMATION);

		player.setGraphic(Graphic.lowGraphic(def.getAttackGfx(), 0));
		player.getUpdateFlags().flag(UpdateFlag.GRAPHICS);
		
		SendDelayedHit.sendDelayedHit(player, victim, new DelayedAttack(calculateDamage(player, victim), 0, 1));
	}

	@Override
	public boolean checkRequirements(Mobile attacker, Mobile victim) {

		return true;
	}

	@Override
	public int distanceRequired(Mobile attacker) {
		return 2;
	}

	@Override
	public int calculateDamage(Mobile attacker, Mobile victim) {
		/*
		 * Calculates the damage a player can deal to the victim
		 */
		if (attacker instanceof Player) {
			/*
			 * Creates a player instance of the attacker
			 */
			Player player = (Player) attacker;
			
			/*
			 * Checks if the victim is an npc so we can grab the calculations
			 */
			if (victim instanceof NPC) {
				/*
				 * Creates an npc instance of the victim so we don't have to cast NPC
				 */
				NPC npc = (NPC) victim;
				/*
				 * Checks if the players attack bonus is greater then the npcs defence bonus
				 */
				if ((Math.random() * player.grabAttackBonus()) > (Math.random() * npc.grabDefenceBonus())) {
					return (int) (Math.random() * Calculations.calculateMeleeMaxHit(player, false));
				}
			} else if (victim instanceof Player) {
				/*
				 * Creates a player instance of the victim so we don't have to cast player
				 */
				Player otherPlayer = (Player) victim;
				
				if (((Math.random() * player.grabAttackBonus()) + Math.random() * 5) > ((Math.random() * otherPlayer.grabDefenceBonus()) + Math.random() * 5)) {
					return ProtocolUtils.random(Calculations.calculateMeleeMaxHit(player, false));
				}
			}
		}
		/*
		 * If none of the requirements are met, set the damage to 0
		 */
		return 0;
	}

}
