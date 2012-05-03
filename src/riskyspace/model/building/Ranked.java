package riskyspace.model.building;
/**
 * Interface for buildings having different Ranks
 * @author flygarN
 *
 */

public interface Ranked {
	
	/**
	 * Rank of this item.
	 */
	public int getRank();
	
	public void upgrade();
	
	public boolean isMaxRank();
	
	public int getMaxRank();
}