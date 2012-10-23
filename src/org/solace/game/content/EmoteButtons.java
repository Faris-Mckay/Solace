package org.solace.game.content;

import org.solace.game.entity.mobile.player.Player;
import org.solace.util.Constants;

/**
 *
 * @author Faris
 */
public class EmoteButtons {
    
    /**
     * Stores the emotes animation id
     */
    private static final int
            YES = 855,
            NO = 856, 
            THINK = 857,
            BOW = 858,
            ANGRY = 859,
            CRY = 860,
            LAUGH = 861,
            CHEER = 862,
            WAVE = 863,
            BECKON = 864,
            CLAP = 865,
            DANCE = 866, 
            PANIC = 2105,
            JIG = 2106,
            SPIN = 2107,
            HEADBANG = 2108,
            JOYJUMP = 2109,
            RASPBERRY = 2110,
            YAWN = 2111, 
            SALUTE = 2112,
            SHRUG = 2113,
            BLOWKISS = 0x558,

            // Unlockable emotes
            GLASSBOX = 0x46B,
            CLIMBROPE = 0x46A,
            LEAN = 0x469,
            GLASSWALL = 0x468,
            GOBLINBOW = 0x850,
            GOBLINDANCE = 0x850,
            SCARED = 2836,
            ZOMBIEWALK = 3544,
            ZOMBIEDANCE = 3543,
            RABBITHOP = 3866;

    public static boolean unlocked[] = {false, false, false, false, false, false, false, false, false, false};
    public static int unlockedId[] = { 2155, 25103, 25106, 2154, 52071, 52072, 59062, 72032, 72033, 72254};
    
    public static final String UNLOCK_ERROR_MESSAGE[] = {
            "the Mime random event.",
            "the Mime random event.",
            "the Mime random event.",
            "during the Mime random event.",
            "the Lost Tribe quest.",
            "the Lost Tribe quest.",
            "a Halloween event.",
            "the Gravedigger random event.",
            "the Gravedigger random event.",
            "an Easter event."
    };

    public static void performAnimation(Player player, int id) {
                    player.getAdvocate().queueAnim(id);
                    player.getAdvocate().haltMovement();
    }

    public static void handleLockedEmotes(Player player, int actionButtonId) {
            for (int i = 0; i < unlockedId.length; i++) {
                    if (unlockedId[i] == actionButtonId && !unlocked[i]) {
                        if(Constants.UNLOCKABLE_EMOTES_ENABLED){
                            handleUnlocked(player, i);
                            return;
                        }
                        player.getAdvocate().displayChatboxText("This emote can be unlocked during " +UNLOCK_ERROR_MESSAGE[i]);
                    } else if (unlockedId[i] == actionButtonId && unlocked[i]) {
                        handleUnlocked(player, i);
                    }
            }
            player.getAdvocate().haltMovement();
    }

    public static void handleUnlocked(Player player, int id) {
            switch (id) {
                case 0: 
                    performAnimation(player, GLASSBOX);
                    break;
                case 1: 
                    performAnimation(player, CLIMBROPE);
                    break;
                case 2: 
                    performAnimation(player, LEAN);
                    break;
                case 3: 
                    performAnimation(player, GLASSWALL);
                    break;
                case 4: 
                    performAnimation(player, GOBLINBOW);
                    break;
                case 5: 
                    performAnimation(player, GOBLINDANCE);
                    break;
                case 6: 
                    performAnimation(player, SCARED);
                    break;
                case 7: 
                    performAnimation(player, ZOMBIEWALK);
                    break;
                case 8: 
                    performAnimation(player, ZOMBIEDANCE);
                    break;
                case 9: 
                    performAnimation(player, RABBITHOP);
                    break;
            }
    }

    public static void handle(Player player, int actionButtonId) {
            switch (actionButtonId) {
                    case 168: performAnimation(player, YES);
                            break;
                    case 169: performAnimation(player, NO);
                            break;
                    case 162: performAnimation(player, THINK);
                            break;
                    case 164: performAnimation(player, BOW);
                            break;
                    case 165: performAnimation(player, ANGRY);
                            break;
                    case 161: performAnimation(player, CRY);
                            break;
                    case 170: performAnimation(player, LAUGH);
                            break;
                    case 171: performAnimation(player, CHEER);
                            break;
                    case 163: performAnimation(player, WAVE);
                            break;
                    case 167: performAnimation(player, BECKON);
                            break;
                    case 172: performAnimation(player, CLAP);
                            break;
                    case 166: performAnimation(player, DANCE);
                            break;
                    case 52050: performAnimation(player, PANIC);
                            break;
                    case 52051: performAnimation(player, JIG);
                            break;
                    case 52052: performAnimation(player, SPIN);
                            break;
                    case 52053: performAnimation(player, HEADBANG);
                            break;
                    case 52054: performAnimation(player, JOYJUMP);
                            break;
                    case 52055: performAnimation(player, RASPBERRY);
                            break;
                    case 52056: performAnimation(player, YAWN);
                            break;
                    case 52057: performAnimation(player, SALUTE);
                            break;
                    case 52058: performAnimation(player, SHRUG);
                            break;
                    case 43092: performAnimation(player, BLOWKISS);
                            break;
                    default: handleLockedEmotes(player, actionButtonId);
                            break;
                    }
            }



}
