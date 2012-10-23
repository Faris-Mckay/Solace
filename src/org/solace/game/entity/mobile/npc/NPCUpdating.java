package org.solace.game.entity.mobile.npc;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.solace.game.Game;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Location;
import org.solace.network.packet.PacketBuilder;

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

		if (out == null || block == null)
			return;
		/*
		 * Initializes the npc updating packet
		 */
		out.createShortSizedFrame(65, player.channelContext().encryption());
		out.bitAccess();
		/*
		 * Writes the local npc list size
		 */
		out.putBits(8, player.getNpcUpdating().localNpcs.size());
		/*
		 * Iterates over all of the npcs in the localNpcs list
		 */
		for (Iterator<NPC> iterator = localNpcs.iterator(); iterator.hasNext();) {
			/*
			 * Constructs a single npc from the iterator
			 */
			NPC n = iterator.next();
			/*
			 * Checks if the npc is visible and within distance and the player
			 * has the npc in his list
			 */
			if (n.isNpcVisible()
					&& player.getLocation().withinDistance(n.getLocation())
					&& player.getNpcUpdating().localNpcs.contains(n)) {
				updateNpcMovement(out, n);
				appendNpcUpdateBlock(block, n);
			} else {
				/*
				 * Removes the npc from the list
				 */
				out.putBit(true);
				out.putBits(2, 3);
				iterator.remove();
			}
		}
		/*
		 * Loops through all available npcs in the repository
		 */
		for (NPC npc : Game.npcRepository.values()) {

			if (npc == null || !npc.isNpcVisible()
					|| player.getNpcUpdating().localNpcs.contains(npc))
				continue;

			/*
			 * Checks if the npc is within distance of the player
			 */
			if (player.getLocation().withinDistance(npc.getLocation())) {
				/*
				 * Adds a new npc to the list
				 */
				player.getNpcUpdating().localNpcs.add(npc);
				addNPC(out, npc);
				appendNpcUpdateBlock(block, npc);
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
	 * 
	 * @param out
	 *            The outstream to the client
	 * @param npc
	 *            The npc being updated
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
	 * 
	 * @param out
	 * @param npc
	 * @param player
	 */
	private void addNPC(PacketBuilder out, NPC npc) {
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

	private void appendNpcUpdateBlock(PacketBuilder block, NPC npc) {

		/*
		 * Creates an instance for the update mask
		 */
		int updateMask = 0x0;
		if (npc.getUpdateFlags().get(UpdateFlag.ANIMATION)) {
			updateMask |= 0x10;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.HIT)) {
			updateMask |= 0x8;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.GRAPHICS)) {
			updateMask |= 0x80;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FACE_ENTITY)) {
			updateMask |= 0x20;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FORCED_CHAT)) {
			updateMask |= 0x1;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.HIT_2)) {
			updateMask |= 0x40;
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FACE_COORDINATE)) {
			updateMask |= 0x4;
		}

		/*
		 * Writes the update mask to the client
		 */
		block.putByte(updateMask);

		if (npc.getUpdateFlags().get(UpdateFlag.ANIMATION)) {
			block.putLEShort(npc.getAnimation().getId());
			block.putByte(npc.getAnimation().getDelay());
		}
		if (npc.getUpdateFlags().get(UpdateFlag.HIT)) {
			appendHitMask(block, npc);
		}
		if (npc.getUpdateFlags().get(UpdateFlag.GRAPHICS)) {
			block.putShort(npc.getGraphic().getId());
			block.putIntTest(npc.getGraphic().getDelay());
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FACE_ENTITY)) {
			block.putShort(npc.getInteractingEntityIndex());
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FORCED_CHAT)) {
			block.putString(npc.getUpdateFlags().getForceChatMessage());
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FACE_COORDINATE)) {
			Location pos = npc.getUpdateFlags().getFaceLocation();
			if (pos == null) {
				block.putLEShort(0);
				block.putLEShort(0);
			} else {
				block.putLEShort(pos.getX() * 2 + 1);
				block.putLEShort(pos.getY() * 2 + 1);
			}
		}
	}

	/**
	 * Updates the damage mask
	 * 
	 * @param block
	 * @param npc
	 */
	private void appendHitMask(PacketBuilder block, NPC npc) {
		block.putByteA(npc.getUpdateFlags().getDamage());
		block.putByteC(npc.getUpdateFlags().getHitmask());
		block.putByteA(getCurrentHP(npc.getHitpoints(), npc.getDefinition()
				.getHitpoints(), 100));
		block.putByte(100);
	}

	/**
	 * Gets the npcs current hitpoints so it doesnt loop the hitpoints bar
	 * 
	 * @param i
	 * @param i1
	 * @param i2
	 * @return
	 */
	public static int getCurrentHP(int i, int i1, int i2) {
		double x = (double) i / (double) i1;
		return (int) Math.round(x * i2);
	}

}
