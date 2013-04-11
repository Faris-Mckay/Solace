package org.solace.game.item.container.impl;

import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.game.item.ItemDefinition;
import org.solace.game.item.container.Container;

/**
 * Handles all of the banking actions
 * 
 * @author Arithium
 */
public class Banking extends Container {

	private boolean withdrawNote = false;
	private boolean insertItems = false;

	/**
	 * Allocates a new bank container for specified player().
	 * 
	 * @param player
	 *            the player instance
	 */
	public Banking(Player player) {
		super(player);
		resetItems();
	}

	/**
	 * Opens up the bank interface.
	 */
	public Banking openBank() {
		/*
		 * Open the bank.
		 */
		player().getBanking().refreshItems();
		player().getPacketDispatcher().sendInterfaceSet(5292, 5063);

		/*
		 * Reset bank the interface configurations.
		 */
		player().getPacketDispatcher().sendConfig(304, 0);
		insertItems(false);
		player().getPacketDispatcher().sendConfig(115, 0);
		withdrawNote(false);
		return this;
	}

	/**
	 * Deposits an item to the bank.
	 * 
	 * @param index
	 *            the item index
	 * 
	 * @param amount
	 *            the item amount
	 * 
	 * @param slot
	 *            the item slot
	 */
	public Banking deposit(int index, int amount, int slot) {
		/*
		 * Inventory item reference.
		 */
		Item item = player().getInventory().items()[slot];
		
		if (item == null)
			return this;

		/*
		 * Check if this was a real packet.
		 */
		if (item.getIndex() != index) {
			return this;
		}
		
		if (amount > player().getInventory().itemAmount(index))
			amount = player().getInventory().itemAmount(index);

		/*
		 * Add item to the bank.
		 */
		ItemDefinition definition = ItemDefinition.get(index);
		int notAdded = add(definition.note() ? definition.noteableIndex() : index, amount, false);
		if (!definition.stackable() && amount == 1) {
			player().getInventory().set(slot, -1, 0);
		} else {
			player().getInventory().delete(index, amount - notAdded);
		}
		System.out.println(notAdded);

		/*
		 * Finally let's send items to the client.
		 */
		player().getInventory().refreshItems();
		refreshItems();
		return this;
	}

	/**
	 * Withdraws an item from the bank.
	 * 
	 * @param index
	 *            the item index
	 * 
	 * @param amount
	 *            the item amount
	 * 
	 * @param slot
	 *            the item slot
	 */
	public Banking withdraw(int index, int amount, int slot) {
		/*
		 * Bank item reference.
		 */
		Item item = items()[slot];

		/*
		 * Check if this was a real packet.
		 */
		if (item.getIndex() != index) {
			return this;
		}
		
		if (amount > items()[slot].getAmount())
			amount = items()[slot].getAmount();

		/*
		 * Withdraw an item from the bank.
		 */
		ItemDefinition definition = ItemDefinition.get(index);
		player().getInventory().add(withdrawNote() ? definition.noteableIndex() : index, amount, false);
		delete(index, amount);

		/*
		 * If we left an empty slot, let's sort out the bank.
		 */
		if (items()[slot].getIndex() == -1) {
			sortBankItems();
		}

		/*
		 * Finally let's send items to the client.
		 */
		player().getInventory().refreshItems();
		refreshItems();
		return this;
	}

	/**
	 * Sorts out bank items leaving no empty slots in the bank.
	 */
	public Banking sortBankItems() {
		/*
		 * Sort shifting indicator.
		 */
		boolean shift = false;

		for (int i = 0; i < capacity(); i++) {
			/*
			 * If item shifting is toggled, let's shift the item one slot back.
			 */
			if (shift) {
				items()[i - 1] = items()[i].copy();
			}

			/*
			 * If we find a free slot, let's toggle the shifting.
			 */
			if (items()[i].getIndex() == -1) {
				if (!shift) {
					shift = true;
				} else {
					break;
				}
			}
		}
		return this;
	}

	/**
	 * Sets the withdraw note mode status.
	 * 
	 * @param withdrawNote
	 *            the withdraw note mode status
	 */
	public Banking withdrawNote(boolean withdrawNote) {
		this.withdrawNote = withdrawNote;
		return this;
	}

	/**
	 * Gets the withdraw note mode status.
	 * 
	 * @return the withdraw note mode
	 */
	public boolean withdrawNote() {
		return withdrawNote;
	}

	/**
	 * Gets the insert items mode status.
	 * 
	 * @return the insert items mode
	 */
	public boolean insertItems() {
		return insertItems;
	}

	/**
	 * Sets the insert items mode status.
	 * 
	 * @param insertItems
	 *            the insert items mode status
	 */
	public Banking insertItems(boolean insertItems) {
		this.insertItems = insertItems;
		return this;
	}

	@Override
	public int capacity() {
		return 352;
	}

	@Override
	public boolean stack() {
		return true;
	}

	@Override
	public Banking refreshItems() {
		player().getPacketDispatcher().sendItemContainer(this, BANK_INTERFACE);
		player().getPacketDispatcher().sendItemContainer(
				player().getInventory(), BANK_INVENTORY_INTERFACE);
		return this;
	}

	@Override
	public Banking noSpace() {
		player().getPacketDispatcher().sendMessage(
				"Not enough space in your bank.");
		return this;
	}

	public static final int BANK_INTERFACE = 5382;
	public static final int BANK_INVENTORY_INTERFACE = 5064;

}