package riskyspace.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventText;

import static riskyspace.model.Resource.METAL;
import static riskyspace.model.Resource.GAS;
import static riskyspace.model.ShipType.*;

public class PlayerStats {
	private Map<Resource, Integer> resources = null;
	private Map<Resource, Integer> income = null;
	private Map<Object, Integer> buildTime = null;
	private Map<Object, Integer> metalCost = null;
	private Map<Object, Integer> gasCost = null;
	private Map<Object, Integer> supplyCost = null;
	
	private Map<Position,LinkedList<QueueItem>> buildQueue = new HashMap<Position,LinkedList<QueueItem>>();
	
	private Supply supply = null;
	
	public PlayerStats() {
		supply = new Supply();
		initResources();
		initIncome();
		initBuildTimes();
		initMetalCost();
		initGasCost();
		initSupplyCost();
	}
	
	private void initResources() {
		resources = new HashMap<Resource, Integer>();
		resources.put(METAL, 150);
		resources.put(GAS, 0);
	}
	
	private void initIncome() {
		income = new HashMap<Resource, Integer>();
		income.put(METAL, 50);
		income.put(GAS, 0);
	}
	
	private void initMetalCost() {
		metalCost = new HashMap<Object, Integer>();
		metalCost.put(SCOUT, 50);
		metalCost.put(HUNTER, 120);
		metalCost.put(COLONIZER, 200);
		metalCost.put(DESTROYER, 400);
	}
	
	private void initGasCost() {
		gasCost = new HashMap<Object, Integer>();
		gasCost.put(SCOUT, 0);
		gasCost.put(HUNTER, 20);
		gasCost.put(COLONIZER, 0);
		gasCost.put(DESTROYER, 100);
	}
	
	private void initSupplyCost() {
		supplyCost = new HashMap<Object, Integer>();
		supplyCost.put(SCOUT, 1);
		supplyCost.put(HUNTER, 1);
		supplyCost.put(COLONIZER, 1);
		supplyCost.put(DESTROYER, 2);
	}
	
	private void initBuildTimes() {
		buildTime = new HashMap<Object, Integer>();
		buildTime.put(SCOUT, 1);
		buildTime.put(HUNTER, 1);
		buildTime.put(COLONIZER, 1);
		buildTime.put(DESTROYER, 2);
		// buildTime.put(Turret, 1);     <- future suggestion?
		// buildTime.put(upgrade, 2);
	}
	
	/*
	 * NEW/YOUR TURN LISTENER: Changes income to relevant numbers and
	 * increases resources.
	 */

	/**
	 * If there is enough resources then use them and return true, else return false.
	 * @param resource The kind of resource needed
	 * @param amount How much of the resource needed
	 * @return true if the purchase was possible
	 */
	public boolean purchase(Resource resource, int amount) {
		if (resources.get(resource) >= amount) {
			resources.put(resource, resources.get(resource) - amount);
			Event evt;
			if (resource == METAL) {
				evt = new Event(Event.EventTag.METAL_CHANGED, resources.get(resource));
			} else {
				evt = new Event(Event.EventTag.GAS_CHANGED, resources.get(resource));
			}
			EventBus.INSTANCE.publish(evt);
			return true;
		} else {
			return false;
		}
	}

	public void update(int numberOfColonies, int usedSupply) {
		supply.update(numberOfColonies, usedSupply);
	}
	
	public boolean hasEnoughSupply(int supplyIncrease) {
		return supply.hasEnough(supplyIncrease);
	}

	/**
	 * Set the income of a Resource to a new value.
	 * @param res The Resource type to be changed.
	 * @param newIncome The new income value.
	 */
	public void setIncome(Resource res, int newIncome) {
		income.put(res, newIncome);
	}
	
	/**
	 * Increase the income of a Resource with a set amount.
	 * @param res The Resource type to be changed.
	 * @param additionalIncome The reduction value.
	 */
	public void increaseIncome(Resource res, int additionalIncome) {
		if (additionalIncome < 0) {
			throw new IllegalArgumentException("Use method \"decreaseIncome()\" to decrease income");
		}
		income.put(res, income.get(res) + additionalIncome);
	}
	
	/**
	 * Decrease the income of a Resource with a set amount.
	 * @param res The Resource type to be changed.
	 * @param reducedIncome The reduction value.
	 */
	public void decreaseIncome(Resource res, int reducedIncome) {
		if (reducedIncome < 0) {
			throw new IllegalArgumentException("Positive integer only: reducedIncome");
		}
		income.put(res, income.get(res) - reducedIncome);
	}
	
	/**
	 * Gain additional resources based on Player's income.
	 */
	public void gainNewResources() {
		resources.put(METAL, resources.get(METAL) + income.get(METAL));
		resources.put(GAS, resources.get(GAS) + income.get(GAS));
		Event evt;
		evt = new Event(Event.EventTag.METAL_CHANGED, resources.get(METAL));
		EventBus.INSTANCE.publish(evt);
		evt = new Event(Event.EventTag.GAS_CHANGED, resources.get(GAS));
		EventBus.INSTANCE.publish(evt);
	}
	
