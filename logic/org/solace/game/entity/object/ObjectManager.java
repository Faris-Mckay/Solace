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
package org.solace.game.entity.object;

import java.util.ArrayList;

import org.solace.Server;
import org.solace.event.Event;
import org.solace.game.Game;
import org.solace.game.entity.ground.GroundItem;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Location;

/**
 *
 * @author Faris
 */
public class ObjectManager {

    /**
     * An array list of all the current objects
     */
    public final static ArrayList<GameObject> object = new ArrayList<GameObject>();

    /**
     * Registers an object
     *
     * @param o The object instance
     */
    public static void registerObject(final GameObject o) {
        object.add(o);
        for (Player player : Game.getPlayerRepository().values()) {
            if (player != null
                    && o.getLocation().withinDistance(player.getLocation(), 16)) {
                player.getPacketDispatcher().sendObject(o, false);
                expireObject(o, player);
            }
        }
    }

    /**
     * Updates any existing objects
     *
     * @param player The player instance
     */
    public static void updateObject(final Player player) {
        for (GameObject o : object) {
            if (player.getLocation().withinDistance(o.getLocation(), 32)) {
                player.getPacketDispatcher().sendObject(o, false);
            }
        }
    }

    /**
     * Checks to see if there is an object at a location
     *
     * @param location The location to check
     * @return If the object exists or not
     */
    public static boolean objectExists(final Location location) {
        for (GameObject o : object) {
            if (location.getX() == o.getLocation().getX()
                    && location.getY() == o.getLocation().getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks to see if there is an object at a location
     *
     * @param location The location to check
     * @return If the object exists or not
     */
    public static boolean isObjectAt(final GameObject o, final Location location) {
        if (location.getX() == o.getLocation().getX()
                && location.getY() == o.getLocation().getY()) {
            return true;
        }
        return false;
    }

    /**
     * Starts an expiration task
     *
     * @param o The object instance
     */
    public static void expireObject(final GameObject o, final Player player) {
        Server.getService().schedule(new Event(1) {
            @Override
            public void execute() {
                if (o.getLifeCycle() > 0) {
                    o.setLifeCycle(o.getLifeCycle() - 1);
                } else {
                    if (o.isExpireable()) {
                        if (o.getReplacementId() == -1) {
                            player.getPacketDispatcher().sendRemoveObject(o);
                        } else {
                            player.getPacketDispatcher().sendObject(o, true);
                        }
                    } else {
                        player.getPacketDispatcher().sendRemoveObject(o);
                    }
                    if (o.getObjectId() == 2732) {
                        player.getPacketDispatcher()
                                .sendGroundItem(
                                new GroundItem(592, 1, player, o
                                .getLocation()));
                    }
                    object.remove(o);
                    stop();
                }
            }
        });
    }
}
