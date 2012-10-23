package org.solace.game.content.dialogue;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import org.solace.Server;

import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.npc.NPCDefinition;
import org.solace.game.entity.mobile.player.Player;
import org.solace.util.XStreamUtil;

public class Dialogue {

	private Player player;

	public Dialogue(Player player) {
		this.player = player;
	}

	public void sendDialogue(int dialogueId, int npcId) {
		for (int i = 0; i < def.length; i++) {
			if (dialogueId == def[i].dialogueId()) {
				if (def[i].getDialogues().length > 0) {
					int emoticon = def[i].getEmoticon();
					setPlayerChat(def[i].isPlayerChat());
					String[] dialogues = def[i].getDialogues();
                                        sendNpcChat(dialogues[i].replaceAll("playername", player.getAuthentication().getUsername()), emoticon);
					if (!isPlayerChat()) {
                                            
						switch (def[i].getDialogues().length) {
                                               /*     
						case 1:
							sendNpcChat(dialogues[0].replaceAll("playername", player.getAuthentication().getUsername()), emoticon);
							break;
						case 2:
							sendNpcChat(dialogues[0].replaceAll("playername", player.getAuthentication().getUsername()),
                                                                    dialogues[1].replaceAll("playername", player.getAuthentication().getUsername()), emoticon);
							break;
						case 3:
							sendNpcChat(dialogues[0],
									dialogues[1].replaceAll("playername", player.getAuthentication().getUsername()),
									dialogues[2].replaceAll("playername", player.getAuthentication().getUsername()), emoticon);
							break;
						case 4:
							sendNpcChat(dialogues[0].replaceAll("playername", player.getAuthentication().getUsername()),
									dialogues[1].replaceAll("playername", player.getAuthentication().getUsername()),
									dialogues[2].replaceAll("playername", player.getAuthentication().getUsername()),
									dialogues[3].replaceAll("playername", player.getAuthentication().getUsername()), emoticon);
							break;*/
						}
					} else {
						switch (dialogues.length) {
						/*case 1:
							sendPlayerChat(dialogues[0].replaceAll("playername", player.getAuthentication().getUsername()), emoticon);
							break;
						case 2:
							sendPlayerChat(dialogues[0].replaceAll("playername", player.getAuthentication().getUsername()),
									dialogues[1].replaceAll("playername", player.getAuthentication().getUsername()), emoticon);
							break;
						case 3:
							sendPlayerChat(dialogues[0].replaceAll("playername", player.getAuthentication().getUsername()),
									dialogues[1].replaceAll("playername", player.getAuthentication().getUsername()),
									dialogues[2].replaceAll("playername", player.getAuthentication().getUsername()), emoticon);
							break;
						case 4:
							sendPlayerChat(dialogues[0].replaceAll("playername", player.getAuthentication().getUsername()),
									dialogues[1].replaceAll("playername", player.getAuthentication().getUsername()),
									dialogues[2].replaceAll("playername", player.getAuthentication().getUsername()),
									dialogues[3].replaceAll("playername", player.getAuthentication().getUsername()), emoticon);
							break;*/
						}
					}
					setDialogueId(def[i].getNextDialogue());
				}
			}
		}
	}

	private boolean playerChat;

	private int dialogueId;

	private int dialogueAction;

	public int getDialogueId() {
		return dialogueId;
	}

	public void setDialogueId(int id) {
		this.dialogueId = id;
	}

	public void setDialogueAction(int action) {
		this.dialogueAction = action;
	}

	public int getDialogueAction() {
		return dialogueAction;
	}

	public boolean isPlayerChat() {
		return playerChat;
	}

	public void setPlayerChat(boolean playerChat) {
		this.playerChat = playerChat;
	}