	public List<QueueItem> reduceBuildQueue() {
		List<QueueItem> buildShips = new ArrayList<QueueItem>();
		for (Position pos : buildQueue.keySet()) {
			if (!buildQueue.get(pos).isEmpty()) {
				if (buildQueue.get(pos).getFirst().getBuildTime() == 1) {
					buildShips.add(buildQueue.get(pos).getFirst());
					supply.setQueuedSupply(supply.getQueuedSupply() - supplyCost.get(buildQueue.get(pos).getFirst().getItem()));
					buildQueue.get(pos).removeFirst();
				} else {
					buildQueue.get(pos).getFirst().subtractBuildTime();
				}
			}
		}
		if (buildShips.size() >= 1) {
			Event evt = new Event(Event.EventTag.SUPPLY_CHANGED, getSupply());
			EventBus.INSTANCE.publish(evt);
		}
		return buildShips;
	}
	
	public void queueItem(Object object, Position position) {
		if (!hasEnoughSupply(supplyCost.get(object))) {
			EventText et = new EventText("Not enough supply!", position);
			Event event = new Event(Event.EventTag.EVENT_TEXT, et);
			EventBus.INSTANCE.publish(event);
			return;
		}
		if (!hasEnoughResources(metalCost.get(object), gasCost.get(object))) {
			EventText et = new EventText("Not enough resources!", position);
			Event event = new Event(Event.EventTag.EVENT_TEXT, et);
			EventBus.INSTANCE.publish(event);
			return;
		}
		purchase(Resource.METAL, metalCost.get(object));
		purchase(Resource.GAS, gasCost.get(object));
		if (!buildQueue.containsKey(position)) {
			buildQueue.put(position, new LinkedList<QueueItem>());
		}
		buildQueue.get(position).add(new QueueItem(object, position, buildTime.get(object)));
		supply.setQueuedSupply(supply.getQueuedSupply() + supplyCost.get(object));
		Event evt = new Event(Event.EventTag.SUPPLY_CHANGED, getSupply());
		EventBus.INSTANCE.publish(evt);
		EventText et = new EventText(object.toString().toLowerCase() + " added to build queue!", position);
		evt = new Event(Event.EventTag.EVENT_TEXT, et);
		EventBus.INSTANCE.publish(evt);
	}
	
	public void resetQueue(Position pos) {
 		if (!buildQueue.containsKey(pos)) {
 			buildQueue.put(pos, new LinkedList<QueueItem>());
 		}
		if (!buildQueue.get(pos).isEmpty()) {
			if (buildQueue.get(pos).getFirst() instanceof QueueItem) {
				supply.setQueuedSupply(supply.getQueuedSupply() - supplyCost.get(buildQueue.get(pos).getFirst().getItem()));
				purchase(Resource.METAL, -metalCost.get(buildQueue.get(pos).getFirst().getItem()));
				purchase(Resource.GAS, -gasCost.get(buildQueue.get(pos).getFirst().getItem()));
				buildQueue.get(pos).removeFirst();
			}
		}
	}
	
	public void resetAll() {
		for (Position pos : buildQueue.keySet()) {
	 		if (!buildQueue.containsKey(pos)) {
	 			buildQueue.put(pos, new LinkedList<QueueItem>());
	 		}
			while (buildQueue.get(pos).size() > 1) {
				if (buildQueue.get(pos).getLast() instanceof QueueItem) {
					supply.setQueuedSupply(supply.getQueuedSupply() - supplyCost.get(buildQueue.get(pos).getLast().getItem()));
					purchase(Resource.METAL, -metalCost.get(buildQueue.get(pos).getLast().getItem()));
					purchase(Resource.GAS, -gasCost.get(buildQueue.get(pos).getLast().getItem()));
					buildQueue.get(pos).removeLast();
				}
			}
		}
	}
	
	private boolean hasEnoughResources(int metalCost, int gasCost) {
		return metalCost <= resources.get(METAL) && gasCost <= resources.get(GAS);
	}

	public int getResource(Resource resource) {
		return resources.get(resource);
	}

	public int getUsedSupply() {
		return supply.getUsed();
	}
	
	public int getMaxSupply() {
		return supply.getMax();
	}
	
	public boolean isSupplyCapped() {
		return supply.isCapped();
	}
	
	public boolean isSupplyOverCapped() {
		return supply.isOverCapped();
	}
	
	public Supply getSupply() {
		return supply.clone();
	}
	
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else if (other == null || this.getClass() != other.getClass()) {
			return false;
		} else {
			PlayerStats otherPlayerStats = (PlayerStats) other;
			return (resources.get(GAS) == otherPlayerStats.resources.get(GAS) && 
					resources.get(METAL) == otherPlayerStats.resources.get(METAL) &&
					supply.getUsed() == otherPlayerStats.supply.getUsed());
		}
	}
	
	@Override
	public String toString() {
		return "PlayerStats [" + "Metal: " + resources.get(METAL) + "  Income: " + income.get(METAL) + ", " + 
				"Gas: " + resources.get(GAS) + "  Income: " + income.get(GAS) + 
				", " + "Supply: " + supply.getUsed() + "/" +
				getMaxSupply() + " ]";
	}
	
	@Override
	public int hashCode() {
		return resources.get(GAS)*3 + resources.get(METAL)*5 + supply.hashCode()*7;
	}

}
