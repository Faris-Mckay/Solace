package org.solace.game.item;

/**
 *
 * @author Faris
 */
public class GameItem {

    public int id, amount;
    public boolean stackable = false;

    public GameItem(int id, int amount) {
            if (ItemDefinitions.getDef()[id].isStackable) {
                    stackable = true;
            }
            this.id = id;
            this.amount = amount;
    }


}
