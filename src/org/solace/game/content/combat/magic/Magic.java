package org.solace.game.content.combat.magic;

import org.solace.Server;
import org.solace.event.Event;
import org.solace.game.content.combat.Combat;
import org.solace.game.content.combat.PrayerHandler.Prayer;
import org.solace.game.content.combat.impl.DelayedHit;
import org.solace.game.content.combat.impl.FireProjectile;
import org.solace.game.content.combat.impl.Hit;
import org.solace.game.content.combat.impl.Projectile;
import org.solace.game.content.combat.magic.MagicDefinitions.MagicSpellLoader;
import org.solace.game.content.skills.Skill;
import org.solace.game.entity.Animation;
import org.solace.game.entity.Graphic;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.Mobile.WelfareStatus;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.game.item.container.impl.Equipment;
import org.solace.util.ProtocolUtils;

/**
 * Represents the magic combat style
 * @author Arithium
 *
 */
public class Magic extends Combat {

	private static Magic magic = new Magic();

	public static Magic getMagic() {
		return magic;
	}

	MagicDefinitions spell = null;

	@Override
	public void handle(Mobile attacker) {
		/*
		 * First we create a mobile instance of the victim
		 */
		Mobile victim = (Mobile) attacker.getInteractingEntity();
		/*
		 * Now we check requirements
		 */
		if (!checkRequirements(attacker, victim)) {
			// requirements weren't met so we can't attack
			return;
		}

		/*
		 * Now we check if the attackers hit delay is 0 to prevent spam
		 * attacking
		 */
		if (attacker.getHitDelay() <= 0) {

			/*
			 * First we face the victim
			 */
			attacker.getUpdateFlags().faceEntity(victim.getIndex() + ((victim instanceof Player) ? 32768 : 0));

			/*
			 * Since players and npcs magic is different we seperate the 2
			 */
			if (attacker instanceof Player) {
				/*
				 * Creates a player instance of the attacker
				 */
				Player player = (Player) attacker;

				int[] runes = spell.getRunes();
				int[] runesAmount = spell.getRunesAmount();
				for (int i = 0; i < runes.length; i++) {
					if (Equipment.hasRuneAsStaff(player, runes[i])) {
						continue;
					}
					player.getInventory().delete(
							new Item(runes[i], runesAmount[i]));
				}

				/*
				 * Now we perform the players animation
				 */
				player.setAnimation(Animation.create(spell.getAnimation()));

				player.setGraphic(Graphic.highGraphic(
						spell.getStartProjectile(), 0));

				int offsetX = (player.getLocation().getX() - victim
						.getLocation().getX()) * -1;
				int offsetY = (player.getLocation().getY() - victim
						.getLocation().getY()) * -1;
				int projectileLockOnIndex = victim instanceof Player ? -victim
						.getIndex() - 1 : victim.getIndex() + 1;
				FireProjectile.submit(new Projectile(player
						.getLocation(), offsetX, offsetY,
						spell.getProjectile(), 43, 31,
						calculateProjectileSpeed(attacker, victim),
						projectileLockOnIndex));

				victim.setGraphic(Graphic.lowGraphic(spell.getEndProjectile(),
						0));

				if (!player.getSettings().isAutoCasting()) {
					player.setSpellId(0);
					Combat.resetCombat(player);
				}
			}

			int delay = calculateDamageDelay(attacker, victim);
			int damage = calculateDamage(attacker, victim);
			System.out.println(damage);
			if (victim instanceof Player) {
				Player otherPlayer = (Player) victim;
				if (otherPlayer.isActivePrayer(Prayer.PROTECT_FROM_MAGIC)) {
					damage = (int) (damage * 0.60);
				}
			}
			DelayedHit.submit(attacker, victim, new Hit(damage, damage > 0 ? 1
					: 0, 1, delay));

			victim.setAnimation(Animation.create(victim.getBlockAnimation(),
					100));

			if (getFreezeSpell(attacker)) {
				freezeOpponent(attacker, victim);
			}

			/*
			 * Now we reset the attackers hit delay
			 */
			victim.setCombatDelay(System.currentTimeMillis());
			attacker.setHitDelay(attacker.grabAttackSpeed());
		}
	}

	public static int calculateProjectileSpeed(Mobile attacker, Mobile victim) {
		switch (ProtocolUtils.getDistance(attacker.getLocation(),
				victim.getLocation())) {
		case 1:
			return 60;
		case 2:
			return 70;
		case 3:
			return 80;
		case 4:
		case 5:
			return 90;
		case 6:
			return 95;
		case 7:
			return 100;
		default:
			return 110;
		}
	}

	public int calculateDamageDelay(Mobile attacker, Mobile victim) {
		switch (ProtocolUtils.getDistance(attacker.getLocation(),
				victim.getLocation())) {
		case 1:
		case 2:
		case 3:
			return 3;
		case 4:
		case 5:
		case 6:
			return 4;
		default:
			return 5;
		}
	}

