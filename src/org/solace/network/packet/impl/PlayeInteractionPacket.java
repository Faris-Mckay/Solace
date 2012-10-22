package org.solace.network.packet.impl;

import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;
import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;

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
                    handleFollowOption(player,packet);
                    break;
                }
                
	}

	private void handleAttackOption(Player player, Packet packet) {
		int playerIndex = packet.getLEShort();
		Player otherPlayer = Game.playerRepository.get(playerIndex);
		if (otherPlayer == null)
			return;
		player.getPacketDispatcher().sendMessage("Other Players Index: " + otherPlayer.getIndex());
	}
        private void handleFollowOption(Player player, Packet packet){
            //TODO
        }

}
