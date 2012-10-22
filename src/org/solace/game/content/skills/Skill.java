package org.solace.game.content.skills;

import org.solace.game.entity.mobile.player.Player;

/**
 *
 * @author Faris
 */
public abstract class Skill extends SkillHandler {
    
    public Skill(Player player){
        super(player);
    }

}
