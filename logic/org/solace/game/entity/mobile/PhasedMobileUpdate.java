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

import java.util.concurrent.Phaser;

/**
 *
 * @author Faris
 */
public class PhasedMobileUpdate implements Runnable {
    
    /**
     * The phaser.
     */
    private final Phaser phaser;

    /**
     * The task.
     */
    private final Runnable task;

    /**
     * Creates the phased synchronization task.
     * @param phaser The phaser.
     * @param task The task.
     */
    public PhasedMobileUpdate(Phaser phaser, Runnable task) {
            this.phaser = phaser;
            this.task = task;
    }
    
    /**
     * Executes the update
     */
    @Override
    public void run() {
        try {
            task.run();
        } finally {
            phaser.arriveAndDeregister();
        }
    }

}