package riskyspace.view.swing.impl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import riskyspace.view.RankIndicator;
import riskyspace.view.View;
import riskyspace.view.swing.SwingDrawable;

public class SwingRankIndicator extends RankIndicator implements SwingDrawable {
	
	public SwingRankIndicator(int maxRank) {
		super(maxRank);
	}

	/*
	 * Graphics size and location variables
	 */
	private int x, y, width, height;
	
	/*
	 * 
	 */
	private Image redLight;
	private Image greenLight;
	private Image offLight;
	
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		/*
		 * Resize Images
		 */
		redLight = Toolkit.getDefaultToolkit().getImage("res/menu/light_red" + View.res).getScaledInstance(width, height/3, Image.SCALE_DEFAULT);
		greenLight = Toolkit.getDefaultToolkit().getImage("res/menu/light_green" + View.res).getScaledInstance(width, height/3, Image.SCALE_DEFAULT);
		offLight = Toolkit.getDefaultToolkit().getImage("res/menu/light_off" + View.res).getScaledInstance(width, height/3, Image.SCALE_DEFAULT);
	}
	
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public void setBounds(int x, int y, int width, int height) {
		setLocation(x, y);
		setSize(width, height);
	}
	
	public void draw(Graphics g) {
		if (width != 0 && height != 0) {
			int dHeight = height/3;
			/*
			 * Draw lights
			 */
			for (int i = 1; i <= 3; i++) {
				if (i <= getMaxRank()) {
					if (i <= getRank()) {
						g.drawImage(greenLight, x, y + height - i*dHeight, null);
					} else {
						g.drawImage(redLight, x, y + height - i*dHeight, null);
					}
				} else {
					g.drawImage(offLight, x, y + height - i*dHeight, null);
				}
			}
		}
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
}