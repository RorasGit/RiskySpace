package riskyspace.model;

/**
 * Interface for colonies and ships for determining what territories they can see.
 * @author flygarN
 *
 */
public interface Sight {
	/**
	 * The distance in positions that this unit can see.
	 */
	public int getSightRange();
}
