package org.solace.game.content.skills.prayer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

import org.solace.Server;
import org.solace.event.Event;
import org.solace.game.content.skills.Skill;
import org.solace.game.content.skills.SkillHandler;
import org.solace.game.entity.Animation;
import org.solace.game.entity.mobile.player.Player;
import org.solace.util.XStreamUtil;

/**
 * Represents the prayer skill
 * 
 * @author Arithium
 * 
 */
public class Prayer extends Skill {

	public Prayer(Player player) {
		super(player);
	}

	/**
	 * Buries a bone for the player
	 * 
	 * @param player
	 *            The player burying the bone
	 * @param boneId
	 *            The bone the prayer is burying
	 */
	public static void buryBones(Player player, int itemId, int slot) {
		if (!player.isBuryingBones()) {
			for (PrayerDefinitions prayer : def) {
				if (prayer.getBoneId() == itemId) {
					if (player.getInventory().contains(itemId)) {
						player.getPacketDispatcher().sendMessage(
								"You dig a hole in the ground.");
						player.getInventory().delete(itemId, slot, 1);
						player.setAnimation(Animation.create(827));
						player.getSkills().addSkillExp(SkillHandler.PRAYER,
								prayer.getExperience());
						player.getPacketDispatcher().sendMessage(
								"You bury the bones.");
						sendPrayerDelay(player);
					}
				}
			}
		}
	}

	/**
	 * Creates a delay before burying again
	 * 
	 * @param player
	 */
	public static void sendPrayerDelay(final Player player) {
		player.setBuryingBones(true);
		Server.getService().schedule(new Event(1) {
			@Override
			public void execute() {
				player.setBuryingBones(false);
				this.stop();
			}
		});
	}

	private static PrayerDefinitions[] def = null;

	public static void load() throws FileNotFoundException {
		@SuppressWarnings("unchecked")
		List<PrayerDefinitions> XMLlist = (List<PrayerDefinitions>) XStreamUtil
				.getXStream().fromXML(
						new FileInputStream("./data/xml/prayer/prayer.xml"));
		def = new PrayerDefinitions[XMLlist.size()];
		for (int i = 0; i < def.length; i++) {
			def[i] = XMLlist.get(i);
		}
	}

}
