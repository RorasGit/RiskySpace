package riskyspace.model;

import java.util.HashMap;
import java.util.Map;

import static riskyspace.model.Resource.METAL;
import static riskyspace.model.Resource.GAS;

public class PlayerStats {
	private Map<Resource, Integer> resources = null;
	private Map<Resource, Integer> income = null;
	
	private static final int BASE_SUPPLY = 3;
	private int numberOfColonies;
	private int usedSupply;
	
	public PlayerStats() {
		initResources();
		initIncome();
	}
	
	private void initResources() {
		resources = new HashMap<Resource, Integer>();
		resources.put(METAL, 200);
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
	public boolean purchase(Resource resource, int amount) {
		if (resources.get(resource) >= amount) {
			resources.put(resource, resources.get(resource) - amount);
			return true;
		} else {
			return false;
		}
	}

	/*
	 * TODO: Method to check if one has enough resources (boolean)
	 */
	
	public void update(int numberOfColonies, int usedSupply) {
		this.numberOfColonies = numberOfColonies;
		this.usedSupply = usedSupply;
	}
	
	public boolean hasEnoughSupply(int supplyIncrease) {
		return usedSupply + supplyIncrease <= getMaxSupply();
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
	}
	
	public int getResource(Resource resource) {
		return resources.get(resource);
	}
	
	public int getNumberOfColonies() {
		return numberOfColonies;
	}
	
	public int getUsedSupply() {
		return usedSupply;
	}
	
	public int getMaxSupply() {
		return BASE_SUPPLY + (2 * numberOfColonies);
	}
	
	public boolean isSupplyCapped() {
		return usedSupply >= getMaxSupply();
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
					usedSupply == otherPlayerStats.usedSupply && 
					numberOfColonies == otherPlayerStats.numberOfColonies);
		}
	}
	
	@Override
	public String toString() {
		return "PlayerStats [" + "Metal: " + resources.get(METAL) + "  Income: " + income.get(METAL) + ", " + 
				"Gas: " + resources.get(GAS) + "  Income: " + income.get(GAS) + 
				", " +  "Number of colonies: " + numberOfColonies + ", " + "Supply: " + usedSupply + "/" +
				getMaxSupply() + " ]";
	}
	
	@Override
	public int hashCode() {
		return resources.get(GAS)*3 + resources.get(METAL)*5 + usedSupply*7 + numberOfColonies*11;
	}
}