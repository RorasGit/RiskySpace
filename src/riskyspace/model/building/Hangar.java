package riskyspace.model.building;

import riskyspace.model.BuildAble;

public class Hangar implements BuildAble, Ranked {

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
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRank() {
		// TODO Auto-generated method stub
		return 0;
	}

}
