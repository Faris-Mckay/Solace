package org.solace.event.impl;

import mint.event.Event;
import org.solace.game.entity.mobile.Mobile;

public class AttackEvent implements Event {

    private final Mobile attacker;

    public AttackEvent(Mobile attacker) {
        this.attacker = attacker;
    }

    public Mobile getEntity() {
        return attacker;
    }
}
