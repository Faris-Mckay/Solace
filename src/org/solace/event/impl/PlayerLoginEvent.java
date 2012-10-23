package org.solace.event.impl;

import org.solace.Server;
import org.solace.event.Event;
import org.solace.util.Constants;
import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public class PlayerLoginEvent extends Event {
    
    public PlayerLoginEvent(Player player){
        super(EventType.INDEPENDANT, 0 , false);
        this.player = player;
    }
    
    Player player;

    @Override
    public void execute() {
        Server.logger.info("[Registry]: new connection made from player: "+player.getAuthentication().getUsername());
        player.getPacketDispatcher().sendMessage("Welcome "+player.getAuthentication().getUsername()+", to "+Constants.SERVER_NAME);
        player.getPacketDispatcher().sendMessage("Current players: "+(Game.playerRepository.size() + Game.registryQueue.size()));
        player.getEquipment().refreshItems();
        player.getInventory().refreshItems();
    }


}
