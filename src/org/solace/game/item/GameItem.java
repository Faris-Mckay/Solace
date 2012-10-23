package org.solace.game.item;

/**
 * 
 * @author Faris
 */
public class GameItem {

	public int id, amount;
	public boolean stackable = false;

	public GameItem(int id, int amount) {
		if (ItemDefinition.get(id).stackable()) {
			stackable = true;
		}
		this.id = id;
		this.amount = amount;
	}

}
