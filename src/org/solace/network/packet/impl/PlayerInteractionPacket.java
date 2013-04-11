package org.solace.network.packet.impl;

import org.solace.Server;
import org.solace.event.impl.EntityFollowService;
import org.solace.game.Game;
import org.solace.game.content.combat.Combat;
import org.solace.game.entity.mobile.Mobile.AttackTypes;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.container.impl.Equipment;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;

public class PlayerInteractionPacket implements PacketHandler {

	public static final int FOLLOW = 39;
	public static final int ATTACK = 73;
	public static final int MAGIC_ON_PLAYER = 249;
	public static final int USE_ITEM_ON_PLAYER = 14;

	@Override
	public void handlePacket(Player player, Packet packet) {
		switch (packet.opcode()) {
		case ATTACK:
			handleAttackOption(player, packet);
			break;
		case FOLLOW:
			handleFollowOption(player, packet);
			break;
		case MAGIC_ON_PLAYER:
			handleMagicOnPlayer(player, packet);
			break;
		}

	}

	private void handleMagicOnPlayer(Player player, Packet packet) {
		int playerIndex = packet.getUShortA();
		int castingSpell = packet.getLEShort();
		Player otherPlayer = Game.playerRepository.get(playerIndex);
		if (otherPlayer == null)
			return;
		player.setInteractingEntity(otherPlayer);
		player.setInteractingEntityIndex(otherPlayer.getIndex());
		player.setAttackType(AttackTypes.MAGIC);
		player.setSpellId(castingSpell);
		Combat.handleCombatStyle(player);
	}

	private void handleAttackOption(Player player, Packet packet) {
		int playerIndex = packet.getLEShort();
		Player otherPlayer = Game.playerRepository.get(playerIndex);
		if (otherPlayer == null)
			return;
		player.setInteractingEntity(otherPlayer);
		player.setInteractingEntityIndex(otherPlayer.getIndex());
		player.setAttackType(Equipment.isUsingRanged(player) ? AttackTypes.RANGED : AttackTypes.MELEE);
		player.setInCombat(true);
		Combat.handleCombatStyle(player);
		Server.getService().schedule(new EntityFollowService(otherPlayer, player, true));
		player.getPacketDispatcher().sendMessage(
				"Other Players Index: " + otherPlayer.getIndex());
	}

	private void handleFollowOption(Player player, Packet packet) {
		final int followPlayerIndex = packet.getLEShort();
		Player otherPlayer = Game.playerRepository.get(followPlayerIndex);
		player.setInteractingEntity(otherPlayer);
		player.setInteractingEntityIndex(otherPlayer.getIndex() + 32768);
		Server.getService().schedule(new EntityFollowService(otherPlayer, player, false));
	}

}
