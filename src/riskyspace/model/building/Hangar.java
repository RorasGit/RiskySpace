package riskyspace.model.building;

import riskyspace.model.BuildAble;
import riskyspace.model.ShipType;

public class Hangar implements BuildAble, Ranked {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3015203446253905759L;
	private static final int MAX_RANK = 2;
	private int rank = 0;
	
	private String[] description = {"No Effect", "Can Build Scout\nCan Build Hunter", "Can Build Destroyer"};
	
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
			return 50;
		} else if (getRank() == 1) {
			return 120;
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
		} else {
			return 0;
		}
	}
	public boolean canBuild(ShipType shiptype){
		if (shiptype == ShipType.SCOUT) {
			return getRank() >= 1;
		}else if (shiptype == ShipType.COLONIZER) {
			return getRank() >= 3;
		}else if (shiptype == ShipType.DESTROYER) {
			return getRank() >= 3;
		}else if (shiptype == ShipType.HUNTER) {
			return getRank() >= 1;
		}
		return false;
	}

	@Override
	public int getBuildTime() {
		return 1;
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
		if (rank < description.length && rank >= 0) {
			return description[rank];
		}
		return "";
	}
}