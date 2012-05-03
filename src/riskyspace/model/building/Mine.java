package riskyspace.model.building;

import java.util.HashMap;
import java.util.Map;

import riskyspace.model.BuildAble;
import riskyspace.model.Resource;

public class Mine implements BuildAble, Ranked {

	private static final int MAX_RANK = 3;
	private int rank = 1;

	private Map<Resource, Map<Integer, Integer>> miningValues = null;
	
	public Mine() {
		initMiningValues();
	}
	
	private void initMiningValues() {
		miningValues = new HashMap<Resource, Map<Integer, Integer>>();
		Map<Integer, Integer> metal = new HashMap<Integer, Integer>();
		Map<Integer, Integer> gas = new HashMap<Integer, Integer>();
		metal.put(1, 20);
		metal.put(2, 40);
		metal.put(3, 60);
		gas.put(1, 10);
		gas.put(2, 20);
		gas.put(3, 30);
		miningValues.put(Resource.METAL, metal);
		miningValues.put(Resource.GAS, gas);
	}
	
	public int mine(Resource res) {
		return miningValues.get(res).get(rank);
	}
	
	@Override
	public int getSupplyCost() {
		/*
		 * Buildings doesn't cost supply
		 */
		return 0;
	}

	@Override
	public int getMetalCost() {
		if (getRank() == 1) {
			return 150;
		} else if (getRank() == 2) {
			return 200;
		} else {
			return 0;
		}
	}

	@Override
	public int getGasCost() {
		return 0;
	}

	@Override
	public int getBuildTime() {
		return 2;
	}

	@Override
	public int getRank() {
		return rank;
	}

	@Override
	public void upgrade() {
		if (rank < MAX_RANK) {
			rank++;
		}
	}

	@Override
	public boolean isMaxRank() {
		return rank == MAX_RANK;
	}

	@Override
	public int getMaxRank() {
		return MAX_RANK;
	}
}