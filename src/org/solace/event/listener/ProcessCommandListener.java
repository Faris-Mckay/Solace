package org.solace.event.listener;

import mint.event.EventHandler;
import mint.event.EventListener;

import org.solace.event.impl.ProcessCommandEvent;
import org.solace.game.entity.mobile.player.command.CommandHandler;

/**
 * Processes a players command through the command handler
 * @author Arithium
 *
 */
public class ProcessCommandListener implements EventListener {
	
	@EventHandler
	public void processCommand(ProcessCommandEvent event) {
		CommandHandler.processCommand(event.getPlayer(), event.getCommand());
	}

}
