package riskyspace.logic;

import java.util.Map;
import java.util.Set;

import riskyspace.GameManager;
import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.World;
import riskyspace.services.Event;
import riskyspace.services.EventBus;

public class FleetMove {
	
	private static boolean moving = false;
	private static Object lock = new Object();
	private static Thread mover = null;
	private static final int STEP_TIME = 100;
	
	public static void interrupt() {
		synchronized (lock) {
			moving = false;
		}
	}
	
	public static int stepTime() {
		return STEP_TIME;
	}
	
	public static synchronized void move(final World world, final Map<Fleet, Path> fleetPaths, final Player player) {
		moving = true;
		final Set<Fleet> fleets = fleetPaths.keySet();
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				while(!checkIfDone(fleetPaths, player, world)) {
					/*
					 * Publish event to graphics to make ships move
					 */
					Event evt = new Event(Event.EventTag.UPDATE_SPRITEDATA, null);
					EventBus.SERVER.publish(evt);
					
					/*
					 * Wait STEP_TIME until moving the objects
					 */
					try {
						Thread.sleep(STEP_TIME);
					} catch (InterruptedException e) {}
					
					if (!moving) {
						//Abort actual move if interrupted
						break;
					}
					
					/*
					 * Synchronize to make interrupt calls wait until move is done
					 */
					synchronized(lock) {
						for (Fleet fleet : fleets) {
							if (fleet.getOwner().equals(player) && fleetPaths.get(fleet).getLength() > 0 &&
									!world.getTerritory(fleetPaths.get(fleet).getCurrentPos()).hasConflict() && fleet.useEnergy()) {
								/*
								 * Move if the fleet is allowed to
								 */
								if (world.getTerritory(fleetPaths.get(fleet).getCurrentPos()).getFleets().contains(fleet)) {
									world.getTerritory(fleetPaths.get(fleet).getCurrentPos()).removeFleet(fleet);
									world.getTerritory(fleetPaths.get(fleet).step()).addFleet(fleet);
								}
							}
						}
					}
				}
				moving = false;
				GameManager.INSTANCE.handleEvent(new Event(Event.EventTag.MOVES_COMPLETE, null), null);
			}
		};
		mover = new Thread(runner);
		mover.start();
	}
	
	/**
	 * Returns if there are currently fleets being moved.
	 */
	public static boolean isMoving() {
		return moving;
	}

	private static boolean checkIfDone(Map<Fleet, Path> fleetPaths, Player player, World world) {
		Set<Fleet> fleets = fleetPaths.keySet();
		boolean done = true;
		for (Fleet fleet : fleets) {
			if (fleet.getOwner().equals(player)) {
				if (fleet.hasEnergy() && fleetPaths.get(fleet).getLength() > 0 && !world.getTerritory(fleetPaths.get(fleet).getCurrentPos()).hasConflict()) {
					done = false;
				}
			}
		}
		return done;
	}
}