	/**
	 * An information box.
	 */
	public void sendInformationBox(String title, String line1, String line2,
			String line3, String line4) {// check
		player.getPacketDispatcher().sendString(6180, title);
		player.getPacketDispatcher().sendString(6181, line1);
		player.getPacketDispatcher().sendString(6182, line2);
		player.getPacketDispatcher().sendString(6183, line3);
		player.getPacketDispatcher().sendString(6184, line4);
		player.getPacketDispatcher().sendChatInterface(6179);
	}

	/**
	 * Option selection.
	 */
	public void sendOption(String option1, String option2) {
		player.getPacketDispatcher().sendString(2461, option1);
		player.getPacketDispatcher().sendString(2462, option2);
		player.getPacketDispatcher().sendChatInterface(2459);
	}

	public void sendOption(String option1, String option2, String option3) {
		player.getPacketDispatcher().sendString(2471, option1);
		player.getPacketDispatcher().sendString(2472, option2);
		player.getPacketDispatcher().sendString(2473, option3);
		player.getPacketDispatcher().sendChatInterface(2469);
	}

	public void sendOption(String option1, String option2, String option3,
			String option4) {
		player.getPacketDispatcher().sendString(2482, option1);
		player.getPacketDispatcher().sendString(2483, option2);
		player.getPacketDispatcher().sendString(2484, option3);
		player.getPacketDispatcher().sendString(2485, option4);
		player.getPacketDispatcher().sendChatInterface(2480);
	}

	public void sendOption(String option1, String option2, String option3,
			String option4, String option5) {
		player.getPacketDispatcher().sendString(2494, option1);
		player.getPacketDispatcher().sendString(2495, option2);
		player.getPacketDispatcher().sendString(2496, option3);
		player.getPacketDispatcher().sendString(2497, option4);
		player.getPacketDispatcher().sendString(2498, option5);
		player.getPacketDispatcher().sendChatInterface(2492);
	}

	/**
	 * Statements.
	 */
	public void sendStatement1(String line1) {
		player.getPacketDispatcher().sendString(357, line1);
		player.getPacketDispatcher().sendChatInterface(356);
	}

	public void sendStatement(String line1, String line2) {
		player.getPacketDispatcher().sendString(360, line1);
		player.getPacketDispatcher().sendString(361, line2);
		player.getPacketDispatcher().sendChatInterface(359);
	}

	public void sendStatement(String line1, String line2, String line3) {
		player.getPacketDispatcher().sendString(364, line1);
		player.getPacketDispatcher().sendString(365, line2);
		player.getPacketDispatcher().sendString(366, line3);
		player.getPacketDispatcher().sendChatInterface(363);
	}

	public void sendStatement(String line1, String line2, String line3,
			String line4) {
		player.getPacketDispatcher().sendString(369, line1);
		player.getPacketDispatcher().sendString(370, line2);
		player.getPacketDispatcher().sendString(371, line3);
		player.getPacketDispatcher().sendString(372, line4);
		player.getPacketDispatcher().sendChatInterface(368);
	}

	public void sendStatement(String line1, String line2, String line3,
			String line4, String line5) {
		player.getPacketDispatcher().sendString(375, line1);
		player.getPacketDispatcher().sendString(376, line2);
		player.getPacketDispatcher().sendString(377, line3);
		player.getPacketDispatcher().sendString(378, line4);
		player.getPacketDispatcher().sendString(379, line5);
		player.getPacketDispatcher().sendChatInterface(374);
	}

	/**
	 * Timed statements. These statements have no close/click options, so should
	 * only be used with a timer.
	 */
	public void sendTimedStatement(String line1) {
		player.getPacketDispatcher().sendString(12789, line1);
		player.getPacketDispatcher().sendChatInterface(12788);
	}

	public void sendTimedStatement(String line1, String line2) {
		player.getPacketDispatcher().sendString(12791, line1);
		player.getPacketDispatcher().sendString(12792, line2);
		player.getPacketDispatcher().sendChatInterface(12790);
	}

	public void sendTimedStatement(String line1, String line2, String line3) {
		player.getPacketDispatcher().sendString(12794, line1);
		player.getPacketDispatcher().sendString(12795, line2);
		player.getPacketDispatcher().sendString(12796, line3);
		player.getPacketDispatcher().sendChatInterface(12793);
	}

