package org.solace.world.game.container;

/**
 *
 * @author Faris
 */
public abstract class Container {
    
    public Container(int size){
        this.setContainerSize(size);
    }
    
    public void setContainerSize(int size){
        this.containerSize = size;
    }
    
    private int containerSize;
    
    public int getContainerSize(){
        return containerSize;
    }

}
