package riskyspace.model.building;

import java.util.List;

import riskyspace.model.BattleAble;
import riskyspace.model.BuildAble;

public class Turret implements BuildAble, BattleAble, Ranked {

	@Override
	public boolean takeDamage(int damage) {
		return false;
	}

	@Override
	public List<Integer> getAttacks() {
		return null;
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
		return 0;
	}

	@Override
	public int getGasCost() {
		return 0;
	}

	@Override
	public int getBuildTime() {
		return 0;
	}

	@Override
	public int getRank() {
		return 0;
	}

}
