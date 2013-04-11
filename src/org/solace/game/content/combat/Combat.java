package org.solace.game.content.combat;

import org.solace.Server;
import org.solace.event.impl.AttackEvent;
import org.solace.game.content.combat.magic.Magic;
import org.solace.game.content.combat.melee.Melee;
import org.solace.game.content.combat.ranged.Ranged;
import org.solace.game.content.skills.Skill;
import org.solace.game.entity.Entity;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.Mobile.WelfareStatus;
import org.solace.game.entity.mobile.player.Player;

/**
 * Handles all of the combat actions
 * @author Arithium
 *
 */
public abstract class Combat {
	
	public static void handleCombatTick(Mobile attacker) {
		Entity victim = attacker.getInteractingEntity();
		if (victim == null) {
			resetCombat(attacker);
		}
		if (attacker.getStatus() == WelfareStatus.DEAD) {
			resetCombat(attacker);
			return;
		}
		if (System.currentTimeMillis() - attacker.getCombatDelay() > 5000) {
			resetCombat(attacker);
		}
		if (attacker.getHitDelay() > 0) {
			attacker.setHitDelay(attacker.getHitDelay() - 1);
		} else {
			if (attacker.isInCombat()) {
				Server.getEventManager().dispatchEvent(new AttackEvent(attacker));
			}
		}
	}

	public static void handleCombatStyle(Mobile attacker) {
		if (attacker.getStatus() == WelfareStatus.DEAD) {
			resetCombat(attacker);
			return;
		}
		switch (attacker.getAttackType()) {
		case MELEE:
			Melee.getSingleton().handle(attacker);
			break;
		case RANGED:
			Ranged.getSingleton().handle(attacker);
			break;
		case MAGIC:
			Magic.getMagic().handle(attacker);
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
		attacker.getUpdateFlags().faceEntity(0);
	}

	public static void addExperience(Mobile attacker, int damage) {
		Player player = (Player) attacker;
		switch (attacker.getAttackType()) {
		case MELEE:
			switch (attacker.getAttackStyle()) {
			case ACCURATE:
				player.getSkills().addSkillExp(Skill.ATTACK, damage * 4);
				break;
			case AGGRESSIVE:
				player.getSkills().addSkillExp(Skill.STRENGTH, damage * 4);
				break;
			case CONTROLLED:
				player.getSkills().addSkillExp(Skill.ATTACK, (damage * 1.3));
				player.getSkills().addSkillExp(Skill.STRENGTH, (damage * 1.3));
				player.getSkills().addSkillExp(Skill.DEFENCE, (damage * 1.3));
				break;
			case DEFENSIVE:
				player.getSkills().addSkillExp(Skill.DEFENCE, damage * 4);
				break;
			}
			player.getSkills().addSkillExp(Skill.HITPOINTS, damage * 3);
			break;
		case RANGED:
			switch (attacker.getAttackStyle()) {
			case ACCURATE:
				player.getSkills().addSkillExp(Skill.RANGED, damage * 4);
				break;
			case AGGRESSIVE:
				player.getSkills().addSkillExp(Skill.RANGED, damage * 4);
				break;
			case CONTROLLED:
				player.getSkills().addSkillExp(Skill.RANGED, damage * 1.3);
				player.getSkills().addSkillExp(Skill.DEFENCE, damage * 1.3);
				break;
			case DEFENSIVE:
				break;
			default:
				break;
			
			}
			player.getSkills().addSkillExp(Skill.HITPOINTS, damage * 3);
			break;
		case MAGIC:
			
			break;
		}
	}
}
