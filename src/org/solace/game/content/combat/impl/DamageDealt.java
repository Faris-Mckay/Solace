package org.solace.game.content.combat.impl;

import java.util.ArrayList;

import org.solace.game.Game;
import org.solace.game.entity.mobile.player.Player;

/**
 * Calculates the amount of damage done to the entity
 * @author Arithium
 *
 */
public class DamageDealt {
	 
    private ArrayList<Damage> dealtDamage = new ArrayList<Damage>();
    
    public void appendDamage(String player, int damage) {
        boolean isAdded = false;
        for (Damage aDealtDamage : dealtDamage) {
            if (aDealtDamage.getPlayer().equals(player)) {
                aDealtDamage.addDamage(damage);
                isAdded = true;
                break;
            }
        }
        if (!isAdded) {
            dealtDamage.add(new Damage(player, damage));
        }
    }
    
    public String getKiller() {
        String killer = "" ;
        int mostDamage = 0;
        for (Damage aDealtDamage : dealtDamage) {
            if (aDealtDamage.getDamage() > mostDamage) {
                killer = aDealtDamage.getPlayer();
                mostDamage = aDealtDamage.getDamage();
            }
        }
        return killer;
    }
    
    public Player getPlayerKiller() {
        Player killer = null;
        int mostDamage = 0;
        for (Damage aDealtDamage : dealtDamage) {
         if (Game.playerRepository.get(aDealtDamage.getPlayer()) != null) {
          if (aDealtDamage.getDamage() > mostDamage) {
           killer = Game.playerRepository.get(aDealtDamage.getPlayer());
           mostDamage = aDealtDamage.getDamage();
          }
         }
        }
        return killer;
    }
 
    public void resetDealtDamage() {
       dealtDamage.clear(); 
    }
 
    private static class Damage {
     
        public String getPlayer() {
            return player;
        }
        
        public int getDamage() {
            return damage;
        }
        
        public void addDamage(int amount) {
            this.damage += damage;
        }
        
        private String player = "";
        private int damage = 0;
        
        private Damage(String player, int damage) {
            this.player = player;
            this.damage = damage;
        }

    }

}