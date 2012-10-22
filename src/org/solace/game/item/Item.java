package org.solace.game.item;

/**
 * Represents a single item
 * @author Arithium
 *
 */
public class Item {

	private int index, amount;
        
	public Item(int index, int amount) {
		setIndex(index).setAmount(amount);
	}

	public Item(int index) {
		setIndex(index).setAmount(1);
	}
        
        /**
         * Parses in a new Index for the item, and sets it
         * @param index
         * @return the updated item
         */
	public Item setIndex(int index) {
		this.index = index;
		return this;
	}
        
        /**
         * Parses in and amount for the item and sets it
         * @param amount
         * @return the updated item
         */
	public Item setAmount(int amount) {
		this.amount = amount;
		return this;
	}
        
        /**
         * Returns the index value
         * @return 
         */
	public int getIndex() {
		return index;
	}
        
        /**
         * Returns the amount value
         * @return 
         */
	public int getAmount() {
		return amount;
	}

}
