package riskyspace.model.building;

import riskyspace.model.BuildAble;
import riskyspace.model.ShipType;

public class Hangar implements BuildAble, Ranked {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3015203446253905759L;
	private static final int MAX_RANK = 3;
	private int rank = 0;
	
	private String[] description = {"No Effect", "Can Build Scout\nCan Build Hunter", "Repair Fleets", "Can Build\nDestroyer"};
	
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
			return 90;
		} else {
			return 140;
		}
	}

	@Override
	public int getGasCost() {
		if (getRank() == 0) {
			return 0;
		} else if (getRank() == 1) {
			return 20;
		} else {
			return 40;
		}
	}
	public boolean canBuild(ShipType shiptype){
		if (shiptype == ShipType.SCOUT || shiptype == ShipType.HUNTER) {
			return getRank() >= 1;
		}else if (shiptype == ShipType.DESTROYER) {
			return getRank() >= 3;
		}
		return false;
	}
	
	public boolean canRepair() {
		return rank >= 2;
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