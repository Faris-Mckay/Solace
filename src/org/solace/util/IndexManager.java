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
    
    private static Integer[] npcIndexStore = new Integer[Constants.SERVER_MAX_NPCS];
    
    /**
     * Sets the parsed index to null for recycling
     * @param index players old index
     */
    public static void freeIndex(int index){
        indexStore[index] = null;
    }
    
    /**
     * Sets the parsed index to null for recycling
     * @param index NPCs old index
     */
    public static void freeNpcIndex(int index) {
    	npcIndexStore[index] = null;
    }
    
    /**
     * Loops through available index
     * @returns first unassigned index
     */
    public static Integer getIndex(){
        for(int i=1; i<indexStore.length; i++){
            if(indexStore[i] == null){
                indexStore[i] = i;
                return indexStore[i];
            }
        }
        return null;
    }
    
    /**
     * Loops through available index
     * @returns first unassigned index
     */
    public static Integer getNpcIndex() {
    	for (int i = 1; i < npcIndexStore.length; i++) {
    		if (npcIndexStore[i] == null) {
    			npcIndexStore[i] = i;
    			return npcIndexStore[i];
    		}
    	}
    	return null;
    }

}
