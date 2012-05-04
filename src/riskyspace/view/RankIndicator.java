package riskyspace.view;

import java.awt.Graphics;

public class RankIndicator {
	
	private int maxRank;
	private int rank;
	
	public RankIndicator(int rank, int maxRank) {
		this.rank = rank;
		this.maxRank = maxRank;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public void draw(Graphics g) {
		
	}
}