package riskyspace.view;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class RankIndicator {
	
	/*
	 * Rank variables
	 */
	private int maxRank;
	private int rank = 0;
	
	/*
	 * Graphics size and location variables
	 */
	private int x, y, width, height;
	
	/*
	 * 
	 */
	private Image offLight;
	private Image onLight;
	
	public RankIndicator(int maxRank) {
		this.maxRank = maxRank;
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		/*
		 * Resize Images
		 */
		offLight = Toolkit.getDefaultToolkit().getImage("res/menu/light_off" + View.res).getScaledInstance(width, height/3, Image.SCALE_DEFAULT);
		onLight = Toolkit.getDefaultToolkit().getImage("res/menu/light_on" + View.res).getScaledInstance(width, height/3, Image.SCALE_DEFAULT);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	public void draw(Graphics g) {
		if (width != 0 && height != 0) {
			int dHeight = height/3;
			/*
			 * Draw lights
			 */
			for (int i = 1; i <= maxRank && i <= 3; i++) {
				if (i <= rank) {
					g.drawImage(onLight, x, y + height - i*dHeight, null);
				} else {
					g.drawImage(offLight, x, y + height - i*dHeight, null);
				}
			}
		}
	}
}