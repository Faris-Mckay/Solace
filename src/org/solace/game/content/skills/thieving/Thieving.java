package org.solace.game.content.skills.thieving;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import org.solace.event.Event;
import org.solace.event.Event.EventType;

import org.solace.game.content.combat.DelayedAttack;
import org.solace.game.content.combat.SendDelayedHit;
import org.solace.game.content.skills.Skill;
import org.solace.game.entity.Animation;
import org.solace.game.entity.Graphic;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.item.Item;
import org.solace.task.Task;
import org.solace.task.TaskExecuter;
import org.solace.util.ProtocolUtils;
import org.solace.util.XStreamUtil;

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
				player.getPacketDispatcher().sendMessage("You attempt to pick the " + npcName + "'s pocket.");
				player.setAnimation(Animation.create(881));
				player.getUpdateFlags().flag(UpdateFlag.ANIMATION);
				final boolean succeed = ((Math.random() * player.getSkills().getPlayerLevel()[Skill.THIEVING]) > (Math.random() * levelRequired));
                                
                               Event newEvent = new Event(EventType.DEPENDANT, 3, false){
                                   @Override
                                   public void execute(){
                                       if (succeed) {
                                                player.getInventory().add( new Item(getItemToAdd(), amount));
                                                player.getSkills().addSkillExp(Skill.THIEVING, expAmount);
                                                player.getPacketDispatcher().sendMessage("You successfully pick the " + npcName + "'s pocket.");
                                        } else {
                                                SendDelayedHit.sendDelayedHit(npc, player, new DelayedAttack(3, 1, 1));
                                                player.setGraphic(Graphic.highGraphic(80, 0));
                                                player.getUpdateFlags().flag(UpdateFlag.GRAPHICS);
                                                npc.getUpdateFlags().sendForceMessage(forcedText);
                                                player.getPacketDispatcher().sendMessage("You failed to pickpocket the " + npcName + "'s pocket.");
                                        }
                                        this.stop();
                                   }
                               };
                               Event.submit(newEvent, player);
			} else {
				player.getPacketDispatcher().sendMessage("Doesnt exist.");
			}
		}
	}

	private static int getItemToAdd() {
		int itemToAdd = 0;
		for (int i = 0; i < definition.length; i++) {
			itemToAdd = definition[i].getItems()[(int) (Math.random() * definition[i]
					.getItems().length)];
		}
		return itemToAdd;
	}

	private static ThievingData[] definition = null;

	public static void load() throws FileNotFoundException {
		@SuppressWarnings("unchecked")
		List<ThievingData> XMLlist = (List<ThievingData>) XStreamUtil.getXStream().fromXML(new FileInputStream("./data/xml/thieving/npcthieving.xml"));
		definition = new ThievingData[XMLlist.size()];
		for (int i = 0; i < definition.length; i++) {
			definition[i] = XMLlist.get(i);
		}
	}

}
