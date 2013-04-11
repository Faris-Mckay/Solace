package org.solace.event.listener;

import mint.event.EventHandler;
import mint.event.EventListener;

import org.solace.event.impl.AttackEvent;
import org.solace.game.content.combat.Combat;

public class AttackListener implements EventListener {
	
	@EventHandler
	public void processAttack(AttackEvent event) {
		Combat.handleCombatStyle(event.getEntity());
	}

}
