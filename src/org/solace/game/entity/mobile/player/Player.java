package org.solace.game.entity.mobile.player;

import org.solace.event.CycleEventExecutor;
import org.solace.event.impl.PlayerLoginEvent;
import org.solace.event.impl.PlayerLogoutEvent;
import org.solace.game.content.dialogue.Dialogue;
import org.solace.game.content.PrivateMessaging;
import org.solace.game.content.combat.Combat;
import org.solace.game.content.combat.DelayedAttack;
import org.solace.game.content.combat.PrayerHandler;
import org.solace.game.content.combat.PrayerHandler.Prayer;
import org.solace.game.content.music.MusicHandler;
import org.solace.game.content.skills.SkillHandler;
import org.solace.game.entity.UpdateFlags.UpdateFlag;
import org.solace.game.entity.mobile.Mobile;
import org.solace.game.entity.mobile.npc.NPCUpdating;
import org.solace.game.item.container.Equipment;
import org.solace.game.item.container.Inventory;
import org.solace.game.map.Location;
import org.solace.network.RSChannelContext;
import org.solace.network.packet.PacketDispatcher;
import org.solace.task.Task;
import org.solace.task.TaskExecuter;
import org.solace.event.impl.PlayerDeathEvent;

/**
 * 
 * @author Faris
 */
public class Player extends Mobile {

	/**
	 * Player stored objects
	 */
	private Task walkToAction;
	private RSChannelContext channelContext;
	private PlayerAuthentication authenticator;
	private PacketDispatcher packetDispatcher = new PacketDispatcher(this);
	private PrivateMessaging playerMessaging = new PrivateMessaging(this);
	private MusicHandler musichandler = new MusicHandler(this);
	private PlayerUpdating updating = new PlayerUpdating(this);
	private Equipment equipment = new Equipment(this);
	private Inventory inventory = new Inventory(this);
	private SkillHandler skills = new SkillHandler(this);
	private NPCUpdating npcUpdating = new NPCUpdating(this);
	private SkillHandler skillHandler = new SkillHandler(this);
	private PlayerAdvocate advocate = new PlayerAdvocate(this);
	private PrayerHandler prayerHandler = new PrayerHandler();
	private Dialogue dialogue = new Dialogue(this);
	private PlayerSettings settings = new PlayerSettings();
        private CycleEventExecutor playerEvents = new CycleEventExecutor(this);

	/**
	 * Player stored primitives
	 */
	private int playerHeadIcon = -1;
	private boolean logoutRequired = false;
	private double prayerPoint = 1.0;
	private boolean autoRetaliating;
	private int[] bonuses = new int[12];

	public Player(String username, String password, RSChannelContext channelContext) {
		super(new Location(3222, 3222));
		this.authenticator = new PlayerAuthentication(username, password);
		this.channelContext = channelContext;
		setDefaultSkills();
		setDefaultAppearance();
		getUpdateFlags().flag(UpdateFlag.APPEARANCE);
                this.getAuthentication().setPlayerRights(PlayerAuthentication.PrivilegeRank.ADMINISTRATOR);
	}

	/**
	 * Can be used for content implementations but only when absolutely
	 * necessary, this is NOT to be used as the process in PI
	 */
	@Override
	public void update() {
            getPlayerEvents().execute();
            getMobilityManager().processMovement();
            if (getStatus() != WelfareStatus.DEAD) {
                    /*
                     * Combat tick
                     */
                    Combat.handleCombatTick(this);

                    /*
                     * Prayer draining 
                     */
                    PrayerHandler.handlePrayerDraining(this);
            }
	}

	/**
	 * Sets channel context as parsed context
	 * 
	 * @param channelContext
	 * @return updated player
	 */
	public Player channelContext(RSChannelContext channelContext) {
		this.channelContext = channelContext;
		return this;
	}

	/**
	 * Returns the channelContext for the player
	 * 
	 * @return
	 */
	public RSChannelContext channelContext() {
		return channelContext;
	}

	/**
	 * Gets the associated music handler.
	 * 
	 * @return
	 */
	public MusicHandler getMusicHandler() {
		return musichandler;
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
		new PlayerLoginEvent(this).execute();
	}

	/**
	 * Schedules a new logout event event for this player
	 */
	public void handleLogoutData() {
		new PlayerLogoutEvent(this).execute();
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
	public PlayerUpdating getUpdater() {
		return updating;
	}

	public void setFaceEntity(Mobile entity) {
		super.setInteractingEntityIndex(entity.getIndex());
		updating.getMaster().getUpdateFlags().faceEntity();
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
	public NPCUpdating getNpcUpdating() {
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
	 * @param logoutRequired
	 *            the logoutRequired to set
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

	/**
	 * Sets the defaulted skill values and experience levels
	 */
	private void setDefaultSkills() {
		for (int i = 0; i < getSkills().getPlayerLevel().length; i++) {
			getSkills().getPlayerLevel()[i] = 1;
			getSkills().getPlayerExp()[i] = 0;
		}
		getSkills().getPlayerLevel()[3] = 10;
		getSkills().getPlayerExp()[3] = 1154;
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
	public void hit(DelayedAttack attack) {
		int damage = attack.getDamage();
		if ((getSkills().getPlayerLevel()[3] - damage) <= 0) {
			damage = getSkills().getPlayerLevel()[3];
		}
		getSkills().getPlayerLevel()[3] -= damage;
		getUpdateFlags().setDamage(damage);
		getUpdateFlags().setHitMask(attack.getHitmask());
		getSkills().refreshSkill(3);

		if (getSkills().getPlayerLevel()[3] <= 0) {
			TaskExecuter.get().schedule(new PlayerDeathEvent(this));
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

	private int prayerIcon = -1;

	public int getPrayerIcon() {
		return prayerIcon;
	}

	public Player setPrayerIcon(int icon) {
		this.prayerIcon = icon;
		return this;
	}

	private double prayerDrainRate;
	public long foodDelay;

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
	
	public boolean isAutoRetaliating() {
		return autoRetaliating;
	}
	
	public void setAutoRetaliating(boolean auto) {
		this.autoRetaliating = auto;
	}

    /**
     * @return the playerEvents
     */
    public CycleEventExecutor getPlayerEvents() {
        return playerEvents;
    }

}
