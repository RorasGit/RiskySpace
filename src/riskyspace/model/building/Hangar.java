package riskyspace.model.building;

import riskyspace.model.BuildAble;

public class Hangar implements BuildAble, Ranked {

	private static final int MAX_RANK = 2;
	private int rank = 0;
	
	private String[] description = {"No Hangar", "Can Build Scouts\nCan Build Hunter", "Can Build Destroyer"};
	
	@Override
	public int getSupplyCost() {
		/*
		 * Buildings doesn't cost supply
		 */
		return 0;
	}

	@Override
	public int getMetalCost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getGasCost() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getBuildTime() {
		return 0;
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

	@Override
	public String getDescriptiveString(int rank) {
		return description[rank];
	}
}