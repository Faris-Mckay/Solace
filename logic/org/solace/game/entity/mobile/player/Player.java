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
package org.solace.game.entity.mobile.player;

import org.jboss.netty.channel.Channel;
import org.solace.game.entity.mobile.update.impl.PlayerUpdateTask;
import org.solace.Server;
import org.solace.event.events.PlayerDisconnectionEvent;
import org.solace.event.events.PlayerLoginEvent;
import org.solace.event.events.PlayerLogoutEvent;
import org.solace.event.impl.PlayerDeathService;
import org.solace.game.content.PrivateMessaging;
import org.solace.game.content.combat.Combat;
import org.solace.game.content.combat.PrayerHandler;
import org.solace.game.content.combat.PrayerHandler.Prayer;
import org.solace.game.content.combat.impl.Hit;
import org.solace.game.content.dialogue.Dialogue;
import org.solace.game.content.skills.SkillHandler;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.update.impl.NPCUpdateTask;
import org.solace.game.item.container.impl.Banking;
import org.solace.game.item.container.impl.Equipment;
import org.solace.game.item.container.impl.Inventory;
import org.solace.game.map.Location;
import org.solace.game.map.Region;
import org.solace.network.Session;
import org.solace.network.packet.Packet;
import org.solace.network.packet.PacketBuilder;
import org.solace.network.packet.PacketDispatcher;
import org.solace.network.util.Stream;
import org.solace.task.Task;

/**
 *
 * @author Faris
 */
public class Player extends Mobile {

    /**
     * Player stored objects
     */
    private Task walkToAction;
    private PlayerAuthentication authenticator;
    private PacketDispatcher packetDispatcher = new PacketDispatcher(this);
    private PrivateMessaging playerMessaging = new PrivateMessaging(this);
    private PlayerUpdateTask updating = new PlayerUpdateTask(this);
    private Equipment equipment = new Equipment(this);
    private Inventory inventory = new Inventory(this);
    private SkillHandler skills = new SkillHandler(this);
    private NPCUpdateTask npcUpdating = new NPCUpdateTask(this);
    private SkillHandler skillHandler = new SkillHandler(this);
    private PlayerAdvocate advocate = new PlayerAdvocate(this);
    private PrayerHandler prayerHandler = new PrayerHandler();
    private Dialogue dialogue = new Dialogue(this);
    private PlayerSettings settings = new PlayerSettings();
    private Banking banking = new Banking(this);


    /**
     * The cached update block.
     */
    private PacketBuilder cachedUpdateBlock;
    public boolean disconnected;
    public boolean updateRequired;
    private Stream outStream;
    private Region cachedRegionNew;
    private boolean appearanceUpdateRequired;

    /**
     * Initialises the attributes
     */
    public void appendLoginAttributes() {
        addAttribute("NO_THIEVE", Boolean.FALSE);
        addAttribute("STUNNED", Boolean.FALSE);
        addAttribute("TELEPORTING", Boolean.FALSE);
        addAttribute("FROZEN", Boolean.FALSE);
        addAttribute("IMMUNE", Boolean.FALSE);
    }

    /**
     * Player stored primitives
     */
    private int playerHeadIcon = -1;
    private boolean buryingBones;
    private boolean logoutRequired = false;
    private double prayerPoint = 1.0;
    private int[] bonuses = new int[12];
    private int spellId;
    private int specialAmount = 100;
    private int specialBarId;
    private int prayerIcon = -1;
    private Session session;
    private double prayerDrainRate;
    public long foodDelay;
    private boolean genuineDisconnection = false, disconnectionHandled;

    public Player(Channel channel, String username, String password) {
        super(new Location(3200,3200));
        this.authenticator = new PlayerAuthentication(username, password);
        setDefaultAppearance();
      
        getUpdateFlags().flag(UpdateFlag.APPEARANCE);
        this.getAuthentication().setPlayerRights(PrivilegeRank.OWNER);
    }

    /**
     * Can be used for content implementations but only when absolutely
     * necessary, this is NOT to be used as the process in PI
     */
    @Override
    public void update() {
        getMobilityManager().processMovement();
        if (getStatus() != WelfareStatus.DEAD) {
            Combat.handleCombatTick(this);
            if (inWild()) {
                getPacketDispatcher().sendWalkableInterface(197);
                getPacketDispatcher().sendString(199, "@yel@Level: " + getWildernessLevel());
            }
            PrayerHandler.handlePrayerDraining(this);
            getSkills().handleSkillRestoring();
        }
    }

