package org.solace.game.entity.object;

import java.util.ArrayList;

import org.solace.Server;
import org.solace.event.Event;
import org.solace.game.Game;
import org.solace.game.entity.grounded.GroundItem;
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
        for (Player player : Game.playerRepository.values()) {
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
