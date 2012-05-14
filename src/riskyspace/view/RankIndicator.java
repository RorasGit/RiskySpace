package riskyspace.view;

/**
 * RankIndicator displays the current rank and maxRank
 * @author Alexander Hederstaf
 *
 */
public abstract class RankIndicator {
	
	/*
	 * Rank variables
	 */
	private int maxRank;
	private int rank = 0;

	public abstract void setSize(int width, int height);
	public abstract void setLocation(int x, int y);
	public abstract void setBounds(int x, int y, int width, int height);
	public abstract int getWidth();
	public abstract int getHeight();
	public abstract int getX();
	public abstract int getY();
	
	public RankIndicator(int maxRank) {
		this.maxRank = maxRank;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public int getRank() {
		return rank;
	}
	
	public int getMaxRank() {
		return maxRank;
	}
}