    /**
     * Returns the packet dispatcher
     *
     * @return
     */
    public PacketDispatcher getPacketDispatcher() {
        return packetDispatcher;
    }

    /**
     * Schedules a new login event for this player
     */
    public void handleLoginData() {
        Server.getEventManager().dispatchEvent(new PlayerLoginEvent(this));
    }

    /**
     * Schedules a new logout event event for this player
     */
    public void handleLogoutData() {
        setGenuineDisconnection(true);
        if (System.currentTimeMillis() - getCombatDelay() > 10000) {
            Server.getEventManager().dispatchEvent(new PlayerLogoutEvent(this));
        } else {
            getPacketDispatcher().sendMessage("You must wait a few seconds before loggout out of combat.");
        }
    }
    
    public void handleDisconnection(){
        if(!isDisconnectionHandled()){
            Server.getEventManager().dispatchEvent(new PlayerDisconnectionEvent(this));
            setDisconnectionHandled(true);
        }
    }

    /**
     * @return the playerMessaging
     */
    public PrivateMessaging getPrivateMessaging() {
        return playerMessaging;
    }

    /**
     * @return the playerCredentials
     */
    public PlayerAuthentication getAuthentication() {
        return authenticator;
    }

    /**
     * @return the assistant
     */
    public PlayerUpdateTask getUpdater() {
        return updating;
    }

    /**
     * @return the playerHeadIcon
     */
    public int getPlayerHeadIcon() {
        return playerHeadIcon;
    }

    /**
     * @return the equipment
     */
    public Equipment getEquipment() {
        return equipment;
    }

    /**
     * Sets the players current walk to task
     *
     * @param walkToAction
     * @return
     */
    public Player walkToAction(Task walkToAction) {
        this.walkToAction = walkToAction;
        return this;
    }

    /**
     * Returns the inventory instance
     *
     * @return
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Returns the bank container
     *
     * @return
     */
    public Banking getBanking() {
        return banking;
    }

    /**
     * Gets the walk to action task.
     *
     * @return the walk to action task
     */
    public Task walkToAction() {
        return walkToAction;
    }

    /**
     * Returns the NPC updating instance
     *
     * @return
     */
    public NPCUpdateTask getNpcUpdating() {
        return npcUpdating;
    }

    /**
     * Returns the skills instance
     *
     * @return
     */
    public SkillHandler getSkills() {
        return skills;
    }

    /**
     * @return the logoutRequired
     */
    public boolean isLogoutRequired() {
        return logoutRequired;
    }

    public PlayerSettings getSettings() {
        return settings;
    }

    /**
     * @param logoutRequired the logoutRequired to set
     */
    public void setLogoutRequired(boolean logoutRequired) {
        this.logoutRequired = logoutRequired;
    }

    public int[] getBonuses() {
        return bonuses;
    }

    public void resetBonuses() {
        bonuses = new int[12];
    }

    public void setBonuses(int id, int bonus) {
        this.bonuses[id] = bonus;
    }

    private void setDefaultAppearance() {
        /**
         * Gender
         */
        getAuthentication().appearanceIndex[0] = 0;

        /**
         * Clothing
         */
        getAuthentication().appearanceIndex[1] = 0;
        getAuthentication().appearanceIndex[2] = 18;
        getAuthentication().appearanceIndex[3] = 26;
        getAuthentication().appearanceIndex[4] = 35;
        getAuthentication().appearanceIndex[5] = 36;
        getAuthentication().appearanceIndex[6] = 42;
        getAuthentication().appearanceIndex[7] = 10;
        /**
         * Colors
         */
        getAuthentication().appearanceIndex[8] = 7;
        getAuthentication().appearanceIndex[9] = 8;
        getAuthentication().appearanceIndex[10] = 9;
        getAuthentication().appearanceIndex[11] = 5;
        getAuthentication().appearanceIndex[12] = 0;

    }

    /**
     * @return the skillHandler
     */
    public SkillHandler getSkillHandler() {
        return skillHandler;
    }

    /**
     * @return the advocate
     */
    public PlayerAdvocate getAdvocate() {
        return advocate;
    }

    public Dialogue getDialogue() {
        return dialogue;
    }

