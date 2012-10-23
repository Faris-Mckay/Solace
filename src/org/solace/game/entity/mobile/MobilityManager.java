package org.solace.game.entity.mobile;

import java.util.Deque;
import java.util.LinkedList;
import org.solace.util.ProtocolUtils;
import org.solace.game.entity.mobile.Mobile.WelfareStatus;
import org.solace.game.entity.mobile.npc.NPC;
import org.solace.game.entity.mobile.player.Player;
import org.solace.game.map.Location;

/**
 * Entity movement manager.
 * 
 * @author Faris
 */
public class MobilityManager {

	private Mobile mobile;
	private Deque<Location> movementSteps;

	private int walkingDirection = -1, runningDirection = -1;
	private boolean running = true;

	/**
	 * Creates a new movement manager for entity.
	 * 
	 * @param entity
	 *            the entity instance
	 * @return a new movement manager
	 */
	public MobilityManager(Mobile mobile) {
		movementSteps = new LinkedList<Location>();
		mobile(mobile);
	}

	/**
	 * Adds movement destination to the queue.
	 * 
	 * @param destination
	 *            the movement destination
	 */
	public MobilityManager queueDestination(Location destination) {
		Location lastStep = movementSteps.peekLast();
		int diffX = destination.getX() - lastStep.getX();
		int diffY = destination.getY() - lastStep.getY();
		int stepsAmount = Math.max(Math.abs(diffX), Math.abs(diffY));
		for (int i = 0; i < stepsAmount; i++) {
			if (diffX < 0) {
				diffX++;
			} else if (diffX > 0) {
				diffX--;
			}
			if (diffY < 0) {
				diffY++;
			} else if (diffY > 0) {
				diffY--;
			}
			queueStep(destination.getX() - diffX, destination.getY() - diffY);
		}
		return this;
	}

	/**
	 * Adds movement point to the queue.
	 * 
	 * @param x
	 *            the x coordinate
	 * 
	 * @param y
	 *            the y coordinate
	 */
	public MobilityManager queueStep(int x, int y) {
		Location currentStep = movementSteps.peekLast();
		int diffX = x - currentStep.getX();
		int diffY = y - currentStep.getY();
		if (ProtocolUtils.getDirection(diffX, diffY) > -1) {
			movementSteps.add(new Location(x, y));
		}
		return this;
	}

	/**
	 * Checks if the direction represented by the two delta values can connect
	 * two points together in a single direction.
	 * 
	 * @param deltaX
	 *            The difference in X coordinates.
	 * @param deltaY
	 *            The difference in X coordinates.
	 * @return {@code true} if so, {@code false} if not.
	 */
	public static boolean isConnectable(int deltaX, int deltaY) {
		return Math.abs(deltaX) == Math.abs(deltaY) || deltaX == 0
				|| deltaY == 0;
	}

	/**
	 * Processes entity movement.
	 */
	public void processMovement() {
		if (mobile.getStatus() == WelfareStatus.DEAD) {
			return;
		}
		if (mobile instanceof Player) {
			if (movementSteps.isEmpty()) {
				return;
			}
			walkingDirection(generateDirection());
			if (running() && !movementSteps.isEmpty()) {
				runningDirection(generateDirection());
			}
			int diffX = mobile.getLocation().getX()
					- mobile.getCachedRegion().regionX() * 8;
			int diffY = mobile.getLocation().getY()
					- mobile.getCachedRegion().regionY() * 8;
			boolean changed = diffX < 16 || diffX >= 88 || diffY < 16 || diffY >= 88;
			((Player) mobile).getUpdater().setMapRegionChanging(changed);
		}
		if (mobile instanceof NPC) {
			final int random = (int) (Math.floor(Math.random() * 7));
			switch (mobile.getMoveStatus()) {
			case STATIONARY:
				walkingDirection(-1);
				break;
			case MOBILE:
				mobile.getLocation().lastX = mobile.getLocation().getX();
				mobile.getLocation().lastY = mobile.getLocation().getY();
				int distanceX = Math.abs(mobile.getLocation().getX()
						- mobile.getTargettedLocation().getX());
				int distanceY = Math.abs(mobile.getLocation().getY()
						- mobile.getTargettedLocation().getY());
				int offsetX = ProtocolUtils.DIRECTION_DELTA_X[random];
				int offsetY = ProtocolUtils.DIRECTION_DELTA_Y[random];
				Location newLocation = new Location((mobile.getLocation()
						.getX() + offsetX),
						(mobile.getLocation().getY() + offsetY));
				if (mobile.getInteractingEntity() != null) {
					if (!movementSteps.isEmpty()) {
						walkingDirection(generateDirection());
					}
				} else {
					int randomValue = ProtocolUtils.random(10);
					if (randomValue > 1) {
						walkingDirection(-1);
					} else {
						if (distanceX > mobile.getMaximumWalkingDistance()
								|| distanceY > mobile
										.getMaximumWalkingDistance()) {
							walkingDirection(-1);
						} else {
							mobile.setLocation(newLocation);
							walkingDirection(random);
						}
					}
				}
				break;
			}
		}
	}