	public void sendTimedStatement(String line1, String line2, String line3,
			String line4) {
		player.getPacketDispatcher().sendString(12798, line1);
		player.getPacketDispatcher().sendString(12799, line2);
		player.getPacketDispatcher().sendString(12800, line3);
		player.getPacketDispatcher().sendString(12801, line4);
		player.getPacketDispatcher().sendChatInterface(12797);
	}

	public void sendTimedStatement(String line1, String line2, String line3,
			String line4, String line5) {
		player.getPacketDispatcher().sendString(12803, line1);
		player.getPacketDispatcher().sendString(12804, line2);
		player.getPacketDispatcher().sendString(12805, line3);
		player.getPacketDispatcher().sendString(12806, line4);
		player.getPacketDispatcher().sendString(12807, line5);
		player.getPacketDispatcher().sendChatInterface(12802);
	}

	/**
	 * NPC dialogue.
	 */
	public void sendNpcChat(String line1, int emotion) {
                String mod = line1.replaceAll("playername", player.getAuthentication().getUsername());
		NPC npc = (NPC) player.getInteractingEntity();
		NPCDefinition def = NPCDefinition.getDefinitions()[npc.getNpcId()];
		String npcName = def.getName();
		player.getPacketDispatcher().sendDialogueAnimation(4883, emotion);
		player.getPacketDispatcher().sendString(4884, npcName);
		player.getPacketDispatcher().sendString(4885, mod);
		player.getPacketDispatcher().sendNPCDialogueHead(def.getId(), 4883);
		player.getPacketDispatcher().sendChatInterface(4882);
	}

	public void sendNpcChat(String line1, String line2, int emotion) {
                String mod = line1.replaceAll("playername", player.getAuthentication().getUsername());
                String mod2 = line2.replaceAll("playername", player.getAuthentication().getUsername());
		NPC npc = (NPC) player.getInteractingEntity();
		NPCDefinition def = NPCDefinition.getDefinitions()[npc.getNpcId()];
		String npcName = def.getName();
		player.getPacketDispatcher().sendDialogueAnimation(4888, emotion);
		player.getPacketDispatcher().sendString(4889, npcName);
		player.getPacketDispatcher().sendString(4890, mod);
		player.getPacketDispatcher().sendString(4891, mod2);
		player.getPacketDispatcher().sendNPCDialogueHead(def.getId(), 4888);
		player.getPacketDispatcher().sendChatInterface(4887);
	}

	public void sendNpcChat(String line1, String line2, String line3,
			int emotion) {
		NPC npc = (NPC) player.getInteractingEntity();
		NPCDefinition def = NPCDefinition.getDefinitions()[npc.getNpcId()];
		String npcName = def.getName();
		player.getPacketDispatcher().sendDialogueAnimation(4894, emotion);
		player.getPacketDispatcher().sendString(4895, npcName);
		player.getPacketDispatcher().sendString(4896, line1);
		player.getPacketDispatcher().sendString(4897, line2);
		player.getPacketDispatcher().sendString(4898, line3);
		player.getPacketDispatcher().sendNPCDialogueHead(def.getId(), 4894);
		player.getPacketDispatcher().sendChatInterface(4893);
	}

	public void sendNpcChat(String line1, String line2, String line3,
			String line4, int emotion) {
		NPC npc = (NPC) player.getInteractingEntity();
		NPCDefinition def = NPCDefinition.getDefinitions()[npc.getNpcId()];
		String npcName = def.getName();
		player.getPacketDispatcher().sendDialogueAnimation(4901, emotion);
		player.getPacketDispatcher().sendString(4902, npcName);
		player.getPacketDispatcher().sendString(4903, line1);
		player.getPacketDispatcher().sendString(4904, line2);
		player.getPacketDispatcher().sendString(4905, line3);
		player.getPacketDispatcher().sendString(4906, line4);
		player.getPacketDispatcher().sendNPCDialogueHead(def.getId(), 4901);
		player.getPacketDispatcher().sendChatInterface(4900);
	}

