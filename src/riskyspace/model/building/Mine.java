package riskyspace.model.building;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.Expose;

import riskyspace.GameManager;
import riskyspace.model.BuildAble;
import riskyspace.model.Resource;
import riskyspace.services.Event;

public class Mine implements BuildAble, Ranked {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5355474270301844204L;
	private static final int MAX_RANK = 3;
	@Expose
	private int rank = 1;
	
	private Map<Resource, Map<Integer, Integer>> miningValues = null;
	private Resource resource = null;

	private String[] gasDescription = {"No Mine", "10 Gas", "20 Gas", "30 Gas"};
	private String[] metalDescription = {"No Mine", "20 Metal", "40 Metal", "60 Metal"};
	private String[] description = null;
	
	public Mine(Resource resource) {
		initMiningValues();
		this.resource = resource;
		description = resource == Resource.METAL ? metalDescription : gasDescription;
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
	
	public int mine() {
		return miningValues.get(resource).get(rank);
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
			Event evt = new Event(Event.EventTag.INCOME_CHANGED, null);
			GameManager.INSTANCE.handleEvent(evt, null);
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

	@Override
	public String getDescriptiveString(int rank) {
		if (rank < description.length && rank >= 0) {
			return description[rank];
		}
		return "";
	}
}