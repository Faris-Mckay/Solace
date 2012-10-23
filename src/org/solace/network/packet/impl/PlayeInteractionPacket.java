package org.solace.network.packet.impl;

import org.solace.game.Game;
import org.solace.game.content.combat.Combat;
import org.solace.game.entity.mobile.Mobile.AttackTypes;
import org.solace.game.entity.mobile.player.Player;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.task.TaskExecuter;
import org.solace.event.impl.EntityFollowEvent;

public class PlayeInteractionPacket implements PacketHandler {

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
		}

	}

	private void handleAttackOption(Player player, Packet packet) {
		int playerIndex = packet.getLEShort();
		Player otherPlayer = Game.playerRepository.get(playerIndex);
		if (otherPlayer == null)
			return;
		player.setInteractingEntity(otherPlayer);
		player.setInteractingEntityIndex(otherPlayer.getIndex() + 32768);
		player.setAttackType(AttackTypes.MELEE);
		player.setInCombat(true);
		Combat.handleCombatStyle(player);
		TaskExecuter.get().schedule(new EntityFollowEvent(otherPlayer, player, true));
		player.getPacketDispatcher().sendMessage(
				"Other Players Index: " + otherPlayer.getIndex());
	}

	private void handleFollowOption(Player player, Packet packet) {
		final int followPlayerIndex = packet.getLEShort();
		Player otherPlayer = Game.playerRepository.get(followPlayerIndex);
		player.setInteractingEntity(otherPlayer);
		player.setInteractingEntityIndex(otherPlayer.getIndex() + 32768);
		TaskExecuter.get().schedule(new EntityFollowEvent(otherPlayer, player, false));
	}

}