	/**
	 * Timed NPC dialogue. These NPC dialogues have no close/click options, so
	 * should only be used with a timer.
	 */
	public void sendTimedNpcChat(String line1, String line2, int emotion) {
		NPC npc = (NPC) player.getInteractingEntity();
		NPCDefinition def = NPCDefinition.getDefinitions()[npc.getNpcId()];
		String npcName = def.getName();
		player.getPacketDispatcher().sendDialogueAnimation(12379, emotion);
		player.getPacketDispatcher().sendString(12380, npcName);
		player.getPacketDispatcher().sendString(12381, line1);
		player.getPacketDispatcher().sendString(12382, line2);
		player.getPacketDispatcher().sendNPCDialogueHead(def.getId(), 12379);
		player.getPacketDispatcher().sendChatInterface(12378);
	}

	public void sendTimedNpcChat(String line1, String line2, String line3,
			int emotion) {
		NPC npc = (NPC) player.getInteractingEntity();
		NPCDefinition def = NPCDefinition.getDefinitions()[npc.getNpcId()];
		String npcName = def.getName();
		player.getPacketDispatcher().sendDialogueAnimation(12384, emotion);
		player.getPacketDispatcher().sendString(12385, npcName);
		player.getPacketDispatcher().sendString(12386, line1);
		player.getPacketDispatcher().sendString(12387, line2);
		player.getPacketDispatcher().sendString(12388, line3);
		player.getPacketDispatcher().sendNPCDialogueHead(def.getId(), 12384);
		player.getPacketDispatcher().sendChatInterface(12383);
	}

	public void sendTimedNpcChat(String line1, String line2, String line3,
			String line4, int emotion) {
		NPC npc = (NPC) player.getInteractingEntity();
		NPCDefinition def = NPCDefinition.getDefinitions()[npc.getNpcId()];
		String npcName = def.getName();
		player.getPacketDispatcher().sendDialogueAnimation(11892, emotion);
		player.getPacketDispatcher().sendString(11893, npcName);
		player.getPacketDispatcher().sendString(11894, line1);
		player.getPacketDispatcher().sendString(11895, line2);
		player.getPacketDispatcher().sendString(11896, line3);
		player.getPacketDispatcher().sendString(11897, line4);
		player.getPacketDispatcher().sendNPCDialogueHead(def.getId(), 11892);
		player.getPacketDispatcher().sendChatInterface(11891);
	}

	/**
	 * Player dialogue.
	 */
	public void sendPlayerChat(String line1, int emotion) {
		player.getPacketDispatcher().sendDialogueAnimation(969, emotion);
		player.getPacketDispatcher().sendString(970,
				player.getAuthentication().getUsername());
		player.getPacketDispatcher().sendString(971, line1);
		player.getPacketDispatcher().sendPlayerDialogueHead(969);
		player.getPacketDispatcher().sendChatInterface(968);
	}

	public void sendPlayerChat(String line1, String line2, int emotion) {
		player.getPacketDispatcher().sendDialogueAnimation(974, emotion);
		player.getPacketDispatcher().sendString(975,
				player.getAuthentication().getUsername());
		player.getPacketDispatcher().sendString(976, line1);
		player.getPacketDispatcher().sendString(977, line2);
		player.getPacketDispatcher().sendPlayerDialogueHead(974);
		player.getPacketDispatcher().sendChatInterface(973);
	}

	public void sendPlayerChat(String line1, String line2, String line3,
			int emotion) {
		player.getPacketDispatcher().sendDialogueAnimation(980, emotion);
		player.getPacketDispatcher().sendString(981,
				player.getAuthentication().getUsername());
		player.getPacketDispatcher().sendString(982, line1);
		player.getPacketDispatcher().sendString(983, line2);
		player.getPacketDispatcher().sendString(984, line3);
		player.getPacketDispatcher().sendPlayerDialogueHead(980);
		player.getPacketDispatcher().sendChatInterface(979);
	}

