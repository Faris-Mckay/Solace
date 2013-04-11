package org.solace.network.packet.impl;

import org.solace.game.content.EmoteButtons;
import org.solace.game.content.TeleportHandler;
import org.solace.game.content.combat.PrayerHandler;
import org.solace.game.content.combat.magic.MagicExtras;
import org.solace.game.entity.mobile.Mobile.AttackStyle;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.mobile.player.PlayerSettings;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketHandler;

/**
 * 
 * @author Faris
 * 
 */
public class ActionButtonPacket implements PacketHandler {

	@Override
	public void handlePacket(Player player, Packet packet) {
		int buttonId = packet.getShort();
		player.getPacketDispatcher().sendMessage("ID: " + buttonId);
		PrayerHandler.togglePrayer(player, buttonId);
		PlayerSettings.handleButtons(player, buttonId);
		TeleportHandler.processModernTeleport(player, buttonId);
		if (MagicExtras.isAutoButton(buttonId)) {
			MagicExtras.assignAutocast(player, buttonId);
		}
		MagicExtras.handleActionButtons(player, buttonId);
		switch (buttonId) {
		case 2458:
			player.handleLogoutData();
			break;
		case 3651:
			player.getPacketDispatcher().sendCloseInterface();
			break;
		case 7562:
		case 12311:
		case 7587:
		case 7537:
			player.getEquipment().setUsingSpecial(!player.getEquipment().isUsingSpecial());
			player.getEquipment().updateSpecialBar(player);
			player.getEquipment().sendWeapon(player);
			break;
		case 168:
		case 169:
		case 162:
		case 164:
		case 165:
		case 161:
		case 170:
		case 171:
		case 163:
		case 167:
		case 172:
		case 166:
		case 52050:
		case 52051:
		case 52052:
		case 52053:
		case 52054:
		case 52055:
		case 52056:
		case 52057:
		case 52058:
		case 43092:
		case 2155:
		case 25103:
		case 25106:
		case 2154:
		case 52071:
		case 52072:
		case 59062:
		case 72032:
		case 72033:
		case 72254:
			EmoteButtons.handle(player, buttonId);
			break;
			
		case 9126: // Defensive
		case 48008: // deflect (whip)
			// case 22229: //block (unarmed)
		case 21201: // block (pickaxe)
		case 1078: // focus - block (staff)
		case 6169: // block (axe)
		case 33019: // fend (hally)
		case 18078: // block (spear)
		case 8235: // block (dagger)
		case 12296:
		case 2283:
		case 2430:
		case 1705:
		case 5861:
			player.setAttackStyle(AttackStyle.DEFENSIVE);
			break;

		case 9128: // Aggressive
		case 6220: // range rapid
			// case 22230: //kick (unarmed)
		case 21203: // impale (pickaxe)
		case 21202: // smash (pickaxe)
		case 1079: // pound (staff)
		case 6171: // hack (axe)
		case 6170: // smash (axe)
		case 33020: // swipe (hally)
		case 6235: // rapid (long bow)
		case 17101: // repid (darts)
		case 8237: // lunge (dagger)
		case 8236: // slash (dagger)
		case 1771:
		case 2285:
		case 2284:
		case 2432:
		case 1707:
		case 1706:
		case 5862:
		case 22230: // kick (unarmed
			player.setAttackStyle(AttackStyle.AGGRESSIVE);
			break;

		case 9127: // Controlled
		case 48009: // lash (whip)
		case 33018: // jab (hally)
		case 6234: // longrange (long bow)
		case 6219: // longrange
		case 18077: // lunge (spear)
		case 18080: // swipe (spear)
		case 18079: // pound (spear)
		case 17100: // longrange (darts)
		case 12297:
		case 1770:
		case 2431:
			player.setAttackStyle(AttackStyle.CONTROLLED);
			break;

		case 22228: // punch (unarmed
		case 9125: // Accurate
		case 6221: // range accurate
			// case 22228: //punch (unarmed)
		case 48010: // flick (whip)
		case 21200: // spike (pickaxe)
		case 1080: // bash (staff)
		case 6168: // chop (axe)
		case 6236: // accurate (long bow)
		case 17102: // accurate (darts)
		case 8234: // stab (dagger)
		case 12298:
		case 1772:
		case 2282:
		case 2249:
		case 1704:
		case 5860:
			player.setAttackStyle(AttackStyle.ACCURATE);
			break;
		}

	}

}
