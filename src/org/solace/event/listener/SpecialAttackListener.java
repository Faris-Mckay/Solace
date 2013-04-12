package org.solace.event.listener;

import mint.event.EventHandler;
import mint.event.EventListener;
import org.solace.event.impl.SpecialAttackEvent;
import org.solace.game.content.combat.specials.SpecialAttackManager;
import org.solace.game.item.container.impl.Equipment;

public class SpecialAttackListener implements EventListener {

	@EventHandler
	public void executeSpecial(SpecialAttackEvent event) {
		SpecialAttackManager.handleSpecial(event.getPlayer(), event.getPlayer().getEquipment().getItemBySlot(Equipment.WEAPON_SLOT));
	}

}
