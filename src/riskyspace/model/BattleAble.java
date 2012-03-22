package riskyspace.model;

import java.util.List;

public interface BattleAble {
	
	/**
	 * Make this unit take damage by an attack.
	 * @param damage The amount of damage caused by the attack
	 * @return true if the unit was destroyed, else false.
	 */
	public boolean takeDamage(int damage);
	
	public List<Integer> getAttacks();

}
