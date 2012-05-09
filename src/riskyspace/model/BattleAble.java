package riskyspace.model;

import java.io.Serializable;
import java.util.List;

public interface BattleAble extends Serializable{
	
	/**
	 * Make this unit take damage by an attack.
	 * @param damage The amount of damage caused by the attack
	 * @return true if the unit was destroyed, else false.
	 */
	public boolean takeDamage(int damage);
	
	public List<Integer> getAttacks();

}