    @Override
    public void hit(Hit attack) {
        if (getStatus() != WelfareStatus.DEAD) {
            int damage = attack.getDamage();
            if ((getSkills().getPlayerLevel()[3] - damage) <= 0) {
                damage = getSkills().getPlayerLevel()[3];
            }
            getSkills().getPlayerLevel()[3] -= damage;
            getSkills().refreshSkill(3);
            if (attack.getHitmask() == 1) {
                getUpdateFlags().setDamage(damage);
                getUpdateFlags().setHitType(attack.getHitType());
            } else if (attack.getHitmask() == 2) {
                getUpdateFlags().setDamage2(damage);
                getUpdateFlags().setHitType2(attack.getHitType());
            }

            if (getSkills().getPlayerLevel()[3] <= 0) {
                Server.getService().schedule(new PlayerDeathService(this));
            }
        }

    }

    public PrayerHandler getPrayerHandler() {
        return prayerHandler;
    }

    private boolean[] activePrayer = new boolean[18];

    public boolean isActivePrayer(Prayer prayer) {
        int index = prayer.getPrayerIndex(prayer);
        return activePrayer[index];
    }

    public Player setActivePrayer(Prayer prayer, boolean active) {
        int index = prayer.getPrayerIndex(prayer);
        this.activePrayer[index] = active;
        return this;
    }

    public int getPrayerIcon() {
        return prayerIcon;
    }

    public Player setPrayerIcon(int icon) {
        this.prayerIcon = icon;
        return this;
    }

    public double getPrayerDrainRate() {
        return prayerDrainRate;
    }

    public Player addPrayerDrainRate(double rate) {
        this.prayerDrainRate += rate;
        return this;
    }

    public double getPrayerPoint() {
        return prayerPoint;
    }

    public void setPrayerPoint(double prayerPoint) {
        this.prayerPoint = prayerPoint;
    }

    /**
     * Returns the players current spell id
     */
    public int getSpellId() {
        return spellId;
    }

    /**
     * Sets the players current spell id
     */
    public void setSpellId(int id) {
        this.spellId = id;
    }

    /**
     * Checks if there is a cached update block for this cycle.
     *
     * @return <code>true</code> if so, <code>false</code> if not.
     */
    public boolean hasCachedUpdateBlock() {
        return cachedUpdateBlock != null;
    }

    /**
     * Sets the cached update block for this cycle.
     *
     * @param cachedUpdateBlock The cached update block.
     */
    public void setCachedUpdateBlock(PacketBuilder cachedUpdateBlock) {
        this.cachedUpdateBlock = cachedUpdateBlock;
    }

    /**
     * Gets the cached update block.
     *
     * @return The cached update block.
     */
    public PacketBuilder getCachedUpdateBlock() {
        return cachedUpdateBlock;
    }

    /**
     * Resets the cached update block.
     */
    public void resetCachedUpdateBlock() {
        cachedUpdateBlock = null;
    }

    /**
     * returns the players special amount
     *
     * @return
     */
    public int getSpecialAmount() {
        return specialAmount;
    }

    /**
     * Sets the players special amount
     *
     * @param amount The amount of special left over
     */
    public void setSpecialAmount(int amount) {
        this.specialAmount = amount;
    }

    public int getSpecialBarId() {
        return specialBarId;
    }

    public void setSpecialBarId(int id) {
        this.specialBarId = id;
    }

    public boolean isBuryingBones() {
        return buryingBones;
    }

    public void setBuryingBones(boolean burying) {
        this.buryingBones = burying;
    }

    /**
     * @return the genuineDisconnection
     */
    public boolean isGenuineDisconnection() {
        return genuineDisconnection;
    }

    /**
     * @param genuineDisconnection the genuineDisconnection to set
     */
    public void setGenuineDisconnection(boolean genuineDisconnection) {
        this.genuineDisconnection = genuineDisconnection;
    }

    /**
     * @return the disconnectionHandled
     */
    public boolean isDisconnectionHandled() {
        return disconnectionHandled;
    }

    /**
     * @param disconnectionHandled the disconnectionHandled to set
     */
    public void setDisconnectionHandled(boolean disconnectionHandled) {
        this.disconnectionHandled = disconnectionHandled;
    }


    public void queueMessage(Packet packet) {
       // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public Stream getOutStream() {
        return outStream;
    }

    public void setCachedRegion(Region region) {
        this.cachedRegionNew = region;
    }

    public void setAppearanceUpdateRequired(boolean b) {
        this.appearanceUpdateRequired = b;
    }

    public Session getSession() {
        return session;
    }

}
