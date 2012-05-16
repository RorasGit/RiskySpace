package riskyspace.model;

import java.io.Serializable;

/**
 * Interface for colonies and ships for determining what territories they can see.
 * @author flygarN
 *
 */
public interface Sight extends Serializable{
	/**
	 * The distance in positions that this unit can see.
	 */
	public int getSightRange();
}
