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
package org.solace.game.entity.mobile.update;

import org.solace.game.entity.mobile.player.Player;



/**
 * A {@link SynchronizationTask} which does pre-synchronization work for the
 * specified {@link Player}.
 *
 * @author Graham
 */
public final class PreUpdateExecutor extends MobileUpdateTask {

    /**
     * The player.
     */
    private final Player player;

    /**
     * Creates the {@link PrePlayerSynchronizationTask} for the specified
     * player.
     *
     * @param player The player.
     */
    public PreUpdateExecutor(Player player) {
        this.player = player;
    }

    @Override
    public void run() {
        synchronize();
    }

    /**
     * Checks if a region update is required.
     *
     * @return {@code true} if so, {@code false} otherwise.
     */
    private boolean isRegionUpdateRequired() {
        return true;
    }

    @Override
    public void synchronize() {
        
    }
}

