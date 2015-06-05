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
	
	/**
	 * Gets the copy of this item.
	 * 
	 * @return the item object clone
	 */
	public Item copy() {
		return new Item(index, amount);
	}

}
