package riskyspace.model.building;

import java.util.ArrayList;
import java.util.List;

import riskyspace.model.BattleAble;
import riskyspace.model.BuildAble;

public class Turret implements BuildAble, BattleAble, Ranked {

	private static final int MAX_RANK = 3;
	private int rank = 1;
	
	private final int baseShield = 10;
	private int shield = baseShield;
	
	private String[] description = {"No Turret", "5-7 damage\n10 shield", "12-16 damage\n25 shield"
			, "22-29 damage\n40 shield"};
	
	@Override
	public boolean takeDamage(int damage) {
		shield -= damage;
		return shield <= 0;
	}

	@Override
	public List<Integer> getAttacks() {
		int damage = 0;
		int variation = 0;
		if (rank > 0) {
			damage += 5;
			variation += 2;
		}
		if (rank > 1) {
			damage += 7;
			variation += 2;
		}
		if (rank > 2) {
			damage += 10;
			variation += 3;
		}
		List<Integer> attacks = new ArrayList<Integer>();
		if (shield > 0) {
			attacks.add(damage + (int) (Math.random()*(variation+1)));
		}
		return attacks;
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
			return 60;
		} else if (getRank() == 2) {
			return 120;
		} else {
			return 0;
		}
	}

	@Override
	public int getGasCost() {
		if (getRank() == 2) {
			return 25;
		} else {
			return 0;
		}
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
		shield = baseShield + (rank - 1) * 15;
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