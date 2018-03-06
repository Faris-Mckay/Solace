/*
 * This file is part of Solace Framework.
 * Solace is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Solace is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Solace. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.solace.game.entity.mobile.update.impl;

import java.util.LinkedList;
import java.util.List;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.update.MobileUpdateTask;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Location;
import org.solace.network.util.Stream;

public class NPCUpdateTask extends MobileUpdateTask {

	private Player player;

	private List<NPC> localNpcs;

	public NPCUpdateTask(Player player) {
		this.player = player;
		localNpcs = new LinkedList<>();
	}

	/**
	 * Updates this npc
	 */
	public void updateMobile() {
            
	}

	/**
	 * Checks if the npc is walking or not
	 * 
	 * @param out
	 *            The outstream to the client
	 * @param npc
	 *            The npc being updated
	 */
	public void updateNpcMovement(Stream out, NPC npc) {
		if (npc.getMobilityManager().walkingDirection() == -1) {
			out.writeBits(1, 1);
			out.writeBits(2, 0);
		} else {
			out.writeBits(1, 1);
			out.writeBits(2, 1);
			out.writeBits(3, npc.getMobilityManager().walkingDirection());
			out.writeBits(1, 1);
		}
	}

	/**
	 * Adds a new npc for the player
	 * 
	 * @param out
	 * @param npc
	 * @param player
	 */
	private void addNPC(Stream out, NPC npc) {
		out.writeBits(14, npc.getIndex());
		Location delta = new Location(npc.getLocation().getX()
				- player.getLocation().getX(), npc.getLocation().getY()
				- player.getLocation().getY());
		out.writeBits(5, delta.getY());
		out.writeBits(5, delta.getX());
		out.writeBits(1, 0);
		out.writeBits(12, npc.getNpcId());
		out.writeBits(1,1);
	}

	private void appendNpcUpdateBlock(Stream block, NPC npc) {

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
		block.writeByte(updateMask);

		if (npc.getUpdateFlags().get(UpdateFlag.ANIMATION)) {
			block.writeWordBigEndian(npc.getAnimation().getId());
			block.writeByte(npc.getAnimation().getDelay());
		}
		if (npc.getUpdateFlags().get(UpdateFlag.HIT)) {
			appendHitMask(block, npc);
		}
		if (npc.getUpdateFlags().get(UpdateFlag.GRAPHICS)) {
			block.writeWord(npc.getGraphic().getId());
			block.writeDWord(npc.getGraphic().getValue());
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FACE_ENTITY)) {
			block.writeWord(npc.getUpdateFlags().getFaceIndex());
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FORCED_CHAT)) {
			block.writeString(npc.getUpdateFlags().getForceChatMessage());
		}
		if (npc.getUpdateFlags().get(UpdateFlag.HIT_2)) {
			appendHitMask2(block, npc);
		}
		if (npc.getUpdateFlags().get(UpdateFlag.FACE_COORDINATE)) {
			Location pos = npc.getUpdateFlags().getFaceLocation();
			if (pos == null) {
				block.writeWordBigEndian(0);
				block.writeWordBigEndian(0);
			} else {
				block.writeWordBigEndian(pos.getX() * 2 + 1);
				block.writeWordBigEndian(pos.getY() * 2 + 1);
			}
		}
	}

	/**
	 * Updates the damage mask
	 * 
	 * @param block
	 * @param npc
	 */
	private void appendHitMask(Stream block, NPC npc) {
		block.writeByteA(npc.getUpdateFlags().getDamage());
		block.writeByteC(npc.getUpdateFlags().getHitType());
		block.writeByteA(getCurrentHP(npc.getHitpoints(), npc.getDefinition()
				.getHitpoints(), 100));
		block.writeByte(100);
	}
	
	/**
	 * Updates the second hit mask
	 * @param out
	 * @param npc
	 */
	public void appendHitMask2(Stream out, NPC npc) {
		out.writeByteC(npc.getUpdateFlags().getDamage2());
		out.writeByteS(npc.getUpdateFlags().getHitType2());
		out.writeByteS(getCurrentHP(npc.getHitpoints(), npc.getDefinition().getHitpoints(), 100));
		out.writeByteC(100);
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

    @Override
    public void run() {
        synchronize();
    }

    @Override
    public void synchronize() {
        updateMobile();
    }

}
