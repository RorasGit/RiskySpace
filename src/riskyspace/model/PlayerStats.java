package riskyspace.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
	
	private Map<Position,LinkedList<QueueItem>> buildQueue = new HashMap<Position,LinkedList<QueueItem>>();
	
	private Supply supply = null;
	
	public PlayerStats() {
		supply = new Supply();
		initResources();
		initIncome();
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
	public boolean useResource(Resource resource, int amount) {
		if (resources.get(resource) >= amount) {
			resources.put(resource, resources.get(resource) - amount);
			Event evt = new Event(Event.EventTag.RESOURCES_CHANGED, new ImmutablePlayerStats(this));
			EventBus.INSTANCE.publish(evt);
			return true;
		} else {
			return false;
		}
	}
	
	public void increaseResource(Resource res, int amount) {
		resources.put(res, amount + resources.get(res));
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
	
	public int getIncome(Resource res) {
		return income.get(res);
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
		Event evt = new Event(Event.EventTag.RESOURCES_CHANGED, new ImmutablePlayerStats(this));
		EventBus.INSTANCE.publish(evt);
	}
	
	public List<QueueItem> reduceBuildQueue() {
		List<QueueItem> buildShips = new ArrayList<QueueItem>();
		for (Position pos : buildQueue.keySet()) {
			if (!buildQueue.get(pos).isEmpty()) {
				if (buildQueue.get(pos).getFirst().getBuildTime() == 1) {
					buildShips.add(buildQueue.get(pos).getFirst());
					supply.setQueuedSupply(supply.getQueuedSupply() - buildQueue.get(pos).getFirst().getItem().getSupplyCost());
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
	
	public void queueItem(BuildAble buildAble, Position position) {
		if (!hasEnoughSupply(buildAble.getSupplyCost())) {
			EventText et = new EventText("Not enough supply!", position);
			Event event = new Event(Event.EventTag.EVENT_TEXT, et);
			EventBus.INSTANCE.publish(event);
			return;
		}
		if (!hasEnoughResources(buildAble.getMetalCost(), buildAble.getGasCost())) {
			EventText et = new EventText("Not enough resources!", position);
			Event event = new Event(Event.EventTag.EVENT_TEXT, et);
			EventBus.INSTANCE.publish(event);
			return;
		}
		useResource(Resource.METAL, buildAble.getMetalCost());
		useResource(Resource.GAS, buildAble.getGasCost());
		if (!buildQueue.containsKey(position)) {
			buildQueue.put(position, new LinkedList<QueueItem>());
		}
		buildQueue.get(position).add(new QueueItem(buildAble, position, buildAble.getBuildTime()));
		supply.setQueuedSupply(supply.getQueuedSupply() + buildAble.getSupplyCost());
		Event evt = new Event(Event.EventTag.SUPPLY_CHANGED, getSupply());
		EventBus.INSTANCE.publish(evt);
		EventText et = new EventText(buildAble.toString().toLowerCase() + " added to build queue!", position);
		evt = new Event(Event.EventTag.EVENT_TEXT, et);
		EventBus.INSTANCE.publish(evt);
	}
	
	public void resetQueue(Position pos) {
 		if (buildQueue.containsKey(pos)) {
			while (!buildQueue.get(pos).isEmpty()) {
					BuildAble building = buildQueue.get(pos).getFirst().getItem();
					supply.setQueuedSupply(supply.getQueuedSupply() - building.getSupplyCost());
					useResource(Resource.METAL, - building.getMetalCost());
					useResource(Resource.GAS, - building.getGasCost());
					buildQueue.get(pos).removeFirst();
			}
 		}
	}
	
	public void resetAll() {
		Iterator<Position> iter = buildQueue.keySet().iterator();
		while(iter.hasNext()){
			resetQueue(iter.next());
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