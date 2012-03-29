package riskyspace.model;

public interface MoveAble {
	/**
	 * Check if this MoveAble has energy left to use
	 * @return true if this MoveAble has at least 1 energy left.
	 */
	public boolean hasEnergy();
	
	/**
	 * Returns how much energy this ship has left.
	 */
	public int getEnergy();
	
	/**
	 * Use one energy
	 * @return true if energy was used.
	 */
	public boolean useEnergy();
}
