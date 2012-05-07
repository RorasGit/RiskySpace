package riskyspace.model;

import static riskyspace.model.Resource.GAS;
import static riskyspace.model.Resource.METAL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.Event.EventTag;

public class PlayerStats {
	
	private Map<Resource, Integer> resources = null;
	private Map<Resource, Integer> income = null;
	
	private Supply supply = null;
	
	public PlayerStats() {
		supply = new Supply();
		initResources();
		initIncome();
	}
	
	/**
	 * Sets starting resources to default value 150.
	 */
	private void initResources() {
		resources = new HashMap<Resource, Integer>();
		resources.put(METAL, 150);
		resources.put(GAS, 0);
	}
	
	/**
	 * Sets the income for home planet to default value 50.
	 */
	private void initIncome() {
		income = new HashMap<Resource, Integer>();
		income.put(METAL, 50);
		income.put(GAS, 0);
	}
	
	/**
	 * Subtracts the cost of the BuildAble from Player's resources.
	 * @param buildAble - the BuildAble object you want to buy.
	 * @return returns true if Player could afford to buy the BuildAble.
	 */
	public boolean purchase(BuildAble buildAble) {
		if (hasEnoughResources(buildAble.getMetalCost(), buildAble.getGasCost())) {
			resources.put(METAL, resources.get(METAL) - buildAble.getMetalCost());
			resources.put(GAS, resources.get(GAS) - buildAble.getGasCost());
			Event evt;
			if (buildAble.getMetalCost() > 0) {
				evt = new Event(Event.EventTag.METAL_CHANGED, resources.get(METAL));
				EventBus.INSTANCE.publish(evt);
			}
			if (buildAble.getGasCost() > 0) {
				evt = new Event(Event.EventTag.GAS_CHANGED, resources.get(GAS));
				EventBus.INSTANCE.publish(evt);
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Subtracts the cost of the BuildAbles in the list from Player's resources.
	 * @param buildAbles - the list of BuildAble objects you want to buy.
	 * @return returns true if Player could afford all the items in the list.
	 */
	public boolean purchase(List<BuildAble> buildAbles) {
		int metalCost = 0;
		int gasCost = 0;
		for (BuildAble ba : buildAbles) {
			metalCost += ba.getMetalCost();
			gasCost += ba.getGasCost();
		}
		if (hasEnoughResources(metalCost, gasCost)) {
			resources.put(METAL, resources.get(METAL) - metalCost);
			resources.put(GAS, resources.get(GAS) - gasCost);
			Event evt;
			if (metalCost > 0) {
				evt = new Event(Event.EventTag.METAL_CHANGED, resources.get(METAL));
				EventBus.INSTANCE.publish(evt);
			}
			if (gasCost > 0) {
				evt = new Event(Event.EventTag.GAS_CHANGED, resources.get(GAS));
				EventBus.INSTANCE.publish(evt);
			}
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Refunds the cost of a BuildAble object.
	 * @param buildAble - the BuildAble whose cost you want refunded.
	 */
	public void refund(BuildAble buildAble) {
		resources.put(METAL, resources.get(METAL) + buildAble.getMetalCost());
		resources.put(GAS, resources.get(GAS) + buildAble.getGasCost());
		Event evt;
		if (buildAble.getMetalCost() > 0) {
			evt = new Event(Event.EventTag.METAL_CHANGED, resources.get(METAL));
			EventBus.INSTANCE.publish(evt);
		}
		if (buildAble.getGasCost() > 0) {
			evt = new Event(Event.EventTag.GAS_CHANGED, resources.get(GAS));
			EventBus.INSTANCE.publish(evt);
		}
	}
	
	/**
	 * Refunds the cost of all the BuildAble items in the list.
	 * Only publishes resource changed event when appropriate.
	 * @param buildAbles - a list containing the BuildAbles whose cost you want refunded.s
	 */
	public void refund(List<BuildAble> buildAbles) {
		int metalCost = 0;
		int gasCost = 0;
		for (BuildAble ba : buildAbles) {
			metalCost += ba.getMetalCost();
			gasCost += ba.getGasCost();
			resources.put(METAL, resources.get(METAL) + ba.getMetalCost());
			resources.put(GAS, resources.get(GAS) + ba.getGasCost());
		}
		Event evt;
		if (metalCost > 0) {
			evt = new Event(Event.EventTag.METAL_CHANGED, resources.get(METAL));
			EventBus.INSTANCE.publish(evt);
		}
		if (gasCost > 0) {
			evt = new Event(EventTag.GAS_CHANGED, resources.get(GAS));
			EventBus.INSTANCE.publish(evt);
		}
	}
	
	/**
	 * Updates the Player's supply based on ships, queued ships and number of colonies.
	 * @param numberOfColonies - amount of colonies owned by the Player
	 * @param usedSupply - the supply currently occupied by ships and queued ships
	 */
	public void update(int numberOfColonies, int usedSupply) {
		supply.update(numberOfColonies, usedSupply);
	}
	
	/**
	 * Checks if the Player has enough free supply.
	 * @param supplyIncrease - the amount of supply you plan to add
	 * @return returns true if enough supply is available
	 */
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
		if (income.get(METAL) > 0) {
			evt = new Event(Event.EventTag.METAL_CHANGED, resources.get(METAL));
			EventBus.INSTANCE.publish(evt);
		}
		if (income.get(GAS) > 0) {
			evt = new Event(Event.EventTag.GAS_CHANGED, resources.get(GAS));
			EventBus.INSTANCE.publish(evt);
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
			return (resources.get(GAS) == otherPlayerStats.getResource(GAS) && 
					resources.get(METAL) == otherPlayerStats.getResource(METAL) &&
					supply.getUsed() == otherPlayerStats.getSupply().getUsed());
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
