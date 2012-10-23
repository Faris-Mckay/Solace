package org.solace.game.entity.mobile.npc;

/**
 * 
 * @author Faris
 */
public class NPCInfo {

	private final String name;
	private final int level;
	private final int hits;
	private final boolean aggressive;
	private final boolean retreats;
	private final boolean poisonous;

	public NPCInfo(String name, int level, int hits, boolean aggressive,
			boolean retreats, boolean poisonous) {
		this.name = name;
		this.level = level;
		this.hits = hits;
		this.aggressive = aggressive;
		this.retreats = retreats;
		this.poisonous = poisonous;
	}

	public String getName() {
		return name;
	}

	public int getLevel() {
		return level;
	}

	public int getHits() {
		return hits;
	}

	public boolean isAggressive() {
		return aggressive;
	}

	public boolean isRetreats() {
		return retreats;
	}

	public boolean isPoisonous() {
		return poisonous;
	}

}
