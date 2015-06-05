/*
 * This file is part of Solace Framework.
 * Solace is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * Solace is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Solace. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package org.solace.game.content.dialogue;

/**
 * Loads the definitions for the dialogue from the XML file
 * 
 * @author Arithium
 * 
 */
public class DialogueDefinition {

	private int dialogueId;

	private int nextDialogue;

	private int emoticon;

	private boolean playerChat;

	private String[] dialogues;

	public int dialogueId() {
		return dialogueId;
	}

	public int getNextDialogue() {
		return nextDialogue;
	}

	public int getEmoticon() {
		return emoticon;
	}

	public String[] getDialogues() {
		return dialogues;
	}

	public boolean isPlayerChat() {
		return playerChat;
	}

}
