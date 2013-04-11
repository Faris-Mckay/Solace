package org.solace.game.entity.mobile.player.command;

import org.solace.game.entity.mobile.player.Player;

public interface Command {
	
	public void execute(Player player, String command);
	
	public int rightsRequired();
	
	public boolean checkRequirements(Player player);

}