	public void sendPlayerChat(String line1, String line2, String line3,
			String line4, int emotion) {
		player.getPacketDispatcher().sendDialogueAnimation(987, emotion);
		player.getPacketDispatcher().sendString(988,
				player.getAuthentication().getUsername());
		player.getPacketDispatcher().sendString(989, line1);
		player.getPacketDispatcher().sendString(990, line2);
		player.getPacketDispatcher().sendString(991, line3);
		player.getPacketDispatcher().sendString(992, line4);
		player.getPacketDispatcher().sendPlayerDialogueHead(987);
		player.getPacketDispatcher().sendChatInterface(986);
	}

	/**
	 * Timed player dialogue. These player dialogues have no close/click
	 * options, so should only be used with a timer.
	 */
	public void sendTimedPlayerChat(String line1, int emotion) {
		player.getPacketDispatcher().sendDialogueAnimation(12774, emotion);
		player.getPacketDispatcher().sendString(12775,
				player.getAuthentication().getUsername());
		player.getPacketDispatcher().sendString(12776, line1);
		player.getPacketDispatcher().sendPlayerDialogueHead(12774);
		player.getPacketDispatcher().sendChatInterface(12773);
	}

	public void sendTimedPlayerChat(String line1, String line2, int emotion) {
		player.getPacketDispatcher().sendDialogueAnimation(12778, emotion);
		player.getPacketDispatcher().sendString(12779,
				player.getAuthentication().getUsername());
		player.getPacketDispatcher().sendString(12780, line1);
		player.getPacketDispatcher().sendString(12781, line2);
		player.getPacketDispatcher().sendPlayerDialogueHead(12778);
		player.getPacketDispatcher().sendChatInterface(12777);
	}

	public void sendTimedPlayerChat(String line1, String line2, String line3,
			int emotion) {
		player.getPacketDispatcher().sendDialogueAnimation(12783, emotion);
		player.getPacketDispatcher().sendString(12784,
				player.getAuthentication().getUsername());
		player.getPacketDispatcher().sendString(12785, line1);
		player.getPacketDispatcher().sendString(12786, line2);
		player.getPacketDispatcher().sendString(12787, line3);
		player.getPacketDispatcher().sendPlayerDialogueHead(12783);
		player.getPacketDispatcher().sendChatInterface(12782);
	}

	public void sendTimedPlayerChat(String line1, String line2, String line3,
			String line4, int emotion) {
		player.getPacketDispatcher().sendDialogueAnimation(11885, emotion);
		player.getPacketDispatcher().sendString(11886,
				player.getAuthentication().getUsername());
		player.getPacketDispatcher().sendString(11887, line1);
		player.getPacketDispatcher().sendString(11888, line2);
		player.getPacketDispatcher().sendString(11889, line3);
		player.getPacketDispatcher().sendString(11890, line4);
		player.getPacketDispatcher().sendPlayerDialogueHead(11885);
		player.getPacketDispatcher().sendChatInterface(11884);
	}

	private static DialogueDefinition[] def = null;

	public static void load() throws FileNotFoundException {
            Server.logger.info("Loading dialogues...");
		@SuppressWarnings("unchecked")
		List<DialogueDefinition> XMLlist = (List<DialogueDefinition>) XStreamUtil
				.getXStream()
				.fromXML(
						new FileInputStream("./data/xml/dialogue/dialogue.xml"));
		def = new DialogueDefinition[XMLlist.size()];
		for (int i = 0; i < def.length; i++) {
			def[i] = XMLlist.get(i);
		}
	}

	public static final int CONTENT = 591, EVIL = 592, SAD = 596, SLEEPY = 603,
			LAUGHING = 605, MOURNING = 610, MAD = 614;

}
