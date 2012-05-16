package riskyspace.model.building;

import java.io.Serializable;

/**
 * Interface for items having different Ranks
 * @author flygarN
 *
 */
public interface Ranked extends Serializable {
	
	/**
	 * Rank of this item.
	 */
	public int getRank();
	
	/**
	 * If possible, increases the rank of this
	 * Ranked item to the next rank.
	 */
	public void upgrade();
	
	/**
	 * Returns if this item is max rank
	 * or not.
	 * @return <code>true</code> if max rank else
	 * <code>false</code>.
	 */
	public boolean isMaxRank();

	/**
	 * Returns this Ranked items maximum
	 * possible rank.
	 */
	public int getMaxRank();
	
	/**
	 * Returns a descriptive String of this rank
	 * @param rank The rank to describe.
	 */
	public String getDescriptiveString(int rank);
}