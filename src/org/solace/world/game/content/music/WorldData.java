package org.solace.world.game.content.music;
 
import org.solace.world.game.entity.mobile.player.Player;
import org.solace.world.map.Location;

/**
 * 
 * @author Faris
 * 
 */
 
public class WorldData {
     
    // 1 = Tut
    // 2 = CW SPawn Room
    // 3 = Barrows under
    // 4 = Barrows top
    // 5 = Wildy
    // 6 = Ardy
    // 7 = Edgy
    // 8 = Wiz Tower
    // 9 = PC
    // 10 = Al Kharid
    // 11 = Lumbridge
    // 12 = Falador
    // 13 = Fight Caves
    // 14 = Pirate House
    // 15 = Barbarian Village
    // 16 = The Abyss
    // 17 = Ape Atoll
    // 18 = Bandit Camp
    // 19 = Barbarian Agility
    // 20 = Bedabin Camp
    // 21 = Crash Island
    // 22 = Catherby
    // 23 = White Wolf Moutain
    // 24 = Burthrope
    // 25 = Camelot
    // 26 = Canfis
    // 27 = Random Event Maze
    // 28 = Crandor
    // 29 = Draynor
    // 30 = Duel Arena
    // 31 = Entrana
    // 32 = Gnome Village
    // 33 = Goblin Village
    // 34 = HAM Base
    // 35 = Karamja
    // 36 = Lost & Found
    // 37 = Miscellania
    // 38 = Morton
    // 39 = Phasmatys
    // 40 = Port Sarim
    // 41 = Rimmington
    // 42 = Seers Village
    // 43 = Shilo Village
    // 44 = Taverly
    // 45 = Gnome Stronghold
    // 46 = Tzhaar
    // 47 = Varrock
    // 48 = Yanille
    // 49 = Castle Wars Game Area
    // 50 = Castle Wars Lobby
    // 51 = Castle Wars Underground
    // 52 = King Black Dragon  Lair
    // 53 = Kalphite Lair
     

