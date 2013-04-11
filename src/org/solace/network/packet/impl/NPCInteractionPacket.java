package org.solace.network.packet.impl;

import org.solace.game.Game;
import org.solace.game.content.combat.Combat;
import org.solace.game.content.skills.thieving.Thieving;
import org.solace.game.entity.mobile.Mobile.AttackTypes;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.container.impl.Equipment;
import org.solace.game.map.Location;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.task.Task;

/**
 * 
 * @author Faris
 */
public class NPCInteractionPacket implements PacketHandler {

	public static final int FIRST_CLICK = 155;
	public static final int SECOND_CLICK = 17;
	public static final int THIRD_CLICK = 21;
	public static final int FOURTH_CLICK = 230;
	public static final int ATTACK = 72;
	public static final int MAGIC_ON_NPC = 131;
	public static final int ITEM_ON_NPC = 57;

	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.opcode()) {
		case ATTACK:
			handleNpcAttack(player, packet);
			break;
		case FIRST_CLICK:
			handleFirstClick(player, packet);
			break;
		case SECOND_CLICK:
			handleSecondClick(player, packet);
			break;
		case THIRD_CLICK:
			handleThirdClick(player, packet);
			break;
		case FOURTH_CLICK:
			handleFourthClick(player, packet);
			break;
		case MAGIC_ON_NPC:
			handleMagicOnNpc(player, packet);
			break;
		case ITEM_ON_NPC:
			handleItemOnNpc(player, packet);
			break;
		}
	}

	private void handleItemOnNpc(Player player, Packet packet) {
		// TODO Auto-generated method stub

	}

	private void handleMagicOnNpc(Player player, Packet packet) {
		int npcIndex = packet.getLEShortA();
		final int spellId = packet.getUShortA();
		NPC npc = Game.npcRepository.get(npcIndex);
		if (npc == null) {
			return;
		}
		player.setSpellId(spellId);
		player.setInteractingEntityIndex(npc.getIndex());
		player.setInteractingEntity(npc);
		player.setAttackType(AttackTypes.MAGIC);
		player.getUpdateFlags().faceEntity(npc.getIndex());
		Combat.handleCombatStyle(player);
	}

	private void handleFourthClick(Player player, Packet packet) {
		// TODO Auto-generated method stub

	}

	private void handleThirdClick(Player player, Packet packet) {
		// TODO Auto-generated method stub

	}

	private void handleSecondClick(final Player player, Packet packet) {
		int npcIndex = packet.getLEShortA();
		final NPC npc = Game.npcRepository.get(npcIndex);
		if (npc == null) {
			return;
		}
		Combat.resetCombat(player);
		player.setInteractingEntity(npc);
		player.setInteractingEntityIndex(npc.getIndex());
		final Location location = npc.getLocation();
		player.walkToAction(new Task(1, true) {
			boolean arrived = false;

			@Override
			public void execute() {
				if (arrived) {
					player.getUpdateFlags().faceEntity(npc.getIndex());
					switch (npc.getNpcId()) {
					default:
						player.getPacketDispatcher().sendMessage(
								"Npc ID: " + npc.getNpcId());
					}
					Thieving.handleNpcThieving(player, npc);
					player.walkToAction(null);
					stop();
				}
				if (player.getLocation().withinDistance(location, 2)) {
					arrived = true;
				}
			}
		});
		Game.submit(player.walkToAction());

	}

	private void handleFirstClick(final Player player, Packet packet) {
		int npcIndex = packet.getLEShort();
		final NPC npc = Game.npcRepository.get(npcIndex);
		if (npc == null) {
			return;
		}
		Combat.resetCombat(player);
		player.setInteractingEntity(npc);
		final Location location = npc.getLocation();
		player.walkToAction(new Task(1, true) {
			boolean arrived = false;

			@Override
			public void execute() {
				if (arrived) {
					player.getUpdateFlags().sendFaceToDirection(location);
					switch (npc.getNpcId()) {
					case 1:
						player.getDialogue().sendDialogue(1, npc.getNpcId());
						break;
					default:
						player.getDialogue().sendDialogue(3, npc.getNpcId());
						player.getPacketDispatcher().sendMessage(
								"Npc ID: " + npc.getNpcId());
					}
					player.walkToAction(null);
					stop();
				}
				if (player.getLocation().withinDistance(location, 2)) {
					arrived = true;
				}
			}
		});
		Game.submit(player.walkToAction());
	}

	private void handleNpcAttack(Player player, Packet packet) {
		int npcIndex = packet.getUShortA();
		NPC npc = Game.npcRepository.get(npcIndex);
		if (npc == null) {
			return;
		}
		player.getUpdateFlags().faceEntity(npc.getIndex());
		player.getPacketDispatcher().sendMessage(
				"Index: " + npc.getIndex() + " Id: " + npc.getNpcId());
		player.setAttackType(Equipment.isUsingRanged(player) ? AttackTypes.RANGED
				: player.getSpellId() != 0 ? AttackTypes.MAGIC
						: AttackTypes.MELEE);
		player.setInteractingEntity(npc);
		player.setInteractingEntityIndex(npc.getIndex());
		player.setInCombat(true);
		Combat.handleCombatStyle(player);
	}

}
