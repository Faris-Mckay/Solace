package org.solace.game.content.minigame.impl;

import java.util.List;
import org.solace.game.content.minigame.Minigame;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Boundary;
import org.solace.game.map.Location;

/**
 *
 * @author Faris
 */
public class ExampleMinigame implements Minigame {

    @Override
    public List<Player> gamePlayers() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void begin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void end() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void process() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean gameStarted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePlayer(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPlayer(Player player) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Location getStartLocation() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Boundary getBoundary(Location lowestPoint, Location highestPoint) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public MinigameSafety getMinigameSafety() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void handleDeath(MinigameSafety safety) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean gameShouldBegin() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean gameShouldEnd() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetMinigameVars() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
