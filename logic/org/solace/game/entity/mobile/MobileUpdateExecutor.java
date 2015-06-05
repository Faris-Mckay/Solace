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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.ThreadFactory;
import org.solace.util.SolaceThreadFactory;

/**
 *
 * @author Faris
 */
public class MobileUpdateExecutor {
    
    /**
     * The executor service.
     */
    private final ExecutorService executor;

    /**
     * The phaser.
     */
    private final Phaser phaser = new Phaser(1);
    
    /**
     * List of awaiting updates
     */
    public static List<MobileUpdateTask> awaitingUpdate = new LinkedList();
    
    /**
     * The instance
     */
    public static final MobileUpdateExecutor instance = new MobileUpdateExecutor();
    
    /**
     * Returns the instance of the class
     * @return instance
     */
    public static MobileUpdateExecutor getInstance(){
        return instance;
    }
    
    public MobileUpdateExecutor(){
        final int processors = Runtime.getRuntime().availableProcessors() * 4;
        final ThreadFactory factory = new SolaceThreadFactory();
        executor = Executors.newFixedThreadPool(processors, factory);
    }
    
    /**
     * Submits a Player Update into the awaiting list
     * @param task 
     */
    public static void includeTask(MobileUpdateTask task){
        awaitingUpdate.add(task);
    }
    
    /**
     * Executes the update task for each submitted Client
     */
    public void executeUpdates(){
        phaser.bulkRegister(awaitingUpdate.size());
        Iterator<MobileUpdateTask> it = awaitingUpdate.iterator();
        while(it.hasNext() && !awaitingUpdate.isEmpty()){
            MobileUpdateTask task = it.next();
            executor.submit(new PhasedMobileUpdate(phaser, task));
            it.remove();
        }
        phaser.arriveAndAwaitAdvance();
    }
    
    

}
