package org.solace.game.content.skills.mining;

import org.solace.Server;
import org.solace.event.Event;
import org.solace.game.content.skills.Skill;
import org.solace.game.content.skills.SkillHandler;
import org.solace.game.content.skills.mining.OreDefinitions.OreDefinitionLoader;
import org.solace.game.content.skills.mining.PickaxeDefinitions.PickaxeDefinitionLoader;
import org.solace.game.entity.Animation;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.entity.object.GameObject;
import org.solace.game.entity.object.ObjectManager;
import org.solace.game.item.Item;
import org.solace.game.item.container.impl.Equipment;
import org.solace.game.map.Location;
import org.solace.util.ProtocolUtils;

/**
 * Handles all of the mining actions
 * 
 * @author Arithium
 *
 */
public class Mining extends Skill {

	public Mining(Player player) {
		super(player);
	}
	
	/**
	 * Starts the mining actions for the player
	 * 
	 * @param player
	 *            The player mining
	 * @param location
	 *            The location of the ore
	 * @param objectId
	 *            The id of the ore
	 */
	public static void handleMining(Player player, Location location, int objectId) {
		/*
		 * First we construct our ore definitions based on the object 
		 */
		OreDefinitions oreDef = OreDefinitionLoader.getOreId(objectId);
		
		/*
		 * Next we construct our pickaxe definitions based on our weapon
		 */
		PickaxeDefinitions def = PickaxeDefinitionLoader.getPickaxe(player.getEquipment().getItemBySlot(Equipment.WEAPON_SLOT));
		
		/*
		 * Now we create an instance for the inventory
		 */
		PickaxeDefinitions inventory = null;

		/*
		 * We loop the pickaxe array for the ids to search for
		 */
		for (int pick : PICKAXES) {
			/*
			 * Search if the player has a pickaxe
			 */
			if (player.getInventory().contains(pick)) {
				/*
				 * If so set the inventory definitions
				 */
				inventory = PickaxeDefinitionLoader.getPickaxe(pick);
			}
		}
		
		/*
		 * First we check if the player can actually mine the ore
		 */
		if (!handleOreRequirements(player, oreDef))
			return;
		
		/*
		 * Then we check if the player has a pickaxe or is able to use it
		 */
		if (!handlePickaxeRequirements(player, def, inventory))
			return;
		
		/*
		 * Heres the tricky part, we had to determine which pickaxe you were
		 * going to use, in this case we check the equipment first, if its null
		 * we check the inventory Hopefully by this point we won't make it this
		 * far for both to be null
		 */
		PickaxeDefinitions toUse = def != null ? def : inventory;
		
		/*
		 * Submits an event to start the mining
		 */
		handleMiningActions(player, location, oreDef, toUse, objectId);
	}
	
	/**
	 * Checks to make sure you can mine the ore to begin with
	 * 
	 * @param player
	 *            The player mining
	 * @param oreDef
	 *            The definitions of the ore your mining
	 * @return
	 */
	private static boolean handleOreRequirements(Player player, OreDefinitions oreDef) {
		/*
		 * First we check if the ore is actually null
		 */
		if (oreDef == null) {
			return false;
		}
		
		/*
		 * Next we check if we have the required level to mine the ore
		 */
		if (player.getSkills().getPlayerLevel()[SkillHandler.MINING] < oreDef.getLevelRequired()) {
			player.getPacketDispatcher().sendMessage("You need a mining level of at least " + oreDef.getLevelRequired() + " to mine " + oreDef.getOreName() + ".");
			return false;
		}
		
		/*
		 * Now we check if the players inventory is full
		 */
		if (player.getInventory().isFull()) {
			player.getPacketDispatcher().sendMessage("Your do not have enough space in your inventory.");
			return false;
		}
		return true;
	}
	
