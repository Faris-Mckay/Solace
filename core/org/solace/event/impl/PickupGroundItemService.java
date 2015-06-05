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
package org.solace.event.impl;

import org.solace.event.Event;
import org.solace.game.entity.ground.GroundItem;
import org.solace.game.entity.ground.GroundItemHandler;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.ItemDefinition;
import org.solace.game.map.Location;

public class PickupGroundItemService extends Event {

    private Location location;

    private int index;

    private Player player;

    public PickupGroundItemService(Player player, Location location, int index) {
        this.location = location;
        this.index = index;
        this.player = player;
    }

    @Override
    public void execute() {
        /*
         * Let's check if ground item exists.
         */
        GroundItem groundItem = GroundItemHandler.find(location, index);
        if (groundItem == null) {
            return;
        }

        /*
         * Check if it is stackable and to pick it up or discard if it exceeds
         * max stack size.
         */
        if (ItemDefinition.get(index).stackable()) {
            int itemSlot = player.getInventory().itemSlot(index);

            /*
             * Check if we have enough space in the inventory.
             */
            if (itemSlot == -1 && player.getInventory().freeSpace() == 0) {
                player.getInventory().noSpace();
                return;
            }

            if (itemSlot != -1) {
                /*
                 * If we already have the same item in the inventory let's
                 * calculate the total amount and discard if it exceeds max
                 * stack.
                 */
                long totalAmount = (long) player.getInventory().items()[itemSlot]
                        .getAmount() + (long) groundItem.item().getAmount();
                if (totalAmount > Integer.MAX_VALUE) {
                    player.getInventory().noSpace();
                    return;
                }
            }

            /*
             * Pickup the stackable item.
             */
            player.getInventory().add(groundItem.item());
            GroundItemHandler.deregister(groundItem);
            if (groundItem.timer() <= GroundItem.LIFE_SPAN / 2) {
                GroundItemHandler.sendRemoveGroundItem(groundItem);
            } else {
                player.getPacketDispatcher().sendRemoveGroundItem(groundItem);
            }
        } else {
            /*
             * Else if item is non-stackable let's check if we have enough
             * space.
             */
            if (player.getInventory().freeSpace() == 0) {
                player.getInventory().noSpace();
                return;
            }

            /*
             * And finally just pick it up.
             */
            player.getInventory().add(groundItem.item());
            GroundItemHandler.deregister(groundItem);
            if (groundItem.timer() <= GroundItem.LIFE_SPAN / 2) {
                GroundItemHandler.sendRemoveGroundItem(groundItem);
            } else {
                player.getPacketDispatcher().sendRemoveGroundItem(groundItem);
            }
        }
        this.stop();
    }

}
