package org.solace.game.item.container;

import org.solace.game.item.Item;
import org.solace.game.entity.mobile.player.Player;

/**
 * Item container implementation.
 * @author Faris
 */
public abstract class Container {

	private Player player;
	private Item[] items;

	/**
	 * Creates a new item container for specified player.
	 * @param player
	 *            the player reference
	 */
	public Container(Player player) {
		this.player = player;
	}

	/**
	 * Gets an item by id.
	 * @param id
	 *            The id.
	 * @return The item, or <code>null</code> if it could not be found.
	 */
	public Item getById(int id) {
		for (int i = 0; i < items.length; i++) {
			if (items[i] == null) {
				continue;
			}
			if (items[i].getIndex() == id) {
				return items[i];
			}
		}
		return null;
	}

	/**
	 * Adds item to this item container.
	 * @param item
	 *            the item object
	 * 
	 * @return amount of items that were not added or 0 if all items were added
	 */
	public int add(Item item) {
		return add(item.getIndex(), item.getAmount(), true);
	}

	public Item get(int index) {
		return items[index];
	}

	/**
	 * Adds item to this item container.
	 * @param index
	 *            the item index
	 * @param amount
	 *            the item amount
	 * @return amount of items that were not added or 0 if all items were added
	 */
	public int add(int index, int amount) {
		return add(index, amount, true);
	}

	/**
	 * Adds item to this item container.
	 * 
	 * @param index
	 *            the item index
	 * 
	 * @param amount
	 *            the item amount
	 * 
	 * @param refresh
	 *            indicates if items should be sent to client
	 * 
	 * @return amount of items that were not added or 0 if all items were added
	 */
	public int add(int index, int amount, boolean refresh) {
		int notAdded = amount;
		for (; amount > 0; amount--) {
			int slot = emptySlot();
			if (slot != -1) {
				items[slot] = items[slot].setIndex(index).setAmount(1);
				notAdded--;
			} else {
				noSpace();
				break;
			}
		}
		if (refresh) {
			refreshItems();
		}
		return notAdded;
	}

	/**
	 * Deletes item from this item container.
	 * 
	 * @param item
	 *            the item object
	 * 
	 * @return amount of items that were not deleted or 0 if all items were
	 *         deleted
	 */
	public int delete(Item item) {
		return delete(item.getIndex(), item.getAmount());
	}

	/**
	 * Deletes item to from item container.
	 * 
	 * @param index
	 *            the item index
	 * 
	 * @param amount
	 *            the item amount
	 * 
	 * @return amount of items that were not deleted or 0 if all items were
	 *         deleted
	 */
	public int delete(int index, int amount) {
		return delete(index, amount, true);
	}

	public void delete(int itemId, int slot, int amount) {
		for (; amount > 0; amount--) {
			if (slot != -1) {
				items[slot].setIndex(-1);
				items[slot].setAmount(0);
				refreshItems();
			}
		}
	}

	/**
	 * Deletes item to from item container.
	 * @param index
	 *            the item index
	 * @param amount
	 *            the item amount
	 * @param refresh
	 *            indicates if items should be sent to client
	 * @return amount of items that were not deleted or 0 if all items were
	 *         deleted
	 */
	public int delete(int index, int amount, boolean refresh) {
		int notDeleted = amount;

		for (; amount > 0; amount--) {
			int slot = itemSlot(index);
			if (slot != -1) {
				items[slot].setIndex(-1);
				items[slot].setAmount(0);
				notDeleted--;
			} else {
				break;
			}
		}
		if (refresh) {
			refreshItems();
		}
		return notDeleted;
	}

	/**
	 * Sets item properties at the given slot.
	 * 
	 * @param slot
	 *            the item slot
	 * 
	 * @param item
	 *            the item
	 */
	public Container set(int slot, Item item) {
		return set(slot, item.getIndex(), item.getAmount());
	}

	/**
	 * Sets item properties at the given slot.
	 * 
	 * @param slot
	 *            the item slot
	 * 
	 * @param index
	 *            the item index
	 * 
	 * @param amount
	 *            the item amount
	 */
	public Container set(int slot, int index, int amount) {
		items[slot].setIndex(index).setAmount(amount);
		return this;
	}

	/**
	 * Gets free space of this container.
	 * 
	 * @return the free space
	 */
	public int freeSpace() {
		int totalSpace = 0;
		for (Item item : items) {
			if (item.getIndex() == -1) {
				totalSpace++;
			}
		}
		return totalSpace;
	}

	/**
	 * Checks if item container is full.
	 * 
	 * @return true only if there are no available slots
	 */
	public boolean isFull() {
		return emptySlot() == -1;
	}

	/**
	 * Gets the empty item slot in the item container, returns -1 if item
	 * container is full.
	 * 
	 * @return the empty item slot
	 */
	public int emptySlot() {
		for (int i = 0; i < items.length; i++) {
			if (items[i].getIndex() == -1) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the item slot in the item container, returns -1 if item is not
	 * found.
	 * 
	 * @param itemIndex
	 *            the item index
	 * 
	 * @return the item slot
	 */
	public int itemSlot(int itemIndex) {
		for (int i = 0; i < capacity(); i++) {
			if (items[i].getIndex() == itemIndex) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the total item amount.
	 * 
	 * @param itemIndex
	 *            the item index
	 * 
	 * @return the total item amount of specified item in this container
	 */
	public int itemAmount(int itemIndex) {
		int totalAmount = 0;
		for (Item item : items) {
			if (item.getIndex() == itemIndex) {
				totalAmount += item.getAmount();
			}
		}
		return totalAmount;
	}

	/**
	 * Gets an item by the slot id
	 * 
	 * @param slot
	 *            The slot
	 * @return The item id
	 */
	public int getItemBySlot(int slot) {
		return items[slot].getIndex();
	}

	/**
	 * Checks to see if the inventory contains an item
	 * 
	 * @param index
	 *            The item index
	 * @return If the inventory contains the item
	 */
	public boolean contains(int index) {
		for (Item item : items) {
			if (item.getIndex() == index) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Resets items of this container.
	 */
	public Container resetItems() {
		items = new Item[capacity()];
		for (int i = 0; i < capacity(); i++) {
			items[i] = new Item(-1, 0);
		}
		return this;
	}

	/**
	 * Copies items from another container.
	 * 
	 * @param container
	 *            the other container
	 */
	public Container copy(Container container) {
		resetItems();
		int capacity = capacity() > container.capacity() ? container.capacity()
				: capacity();
		for (int i = 0; i < capacity; i++) {
			items[i].setIndex(container.items()[i].getIndex()).setAmount(
					container.items()[i].getAmount());
		}
		return this;
	}

	/**
	 * Gets the associated player.
	 * 
	 * @return the associated player
	 */
	public Player player() {
		return player;
	}

	/**
	 * Gets the items array.
	 * 
	 * @return the items array
	 */
	public Item[] items() {
		return items;
	}

	/**
	 * Gets the container capacity.
	 * 
	 * @return the container capacity
	 */
	public abstract int capacity();

	/**
	 * Check if this container is stack, meaning that all same items goes into
	 * same stack.
	 * 
	 * @return true if this container is stack
	 */
	public abstract boolean stack();

	/**
	 * Refreshes items after certain action is done.
	 */
	public abstract Container refreshItems();

	/**
	 * Method that gets triggered whenever player is trying to add items to the
	 * container and there is no space available. Can be used to send chat
	 * message to the player or trigger other events.
	 */
	public abstract Container noSpace();

}