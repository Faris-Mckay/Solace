package org.solace.util;

/**
 *
 * @author Faris
 */
public class IndexManager {
    
    /**
     * Stores index keys, anything nulled is an available index
     */
    public static Integer indexStore [] = new Integer[Constants.SERVER_MAX_PLAYERS];
    
    /**
     * Sets the parsed index to null for recycling
     * @param index players old index
     */
    public static void freeIndex(int index){
        indexStore[index] = null;
    }
    
    /**
     * Loops through available index
     * @returns first unassigned index
     */
    public static Integer getIndex(){
        for(Integer index : indexStore){
            if(index != null){
                return index;
            }
        }
        return null;
    }

}
