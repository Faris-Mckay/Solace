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
package org.solace.game.entity.mobile;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadFactory;
import org.solace.game.Game;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.mobile.player.PlayerUpdateTask;
import org.solace.game.entity.mobile.update.PostUpdateExecutor;
import org.solace.game.entity.mobile.update.PreUpdateExecutor;
import org.solace.util.NamedThreadFactory;

/**
 *
 * @author Faris
 */
public class ParallelUpdateTask extends MobileUpdateTask {

    /**
	 * The executor service.
	 */
	private final ExecutorService executor;

	/**
	 * The phaser.
	 */
	private final Phaser phaser = new Phaser(1);

	/**
	 * Creates the parallel client synchronizer backed by a thread pool with a
	 * number of threads equal to the number of processing cores available
	 * (this is found by the {@link Runtime#availableProcessors()} method.
	 */
	public ParallelUpdateTask() {
		int processors = Runtime.getRuntime().availableProcessors();
		ThreadFactory factory = new NamedThreadFactory("ClientSynchronizer");
		executor = Executors.newFixedThreadPool(processors, factory);
	}

	@Override
	public void synchronize() {
		Map<Integer ,Player> players = Game.getPlayerRepository();
		int playerCount = players.size();

		phaser.bulkRegister(playerCount);
		for (Player player : players.values()) {
			MobileUpdateTask task = new PreUpdateExecutor(player);
			executor.submit(new PhasedMobileUpdate(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(playerCount);
		for (Player player : players.values()) {
			MobileUpdateTask task = player.getUpdater();
			executor.submit(new PhasedMobileUpdate(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();

		phaser.bulkRegister(playerCount);
		for (Player player : players.values()) {
			MobileUpdateTask task = new PostUpdateExecutor(player);
			executor.submit(new PhasedMobileUpdate(phaser, task));
		}
		phaser.arriveAndAwaitAdvance();
                
                phaser.bulkRegister(playerCount);
                for (Player player : players.values()) {   
                        MobileUpdateTask task = player.getNpcUpdating();
                        executor.submit(new PhasedMobileUpdate(phaser, task));
                }
                phaser.arriveAndAwaitAdvance();
                
                for (Player player : players.values()) {
			player.getUpdater().resetUpdateVars();
		}
                
                for (NPC npc : Game.getNpcRepository().values()) {
                    try {
                            npc.getUpdateFlags().reset();
                    } catch (Exception e) {
                            e.printStackTrace();
                    }
                }
	}

    @Override
    public void run() {
        synchronize();
    }

}