	@Override
	public boolean checkRequirements(Mobile attacker, Mobile victim) {
		/*
		 * Check if we are dead first
		 */
		if (attacker.getStatus() == WelfareStatus.DEAD) {
			super.resetCombat(attacker);
			return false;
		}
		/*
		 * Check if our victim is null or dead
		 */
		if (victim == null || victim.getStatus() == WelfareStatus.DEAD) {
			super.resetCombat(attacker);
			return false;
		}
		
		if (attacker instanceof Player) {
			Player player = (Player) attacker;
			/*
			 * Creates the magic definitions instance
			 */
			spell = MagicSpellLoader.getSpell(player.getSpellId());

			if (spell == null) {
				super.resetCombat(attacker);
				return false;
			}
			/*
			 * Creates the runes and rune amount instances
			 */
			int[] runes = spell.getRunes();
			int[] runesAmount = spell.getRunesAmount();

			/*
			 * Loops through the rune requirements
			 */
			for (int i = 0; i < runes.length; i++) {
				/*
				 * Checks if the rune requirement == 0 or the player has a staff
				 * of the rune element
				 */
				if (runes[i] == 0 || Equipment.hasRuneAsStaff(player, runes[i])) {
					continue;
				}
				/*
				 * Creates an item for the rune istance
				 */
				Item rune = player.getInventory().getById(runes[i]);
				/*
				 * Checks if the player has the required runes
				 */
				if (rune == null || rune.getAmount() < runesAmount[i]) {
					player.getPacketDispatcher().sendMessage(
							"You do not have enough runes to cast this spell.");
					super.resetCombat(attacker);
					return false;
				}
			}
			/*
			 * Checks if the player has the required level to cast the spell
			 */
			if (player.getSkills().getPlayerLevel()[Skill.MAGIC] < spell
					.getLevel()) {
				player.getPacketDispatcher().sendMessage(
						"You need a magic level of " + spell.getLevel()
								+ " to cast this spell.");
				super.resetCombat(attacker);
				return false;
			}
		}
		/*
		 * Check if were in distance
		 */
		if (!attacker.getLocation().withinDistance(victim.getLocation(),
				distanceRequired(attacker))) {
			return false;
		} else {
			/*
			 * Stop the players movement
			 */
			attacker.getMobilityManager().stopMovement();
		}
		return true;
	}

	@Override
	public int distanceRequired(Mobile attacker) {
		return 7;
	}

	@Override
	public int calculateDamage(Mobile attacker, Mobile victim) {
		if (attacker instanceof Player) {
			Player player = (Player) attacker;
			if (victim instanceof Player) {
				Player otherPlayer = (Player) victim;
				if (ProtocolUtils.random(MagicCalculations
						.calculateMagicAttack(player)
						+ ProtocolUtils.random(10)) > ProtocolUtils
						.random(MagicCalculations
								.calculateMagicDefence(otherPlayer))
						+ ProtocolUtils.random(10)) {
					return ProtocolUtils.random(spell.getMaximumHit()
							+ MagicCalculations.magicMaxHitModifier(player));
				}
			} else if (victim instanceof NPC) {
				NPC npc = (NPC) victim;
				int random1 = ProtocolUtils.random(MagicCalculations
						.calculateMagicAttack(player)
						+ ProtocolUtils.random(10));
				int random2 = ProtocolUtils.random(npc.getDefinition()
						.getDefenceMage() + ProtocolUtils.random(10));
				if (random1 > random2) {
					return ProtocolUtils.random(spell.getMaximumHit()
							+ MagicCalculations.magicMaxHitModifier(player));
				}
			}
		}

		return 0;
	}

	private boolean getFreezeSpell(Mobile attacker) {
		if (attacker instanceof Player) {
			switch (spell.getAnimation()) {
			case 1979:
				return true;
			}
		}
		return false;
	}

	private int getFreezeDelay(Mobile attacker) {
		if (attacker instanceof Player) {
			switch (spell.getAnimation()) {
			case 1979:
				return 33;
			}
		}
		return 0;
	}

	private void freezeOpponent(final Mobile attacker, final Mobile victim) {
		if ((Boolean) victim.getAttribute("FROZEN")) {
			if (attacker instanceof Player) {
				((Player) attacker).getPacketDispatcher()
						.sendMessage("frozen.");
			}
			// we return because the victim is already frozen
			return;
		}
		if ((Boolean) victim.getAttribute("IMMUNE")) {
			if (attacker instanceof Player) {
				((Player) attacker).getPacketDispatcher()
						.sendMessage("Immune.");
			}
			// return as the player is immune
			return;
		}
		victim.getMobilityManager().stopMovement();
		if (victim instanceof Player) {
			((Player) victim).getPacketDispatcher().sendMessage(
					"You have been frozen.");
		}
		victim.addAttribute("FROZEN", Boolean.TRUE);
		Server.getService().schedule(new Event(getFreezeDelay(attacker)) {
			@Override
			public void execute() {
				victim.addAttribute("FROZEN", Boolean.FALSE);
				sendImmuneEvent(victim);
				this.stop();
			}
		});
	}

	private void sendImmuneEvent(final Mobile victim) {
		victim.addAttribute("IMMUNE", Boolean.TRUE);
		Server.getService().schedule(new Event(7) {
			@Override
			public void execute() {
				victim.addAttribute("IMMUNE", Boolean.FALSE);
				this.stop();
			}
		});
	}

}
