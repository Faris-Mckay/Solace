package org.solace.game.content.combat.magic;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Map;

import org.solace.util.XStreamUtil;

/**
 * 
 * @author Anthony (Ultimate)
 * 
 */
public class MagicDefinitions {

	public SpellType type;

	public static final SpellType[] MODERN_SPELLS = new SpellType[63];
	public static final SpellType[] ANCIENT_SPELLS = new SpellType[16];

	public enum SpellType {
		MODERN, ANCIENTS, TELEPORT, MISC
	}

	private int level;

	private int animation;
	private int projectile;
	private int startProjectile;
	private int endProjectile;

	private int[] runes = new int[4];
	private int[] runesAmount = new int[4];
	private int requiredItem = -1;

	private int maximumHit;
	private double xp;

	public MagicDefinitions(SpellType type, int level, int animation, int projectile,
			int startProjectile, int endProjectile, int[] runes,
			int[] runesAmount, int maximumHit, double xp) {
		this.type = type;
		this.level = level;
		this.animation = animation;
		this.projectile = projectile;
		this.startProjectile = startProjectile;
		this.endProjectile = endProjectile;
		this.runes = runes;
		this.runesAmount = runesAmount;
		this.maximumHit = maximumHit;
		this.xp = xp;
	}

	public SpellType getSpellType() {
		return type;
	}

	public int getLevel() {
		return level;
	}

	public int getAnimation() {
		return animation;
	}

	public int getProjectile() {
		return projectile;
	}

	public int getStartProjectile() {
		return startProjectile;
	}

	public int getEndProjectile() {
		return endProjectile;
	}

	public int[] getRunes() {
		return runes;
	}

	public int[] getRunesAmount() {
		return runesAmount;
	}

	public int getRequiredItem() {
		return requiredItem;
	}

	public int getMaximumHit() {
		return maximumHit;
	}

	public double getXP() {
		return xp;
	}

	public static class MagicSpellLoader {

		private static Map<Integer, MagicDefinitions> spells;

		public static MagicDefinitions getSpell(int id) {
			return spells.get(id);
		}

		@SuppressWarnings("unchecked")
		public static void loadMagicSpells() throws FileNotFoundException {
			XStreamUtil.getInstance();
			spells = (Map<Integer, MagicDefinitions>) XStreamUtil.getXStream().fromXML(new FileInputStream("./data/xml/items/magic_spells.xml"));
		}

	}

}