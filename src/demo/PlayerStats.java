package demo;

import java.util.HashMap;
import java.util.Map;

public class PlayerStats {
	private Map<Resource, Integer> resources = null;
	private Map<Resource, Integer> income = null;
	
	public PlayerStats() {
		initResources();
		initIncome();
	}
	
	private void initResources() {
		resources = new HashMap<Resource, Integer>();
		resources.put(Resource.METAL, 200);
		resources.put(Resource.GAS, 0);
	}
	
	private void initIncome() {
		income = new HashMap<Resource, Integer>();
		income.put(Resource.METAL, 50);
		income.put(Resource.GAS, 0);
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
}