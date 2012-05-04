package riskyspace.model.building;

import riskyspace.model.BuildAble;
import riskyspace.model.Sight;

public class Radar implements BuildAble, Ranked, Sight {

	private static final int MAX_RANK = 3;
	private int rank = 0;
	
	private String[] description = {"Sight 2\nNo Radar", "Sight 3", "Sight 4", "Sight 5"};
	
	@Override
	public int getSupplyCost() {
		/*
		 * Buildings doesn't cost supply
		 */
		return 0;
	}

	@Override
	public int getMetalCost() {
		if (getRank() == 0) {
			return 80;
		} else if (getRank() == 1) {
			return 120;
		} else if (getRank() == 2) {
			return 200;
		} else {
			return 0;
		}
	}

	@Override
	public int getGasCost() {
		if (getRank() == 0) {
			return 0;
		} else if (getRank() == 1) {
			return 20;
		} else if (getRank() == 2) {
			return 100;
		} else {
			return 0;
		}
	}

	@Override
	public int getBuildTime() {
		if (getRank() == MAX_RANK - 1) {
			return 2;
		}
		return 1;
	}

	@Override
	public int getRank() {
		return rank;
	}

	@Override
	public int getSightRange() {
		return 2 + rank;
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

	@Override
	public String getDescriptiveString(int rank) {
		return description[rank];
	}
}