	/**
	 * Generates next walking direction.
	 * 
	 * @return next walking direction for player updating
	 */
	public int generateDirection() {
		Location nextStep = movementSteps.poll();
		Location currentStep = mobile.getLocation();
		int diffX = nextStep.getX() - currentStep.getX();
		int diffY = nextStep.getY() - currentStep.getY();
		int direction = ProtocolUtils.getDirection(diffX, diffY);
		if (direction > -1) {
			mobile.getLocation().transform(
					ProtocolUtils.DIRECTION_DELTA_X[direction],
					ProtocolUtils.DIRECTION_DELTA_Y[direction]);
		}
		return direction;
	}

	/**
	 * Prepares the movement queue for new steps.
	 */
	public MobilityManager prepare() {
		walkingDirection(-1).runningDirection(-1);
		movementSteps.clear();
		movementSteps.add(mobile.getLocation());
		return this;
	}

	/**
	 * Gives the player a position to travel to
	 * 
	 * @param directionX
	 * @param directionY
	 */
	public void walkTo(final int directionX, final int directionY) {
		Location entityLocation = mobile().getLocation();
		int newX = (entityLocation.getX() + directionX);
		int newY = (entityLocation.getY() + directionY);
		prepare();
		queueDestination(new Location(newX, newY));
		finish();
	}

	/**
	 * Finishes queue preparation.
	 */
	public MobilityManager finish() {
		movementSteps.removeFirst();
		return this;
	}

	/**
	 * Sets the associated entity with this movement manager.
	 * 
	 * @param entity
	 *            the associated entity
	 */
	public MobilityManager mobile(Mobile mobile) {
		this.mobile = mobile;
		return this;
	}

	/**
	 * Gets the associated character
	 * 
	 * @return the associated character
	 */
	public Mobile mobile() {
		return mobile;
	}

	/**
	 * Sets the walking direction.
	 * 
	 * @param direction
	 *            the direction
	 */
	public MobilityManager walkingDirection(int direction) {
		this.walkingDirection = direction;
		return this;
	}

	/**
	 * Gets the walking direction.
	 * 
	 * @return the walking direction
	 */
	public int walkingDirection() {
		return walkingDirection;
	}

	/**
	 * Sets the running direction.
	 * 
	 * @param direction
	 *            the direction
	 */
	public MobilityManager runningDirection(int direction) {
		this.runningDirection = direction;
		return this;
	}

	/**
	 * Gets the running direction.
	 * 
	 * @return the running direction
	 */
	public int runningDirection() {
		return runningDirection;
	}

	/**
	 * Sets the running flag.
	 * 
	 * @param running
	 *            the running flag
	 */
	public MobilityManager running(boolean running) {
		this.running = running;
		return this;
	}

	/**
	 * Gets the running flag.
	 * 
	 * @return the running flag
	 */
	public boolean running() {
		return running;
	}

}