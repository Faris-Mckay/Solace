package org.solace.game.content.combat.ranged;

import org.solace.game.content.combat.Combat;
import org.solace.game.content.combat.PrayerHandler.Prayer;
import org.solace.game.content.combat.impl.DelayedHit;
import org.solace.game.content.combat.impl.FireProjectile;
import org.solace.game.content.combat.impl.Hit;
import org.solace.game.content.combat.impl.Projectile;
import org.solace.game.content.combat.ranged.RangedDefinitions.RangedWeaponLoader;
import org.solace.game.content.combat.specials.SpecialAttackManager;
import org.solace.game.entity.Animation;
import org.solace.game.entity.Graphic;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.Mobile.WelfareStatus;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.container.impl.Equipment;
import org.solace.util.ProtocolUtils;

/**
 * Represents the ranged combat style
 * @author Arithium
 *
 */
public class Ranged extends Combat {

	private static final Ranged ranged = new Ranged();

	public static Ranged getSingleton() {
		return ranged;
	}

	@Override
	public void handle(Mobile attacker) {
		/*
		 * Create an instance of the victim
		 */
		Mobile victim = (Mobile) attacker.getInteractingEntity();

		/*
		 * Check all requirements before attacking
		 */
		if (!checkRequirements(attacker, victim)) {
			return;
		}
		/*
		 * Check that the attackers hit delay is at least 0
		 */
		if (attacker.getHitDelay() <= 0) {

			attacker.getUpdateFlags().faceEntity(victim.getIndex() + ((victim instanceof Player) ? 32768 : 0));
			/*
			 * First we do the player stuff since its different then npcs
			 */
			if (attacker instanceof Player) {
				/*
				 * Create an instance of a player for the attacker
				 */
				final Player player = (Player) attacker;
				/*
				 * Define the arrow by getting the arrow slot
				 */
				int arrow = player.getEquipment().get(Equipment.AMMUNITION_SLOT).getIndex();

				int arrowAmount = player.getEquipment().get(Equipment.AMMUNITION_SLOT).getAmount();
				/*
				 * Check if the player actually has arrows, if not reset the
				 * attack
				 */
				if (arrowAmount <= 0) {
					player.getPacketDispatcher().sendMessage("You have run out of arrows.");
					Combat.resetCombat(player);
					return;
				}
				/*
				 * Now we create an instance the range definitions
				 */
				RangedDefinitions def = RangedWeaponLoader.getRangedWeapon(arrow);

				/*
				 * We set the players animation
				 */
				player.setAnimation(Animation.create(player.grabAttackAnimation()));
				/*
				 * We set the drawback graphic
				 */
				player.setGraphic(Graphic.highGraphic(def.getDrawBack(), 0));

				/*
				 * Now we delete the arrow TODO:figure out the refreshing
				 * problem
				 */
				player.getEquipment().set(Equipment.AMMUNITION_SLOT, arrowAmount > 1 ? arrow : -1, (arrowAmount > 1) ? (arrowAmount - 1) : 0);
				player.getEquipment().refreshItems();
				/*
				 * Now we define the projectile data so the projectile shoots
				 * properly
				 */
				/*
				 * The offsetX of the projectile path
				 */
				int offsetX = (player.getLocation().getX() - victim.getLocation().getX()) * -1;
				/*
				 * The offsetY of the projectiles path
				 */
				int offsetY = (player.getLocation().getY() - victim.getLocation().getY()) * -1;
				/*
				 * The entities index we are locking on too
				 */
				int projectileLockOnIndex = (victim instanceof Player ? (-victim.getIndex() - 1) : (victim.getIndex() + 1));

				/*
				 * After defining the projectile info we can now send it
				 */
				FireProjectile.submit(new Projectile(player.getLocation(), offsetX, offsetY, def.getProjectile(), 43, 31, 60, projectileLockOnIndex));

				/*
				 * We send the delayed hit to the victim
				 */
				int damage = calculateDamage(player, victim);
				
				if (victim instanceof Player) {
					Player otherPlayer = (Player) victim;
					if (otherPlayer.isActivePrayer(Prayer.PROTECT_FROM_MISSILE)) {
						damage = (int) (damage * 0.60);
					}
				}
				if (player.getEquipment().isUsingSpecial()) {
					SpecialAttackManager.handleSpecial(player, player.getEquipment().getItemBySlot(3));
				} else {
					DelayedHit.submit(player, victim, new Hit(damage, damage > 0 ? 1 : 0, 1, 3));
				}
				
				super.addExperience(attacker, damage);
			}

			/*
			 * Finally reset the attackers hit delay
			 */
			victim.setCombatDelay(System.currentTimeMillis());
			attacker.setHitDelay(attacker.grabAttackSpeed());
		}
	}

	@Override
	public boolean checkRequirements(Mobile attacker, Mobile victim) {
		/*
		 * Check if the victim is null or dead
		 */
		if (victim == null || victim.getStatus() == WelfareStatus.DEAD) {
			Combat.resetCombat(attacker);
			return false;
		}
		/*
		 * Check if the attacker is dead
		 */
		if (attacker.getStatus() == WelfareStatus.DEAD) {
			Combat.resetCombat(attacker);
			return false;
		}
		if (attacker.getLocation().withinDistance(victim.getLocation(), distanceRequired(attacker))) {
			attacker.getMobilityManager().stopMovement();
		} else {
			return false;
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
				if (ProtocolUtils.random(RangedCalculations.calculateRangeAttack(player)) > ProtocolUtils.random(RangedCalculations.calculateRangeDefence(otherPlayer))) {
					return ProtocolUtils.random(RangedCalculations.calculateRangedMaxHit(player, false));
				}
			} else if (victim instanceof NPC) {
				NPC npc = (NPC) victim;
				if (ProtocolUtils.random(RangedCalculations.calculateRangeAttack(player)) > ProtocolUtils.random(npc.getDefinition().getDefenceRange())) {
					return ProtocolUtils.random(RangedCalculations.calculateRangedMaxHit(player, false));
				}
			}
		}
		return 0;
	}

}
