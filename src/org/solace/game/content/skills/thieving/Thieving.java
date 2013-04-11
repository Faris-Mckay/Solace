package org.solace.game.content.skills.thieving;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.solace.Server;
import org.solace.event.Event;
import org.solace.game.content.combat.impl.Hit;
import org.solace.game.content.combat.impl.InstantHit;
import org.solace.game.content.skills.Skill;
import org.solace.game.entity.Animation;
import org.solace.game.entity.Graphic;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.util.ProtocolUtils;
import org.solace.util.XStreamUtil;

/**
 * Represents the thieving skill
 * 
 * @author Arithium
 * 
 */
public class Thieving extends Skill {

	public Thieving(Player player) {
		super(player);
	}

	public static void handleNpcThieving(final Player player, final NPC npc) {
		for (int i = 0; i < definition.length; i++) {
			if (definition[i].getNpcId() == npc.getNpcId()) {
				final int amount = definition[i].getAmount();
				final int levelRequired = definition[i].getLevelRequired();
				final int expAmount = definition[i].getExperience();
				final String npcName = ProtocolUtils.formatString(npc.getDefinition().getName());
				final String forcedText = definition[i].getForcedText();
				
				if (player.getSkills().getPlayerLevel()[Skill.THIEVING] < levelRequired) {
					player.getPacketDispatcher().sendMessage("You need a thieving level of at least " + levelRequired + ".");
					return;
				}
				/*
				 * Checks if the player is currently stunned
				 */
				if ((Boolean) player.getAttribute("STUNNED")) {
					player.getPacketDispatcher().sendMessage("You are currently stunned.");
					return;
				}
				/*
				 * Checks if the players thieving delay, if so don't allow the player to steal
				 */
				if ((Boolean) player.getAttribute("NO_THIEVE")) {
					return;
				}
				sendThievingDelay(player);
				player.getPacketDispatcher().sendMessage("You attempt to pick the " + npcName + "'s pocket.");
				player.setAnimation(Animation.create(881));
				final boolean succeed = ((Math.random() * player.getSkills().getPlayerLevel()[Skill.THIEVING]) > (Math.random() * levelRequired));                          
				Server.getService().schedule(new Event(3) {
                                   @Override
                                   public void execute(){
                                       if (succeed) {
                                                player.getInventory().add( new Item(getItemToAdd(), amount));
                                                player.getSkills().addSkillExp(Skill.THIEVING, expAmount);
                                                player.getPacketDispatcher().sendMessage("You successfully pick the " + npcName + "'s pocket.");
                                        } else {
                                                InstantHit.sendInstantHit(player, new Hit(3, 1, 1, 0));
                                                player.setGraphic(Graphic.highGraphic(80, 0));
                                                stunPlayer(player);
                                                npc.getUpdateFlags().sendForceMessage(forcedText);
                                                player.getPacketDispatcher().sendMessage("You failed to pickpocket the " + npcName + "'s pocket.");
                                        }
                                        this.stop();
                                   }
                               });
			} else {
				player.getPacketDispatcher().sendMessage("Doesnt exist.");
			}
		}
	}
	
	/**
	 * Stuns the player upon failing to thieve
	 * @param player
	 */
	private static void stunPlayer(final Player player) {
		player.addAttribute("STUNNED", Boolean.TRUE);
		Server.getService().schedule(new Event(7) {
			@Override
			public void execute() {
				player.addAttribute("STUNNED", Boolean.FALSE);
				this.stop();
			}
		});
	}
	
	private static void sendThievingDelay(final Player player) {
		player.addAttribute("NO_THIEVE", Boolean.TRUE);
		Server.getService().schedule(new Event(1) {
			@Override
			public void execute() {
				player.addAttribute("NO_THIEVE", Boolean.FALSE);
				this.stop();
			}
		});
	}

	/**
	 * Selects a random item from the list to give to the player
	 * @return
	 */
	private static int getItemToAdd() {
		int itemToAdd = 0;
		for (int i = 0; i < definition.length; i++) {
			itemToAdd = definition[i].getItems()[(int) (Math.random() * definition[i]
					.getItems().length)];
		}
		return itemToAdd;
	}

	private static ThievingData[] definition = null;

	/**
	 * Loads all of the thieving definitions
	 * @throws FileNotFoundException
	 */
	public static void load() throws FileNotFoundException {
		@SuppressWarnings("unchecked")
		List<ThievingData> XMLlist = (List<ThievingData>) XStreamUtil.getXStream().fromXML(new FileInputStream("./data/xml/thieving/npcthieving.xml"));
		definition = new ThievingData[XMLlist.size()];
		for (int i = 0; i < definition.length; i++) {
			definition[i] = XMLlist.get(i);
		}
	}

}
