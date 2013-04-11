package org.solace.event.listener;

import mint.event.EventHandler;
import mint.event.EventListener;

import org.solace.Server;
import org.solace.event.impl.PlayerLoginEvent;
import org.solace.game.Game;
import org.solace.game.content.skills.SkillHandler;
import org.solace.util.Constants;

public class PlayerLoginListener implements EventListener {
	
	@EventHandler
	public void handleLogin(PlayerLoginEvent event) {
		Server.logger.info("[Registry]: new connection made from player: "+event.getPlayer().getAuthentication().getUsername());
        event.getPlayer().getPacketDispatcher().sendMessage("Welcome "+event.getPlayer().getAuthentication().getUsername()+", to "+Constants.SERVER_NAME);
        event.getPlayer().getPacketDispatcher().sendMessage("Current players: "+(Game.playerRepository.size() + Game.registryQueue.size()));
        event.getPlayer().getEquipment().refreshItems();
        event.getPlayer().getInventory().refreshItems();
        for (int i = 0; i < SkillHandler.MAXIMUM_SKILLS; i++) {
        	event.getPlayer().getSkills().refreshSkill(i);
		}
        event.getPlayer().appendLoginAttributes();
	}

}
