package org.solace.task.impl;

import org.solace.task.ConcurrentTask;
import org.solace.world.World;
import org.solace.world.game.entity.mobile.Mobile;

/**
 *
 * @author Faris
 */
public class MobileUpdateTask extends ConcurrentTask {
    
    @Override
    public void execute(){
        for(Mobile mobile : World.mobileRepository){
            if(mobile != null){
                mobile.update();
            }
        }
    }

}