    /**
     * Sets area id by integer in MusicHandler
     * @param c
     * @return
     */
     public int getAreaID(Player player) {
            if (player.getLocation().getX() >= 2625 && player.getLocation().getX() <= 2687 && player.getLocation().getY() >= 4670 && player.getLocation().getY() <= 4735) 
                        return 1;
            if ((player.getLocation().getX() >= 2368 && player.getLocation().getX() <= 2376 && player.getLocation().getY() >= 3127 && player.getLocation().getY() <= 3135 && player.getLocation().getH() == 1) ||
                    (player.getLocation().getX() >= 2423 && player.getLocation().getX() <= 2431 && player.getLocation().getY() >= 3072 && player.getLocation().getY() <= 3080 && player.getLocation().getH() == 1)) 
                        return 2;
            if (player.getLocation().getX() > 3520 && player.getLocation().getX() < 3598 && player.getLocation().getY() > 9653 && player.getLocation().getY() < 9750) 
                        return 3;
            if (player.getLocation().getX() >= 3542 && player.getLocation().getX() <= 3583 && player.getLocation().getY() >= 3265 && player.getLocation().getY() <= 3322) 
                        return 4;
            if(player.getLocation().getX() > 2941 && player.getLocation().getX() < 3392 && player.getLocation().getY() > 3518 && player.getLocation().getY() < 3966 ||
                    player.getLocation().getX() > 3343 && player.getLocation().getX() < 3384 && player.getLocation().getY() > 9619 && player.getLocation().getY() < 9660 ||
                    player.getLocation().getX() > 2941 && player.getLocation().getX() < 3392 && player.getLocation().getY() > 9918 && player.getLocation().getY() < 10366)  
                        return 5;
            if (player.getLocation().getX() > 2558 && player.getLocation().getX() < 2729 && player.getLocation().getY() > 3263 && player.getLocation().getY() < 3343) 
                        return 6;
            if (player.getLocation().getX() > 3084 && player.getLocation().getX() < 3111 && player.getLocation().getY() > 3483 && player.getLocation().getY() < 3509) 
                        return 7;
            if (player.getLocation().getX() > 2935 && player.getLocation().getX() < 3061 && player.getLocation().getY() > 3308 && player.getLocation().getY() < 3396) 
                        return 8;
            if (player.getLocation().getX() >= 2659 && player.getLocation().getX() <= 2664 && player.getLocation().getY() >= 2637 && player.getLocation().getY() <= 2644 || 
                    player.getLocation().getX() >= 2623 && player.getLocation().getX() <= 2690 && player.getLocation().getY() >= 2561 && player.getLocation().getY() <= 2688) 
                        return 9;
            if (player.getLocation().getX() > 3270 && player.getLocation().getX() < 3455 && player.getLocation().getY() > 2880 && player.getLocation().getY() < 3330) 
                        return 10;
            if (player.getLocation().getX() > 3187 && player.getLocation().getX() < 3253 && player.getLocation().getY() > 3189 && player.getLocation().getY() < 3263) 
                        return 11;
            if (player.getLocation().getX() > 3002 && player.getLocation().getX() < 3004 && player.getLocation().getY() > 3002 && player.getLocation().getY() < 3004) 
                        return 12;
            if (player.getLocation().getX() >= 2360 && player.getLocation().getX() <= 2445 && player.getLocation().getY() >= 5045 && player.getLocation().getY() <= 5125) 
                        return 13;
            if (player.getLocation().getX() >= 3038 && player.getLocation().getX() <= 3044 && player.getLocation().getY() >= 3949 && player.getLocation().getY() <= 3959) 
                        return 14;
            if (player.getLocation().getX() >= 3060 && player.getLocation().getX() <= 3099 && player.getLocation().getY() >= 3399 && player.getLocation().getY() <= 3450) 
                        return 15;
            if (player.getLocation().getX() >= 3008 && player.getLocation().getX() <= 3071 && player.getLocation().getY() >= 4800 && player.getLocation().getY() <= 4863) 
                        return 16;
            if (player.getLocation().getX() >= 2691 && player.getLocation().getX() <= 2826 && player.getLocation().getY() >= 2690 && player.getLocation().getY() <= 2831) 
                        return 17;
            if (player.getLocation().getX() >= 3155 && player.getLocation().getX() <= 3192 && player.getLocation().getY() >= 2962 && player.getLocation().getY() <= 2994) 
                        return 18;
            if (player.getLocation().getX() >= 2526 && player.getLocation().getX() <= 2556 && player.getLocation().getY() >= 3538 && player.getLocation().getY() <= 3575) 
                        return 19;
            if (player.getLocation().getX() >= 3165 && player.getLocation().getX() <= 3199 && player.getLocation().getY() >= 3022 && player.getLocation().getY() <= 3054) 
                        return 20;
            if (player.getLocation().getX() >= 2785 && player.getLocation().getX() <= 2804 && player.getLocation().getY() >= 3312 && player.getLocation().getY() <= 3327) 
                        return 21;
            if ((player.getLocation().getX() >= 2792 && player.getLocation().getX() <= 2829 && player.getLocation().getY() >= 3412 && player.getLocation().getY() <= 3472) ||
                (player.getLocation().getX() > 2828 && player.getLocation().getX() < 2841 && player.getLocation().getY() > 3430 && player.getLocation().getY() < 3459) ||
                (player.getLocation().getX() > 2839 && player.getLocation().getX() < 2861 && player.getLocation().getY() > 3415 && player.getLocation().getY() < 3441))
                        return 22;
            if (player.getLocation().getX() >= 2850 && player.getLocation().getX() <= 2879 && player.getLocation().getY() >= 3446 && player.getLocation().getY() <= 3522)
                return 23;
            if (player.getLocation().getX() >= 2878 && player.getLocation().getX() <= 2937 && player.getLocation().getY() >= 3524 && player.getLocation().getY() <= 3582) 
                return 24;
            if (player.getLocation().getX() >= 2744 && player.getLocation().getX() <= 2787 && player.getLocation().getY() >= 3457 && player.getLocation().getY() <= 3519) 
                return 25;
            if (player.getLocation().getX() >= 3425 && player.getLocation().getX() <= 3589 && player.getLocation().getY() >= 3256 && player.getLocation().getY() <= 3582) 
                return 26;
            if (player.getLocation().getX() >= 2883 && player.getLocation().getX() <= 2942 && player.getLocation().getY() >= 4547 && player.getLocation().getY() <= 4605) 
                return 27;
            if (player.getLocation().getX() >= 2819 && player.getLocation().getX() <= 2859 && player.getLocation().getY() >= 3224 && player.getLocation().getY() <= 3312) 
                return 28;
            if (player.getLocation().getX() >= 3067 && player.getLocation().getX() <= 3134 && player.getLocation().getY() >= 3223 && player.getLocation().getY() <= 3297) 
                return 29;
            if (player.getLocation().getX() >= 3324 && player.getLocation().getX() <= 3392 && player.getLocation().getY() >= 3196 && player.getLocation().getY() <= 3329) 
                return 30;
            if (player.getLocation().getX() >= 2800 && player.getLocation().getX() <= 2869 && player.getLocation().getY() >= 3324 && player.getLocation().getY() <= 3391) 
                return 31;
            if (player.getLocation().getX() >= 2492 && player.getLocation().getX() <= 2563 && player.getLocation().getY() >= 3132 && player.getLocation().getY() <= 3203) 
                return 32;
            if (player.getLocation().getX() >= 2945 && player.getLocation().getX() <= 2968 && player.getLocation().getY() >= 3477 && player.getLocation().getY() <= 3519) 
                return 33;
            if (player.getLocation().getX() >= 3136 && player.getLocation().getX() <= 3193 && player.getLocation().getY() >= 9601 && player.getLocation().getY() <= 9664) 
                return 34;
            if (player.getLocation().getX() >= 2816 && player.getLocation().getX() <= 2958 && player.getLocation().getY() >= 3139 && player.getLocation().getY() <= 3175) 
                return 35;
            if (player.getLocation().getX() >= 2334 && player.getLocation().getX() <= 2341 && player.getLocation().getY() >= 4743 && player.getLocation().getY() <= 4751) 
                return 36;
            if (player.getLocation().getX() >= 2495 && player.getLocation().getX() <= 2625 && player.getLocation().getY() >= 3836 && player.getLocation().getY() <= 3905) 
                return 37;
            if (player.getLocation().getX() >= 3465 && player.getLocation().getX() <= 3520 && player.getLocation().getY() >= 3266 && player.getLocation().getY() <= 3309) 
                return 38;
            if (player.getLocation().getX() >= 3585 && player.getLocation().getX() <= 3705 && player.getLocation().getY() >= 3462 && player.getLocation().getY() <= 3539) 
                return 39;
            if (player.getLocation().getX() >= 2985 && player.getLocation().getX() <= 3064 && player.getLocation().getY() >= 3164 && player.getLocation().getY() <= 3261) 
                return 40;
            if (player.getLocation().getX() >= 2913 && player.getLocation().getX() <= 2989 && player.getLocation().getY() >= 3185 && player.getLocation().getY() <= 3267) 
                return 41;
            if (player.getLocation().getX() >= 2639 && player.getLocation().getX() <= 2740 && player.getLocation().getY() >= 3391 && player.getLocation().getY() <= 3503) 
                return 42;
            if (player.getLocation().getX() >= 2816 && player.getLocation().getX() <= 2879 && player.getLocation().getY() >= 2946 && player.getLocation().getY() <= 3007) 
                return 43;
            if (player.getLocation().getX() >= 2874 && player.getLocation().getX() <= 2934 && player.getLocation().getY() >= 3390 && player.getLocation().getY() <= 3492) 
                return 44;
            if (player.getLocation().getX() >= 2413 && player.getLocation().getX() <= 2491 && player.getLocation().getY() >= 3386 && player.getLocation().getY() <= 3515) 
                return 45;
            if (player.getLocation().getX() >= 2431 && player.getLocation().getX() <= 2495 && player.getLocation().getY() >= 5117 && player.getLocation().getY() <= 5180) 
                return 46;
            if (player.getLocation().getX() >= 3168 && player.getLocation().getX() <= 3291 && player.getLocation().getY() >= 3349 && player.getLocation().getY() <= 3514) 
                return 47;
            if (player.getLocation().getX() >= 2532 && player.getLocation().getX() <= 2621 && player.getLocation().getY() >= 3071 && player.getLocation().getY() <= 3112) 
                return 48;
            if (player.getLocation().getX() >= 2368 && player.getLocation().getX() <= 2430 && player.getLocation().getY() >= 3073 && player.getLocation().getY() <= 3135) 
                return 49;
            if (player.getLocation().getX() >= 2440 && player.getLocation().getX() <= 2444 && player.getLocation().getY() >= 3083 && player.getLocation().getY() <= 3095) 
                return 50;
            if (player.getLocation().getX() >= 2359 && player.getLocation().getX() <= 2440 && player.getLocation().getY() >= 9466 && player.getLocation().getY() <= 9543) 
                return 51;
            if (player.getLocation().getX() >= 2251 && player.getLocation().getX() <= 2295 && player.getLocation().getY() >= 4675 && player.getLocation().getY() <= 4719) 
                return 52;
            if (player.getLocation().getX() >= 3463 && player.getLocation().getX() <= 3515 && player.getLocation().getY() >= 9469 && player.getLocation().getY() <= 9524) 
                return 53;
            if (player.getLocation().getX() >= 3200 && player.getLocation().getX() <= 3303 && player.getLocation().getY() >= 3273 && player.getLocation().getY() <= 3353) 
                return 54;;
            if (player.getLocation().getX() >= 3274 && player.getLocation().getX() <= 3328 && player.getLocation().getY() >= 3315 && player.getLocation().getY() <= 3353) 
                return 55;
            if (player.getLocation().getX() >= 3274 && player.getLocation().getX() <= 3266 && player.getLocation().getY() >= 3323 && player.getLocation().getY() <= 3327) 
                return 56;
            if (player.getLocation().getX() >= 3274 && player.getLocation().getX() <= 3200 && player.getLocation().getY() >= 3323 && player.getLocation().getY() <= 3265) 
                return 57;
            if (player.getLocation().getX() >= 3324 && player.getLocation().getX() <= 3263 && player.getLocation().getY() >= 3408 && player.getLocation().getY() <= 3285) 
                return 58;
            if (player.getLocation().getX() >= 3324 && player.getLocation().getX() <= 3286 && player.getLocation().getY() >= 3408 && player.getLocation().getY() <= 3327) 
                return 59;
            if (player.getLocation().getX() >= 3136 && player.getLocation().getX() <= 3136 && player.getLocation().getY() >= 3193 && player.getLocation().getY() <= 3199) 
                return 60; 
            if (player.getLocation().getX() >= 3121 && player.getLocation().getX() <= 3200 && player.getLocation().getY() >= 3199 && player.getLocation().getY() <= 3268) 
                return 61; 
            if (player.getLocation().getX() >= 3121 && player.getLocation().getX() <= 3269 && player.getLocation().getY() >= 3199 && player.getLocation().getY() <= 3314)
                return 62;
            if (player.getLocation().getX() >= 3066 && player.getLocation().getX() <= 3315 && player.getLocation().getY() >= 3147 && player.getLocation().getY() <= 3394) 
                return 63;
            if (player.getLocation().getX() >= 3200 && player.getLocation().getX() <= 3354 && player.getLocation().getY() >= 3315 && player.getLocation().getY() <= 3394) 
                return 64;
            if (player.getLocation().getX() >= 3248 && player.getLocation().getX() <= 3395 && player.getLocation().getY() >= 3328 && player.getLocation().getY() <= 3468) 
                return 65;
            if (player.getLocation().getX() >= 3111 && player.getLocation().getX() <= 3469 && player.getLocation().getY() >= 3264 && player.getLocation().getY() <= 3524) 
                return 66;
            if (player.getLocation().getX() >= 3265 && player.getLocation().getX() <= 3469 && player.getLocation().getY() >= 3328 && player.getLocation().getY() <= 3524) 
                return 67;
            if (player.getLocation().getX() >= 3329 && player.getLocation().getX() <= 3447 && player.getLocation().getY() >= 3418 && player.getLocation().getY() <= 3524) 
                return 68;
            if (player.getLocation().getX() >= 2889 && player.getLocation().getX() <= 3265 && player.getLocation().getY() >= 2940 && player.getLocation().getY() <= 3324) 
                return 69;
            if (player.getLocation().getX() >= 3014 && player.getLocation().getX() <= 3261 && player.getLocation().getY() >= 3065 && player.getLocation().getY() <= 3324) 
                return 70; 
            if (player.getLocation().getX() >= 2880 && player.getLocation().getX() <= 3325 && player.getLocation().getY() >= 2935 && player.getLocation().getY() <= 3394) 
                return 71; 
        return 0;
    }
 
}