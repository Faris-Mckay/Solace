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