	/**
	 * Checks if the player has a pickaxe and is able ot use it
	 * 
	 * @param player
	 *            The player mining
	 * @param def
	 *            The definitions of the pickaxe in your equipment
	 * @param inventory
	 *            The definitions of the pickaxe in your inventory
	 * @return
	 */
	private static boolean handlePickaxeRequirements(Player player, PickaxeDefinitions def, PickaxeDefinitions inventory) {
		
		/*
		 * We check if either of the definitions are null, if they are we don't have a pickaxe
		 */
		if (def == null && inventory == null) {
			player.getPacketDispatcher().sendMessage("You do not have a pickaxe and the required level to mine here.");
			return false;
		}
		/*
		 * This determines the level required based on checking the players equipment or the inventory
		 */
		int levelRequired = (def != null ? def.getLevelRequired() : inventory != null ?  inventory.getLevelRequired() : 0);
		
		/*
		 * This checks that we can actually use the pickaxe we designated
		 */
		if (player.getSkills().getPlayerLevel()[Skill.MINING] < levelRequired) {
			player.getPacketDispatcher().sendMessage("You need a mining level of at least " + levelRequired + " to use this pickaxe.");
			return false;
		}
		return true;
	}
	
	/**
	 * Performs the animation of the pickaxe your using
	 * 
	 * @param player
	 *            The player mining
	 * @param pick
	 *            The pickaxe being used
	 */
	private static void handlePickaxeUsage(Player player, PickaxeDefinitions pick) {
		/*
		 * This checks if the pickaxe is null before performing the emote
		 */
		if (pick != null) {
			/*
			 * If its not we perform the animation of the pickaxe
			 */
			player.setAnimation(Animation.create(pick.getMiningAnimation()));
		}
	}
	
	/**
	 * Handles the actual mining event
	 * 
	 * @param player
	 *            The player mining
	 * @param location
	 *            The location of the ore
	 * @param ore
	 *            The ore itself
	 * @param pickaxe
	 *            The pickaxe being used
	 * @param objectId
	 *            The id of the ore
	 */
	private static void handleMiningActions(final Player player, final Location location, final OreDefinitions ore, final PickaxeDefinitions pickaxe, final int objectId) {
		/*
		 * We start by sending the animation
		 */
		handlePickaxeUsage(player, pickaxe);
		
		/*
		 * Now we sent a message saying we started mining
		 */
		player.getPacketDispatcher().sendMessage("You swing your pick at the rock.");
		/*
		 * This is the event created to cycle the mining actions until the requirements are met.
		 */
		Server.getService().schedule(new Event(1) {
			/*
			 * This is the tickable for the animation
			 */
			int tickable = 0;
			int miningCheck = 0;
			@Override
			public void execute() {
				if (miningCheck > 3) {
					miningCheck++;
				} else {
					if (ProtocolUtils.random(player.getSkills().getPlayerLevel()[Skill.MINING]) > (Math.random() * ore.getLevelRequired() + ProtocolUtils.random(20))) {
						handleObjectReplacement(player, location, ore, objectId);
						this.stop();
					}
					miningCheck = 0;
				}
				/*
				 * Performs the animation cycle
				 */
				if (tickable < 4) {
					tickable++;
				} else {
					handlePickaxeUsage(player, pickaxe);
					tickable = 0;
				}
				if (player.walkToAction() != null) {
					player.setAnimation(Animation.create(65535));
					this.stop();
				}
				/*
				 * Random value checking on whether we successfully mine the ore
				 */
				if (!eventCheck(player, ore, pickaxe)) {
					player.setAnimation(Animation.create(65535));
					this.stop();
					return;
				}
			}
		});
	}
	
	private static boolean eventCheck(Player player, OreDefinitions ore, PickaxeDefinitions pick) {
		if (!player.getInventory().contains(pick.getItemId()) && player.getEquipment().getItemBySlot(3) != pick.getItemId()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Creates a new object and handles player details when it comes to mining
	 * 
	 * @param player
	 *            The player mining
	 * @param location
	 *            the location of the ore
	 * @param ore
	 *            The ore the player is mining
	 * @param objectId
	 *            The id of the ore the player is mining
	 */
	private static void handleObjectReplacement(Player player, Location location, OreDefinitions ore, int objectId) {
		ObjectManager.registerObject(new GameObject(location, ore.getReplacementId(), objectId, ore.getRespawnTimer()));
		player.getSkills().addSkillExp(SkillHandler.MINING, ore.getExperience());
		player.setAnimation(Animation.create(65535));
		player.getPacketDispatcher().sendMessage("You managed to mine some " + ore.getOreName() + " ore.");
		player.getInventory().add(new Item(ore.getItemId(), 1));
	}
	
	/**
	 * Defines all of the pickaxe's available, necessary because its for the inventory checking
	 * We loop from highest to lower to get the right pick
	 */
	private static final int[] PICKAXES = { 1275, 1271, 1273, 1269, 1267, 1265 };

}
