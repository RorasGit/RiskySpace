package riskyspace.view;

/**
 * RankIndicator displays the current rank and maxRank
 * @author Alexander Hederstaf & Daniel Augurell
 *
 */
public abstract class RankIndicator {
	
	private int x, y, width, height;
	/*
	 * Rank variables
	 */
	private int maxRank;
	private int rank = 0;
	
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

	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}
	
	public void setBounds(int x, int y, int width, int height) {
		setLocation(x, y);
		setSize(width, height);
	}
}