package org.solace.game.entity.mobile.npc;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.solace.network.packet.PacketBuilder;
import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Location;

public class NPCUpdating {

	private Player player;

	private List<NPC> localNpcs;

	public NPCUpdating(Player player) {
		this.player = player;
		localNpcs = new LinkedList<NPC>();
	}

	/**
	 * Updates this npc
	 */
	public void updateThisNpc() {

		PacketBuilder out = PacketBuilder.allocate(4096);
		PacketBuilder block = PacketBuilder.allocate(2048);

		if (out == null)
			return;
		/*
		 * Initializes the npc updating packet
		 */
		out.createShortSizedFrame(65, player.channelContext().encryption());
		out.bitAccess();
		/*
		 * Writes the local npc list size
		 */
		out.putBits(8, localNpcs.size());
		/*
		 * Iterates over all of the npcs in the localNpcs list
		 */
		for (Iterator<NPC> iterator = localNpcs.iterator(); iterator.hasNext();) {
			/*
			 * Constructs a single npc from the iterator
			 */
			NPC n = iterator.next();
			/*
			 * Checks if the npc is visible and within distance and the player has the npc 
			 * in his list
			 */
			if (n.isNpcVisible() && player.getLocation().withinDistance(n.getLocation()) && localNpcs.contains(n)) {
				updateNpcMovement(out, n);
				appendNpcUpdateBlock(block, n, false);
			} else {
				/*
				 * Removes the npc from the list 
				 */
				iterator.remove();
				out.putBits(1, 1);
				out.putBits(2, 3);
			}
		} 
		/*
		 * Loops through all available npcs in the repository
		 */
		for (NPC npc : Game.npcRepository.values()) {
			/*
			 * Checks if the player doesnt have the npc and if the npc is within distance
			 */
			if (!localNpcs.contains(npc) && player.getLocation().withinDistance(npc.getLocation())) {
				/*
				 * Adds a new npc to the list
				 */
				localNpcs.add(npc);
				addNPC(out, npc, player);
				appendNpcUpdateBlock(block, npc, true);
			}
		}
		/*
		 * Sends the status to the client
		 */
		if (block.buffer().position() > 0) {
			out.putBits(14, 16383);
			out.byteAccess();
			out.put(block.buffer());
		} else {
			out.byteAccess();
		}
		/*
		 * Ends the npc updating block
		 */
		out.finishShortSizedFrame();
		/*
		 * Writes to the players outstream channel
		 */
		out.sendTo(player.channelContext().channel());
	}
	
	/**
	 * Checks if the npc is walking or not
	 * @param out
	 * @param npc
	 */
	public void updateNpcMovement(PacketBuilder out, NPC npc) {
		if (npc.getMobilityManager().walkingDirection() == -1) {
			out.putBits(1, 1);
			out.putBits(2, 0);
		} else {
			out.putBits(1, 1);
			out.putBits(2, 1);
			out.putBits(3, npc.getMobilityManager().walkingDirection());
			out.putBits(1, 1);
		}
	}
	
	/**
	 * Adds a new npc for the player
	 * @param out
	 * @param npc
	 * @param player
	 */
	private void addNPC(PacketBuilder out, NPC npc, Player player) {
		out.putBits(14, npc.getIndex());
		Location delta = new Location(npc.getLocation().getX()
				- player.getLocation().getX(), npc.getLocation().getY()
				- player.getLocation().getY());
		out.putBits(5, delta.getY());
		out.putBits(5, delta.getX());
		out.putBits(1, 0);
		out.putBits(12, npc.getNpcId());
		out.putBit(true);
	}
	
	private void appendNpcUpdateBlock(PacketBuilder block, NPC npc, boolean force) {
		if (!npc.getUpdateFlags().isUpdateRequired() && !force) {
			//nothing to update
			return;
		}
		/*
		 * Creates an instance for the update mask
		 */
		int updateMask = 0x0;
		
		block.putByte(updateMask);
	}

}
