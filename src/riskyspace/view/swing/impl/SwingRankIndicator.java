package riskyspace.view.swing.impl;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import riskyspace.view.RankIndicator;
import riskyspace.view.swing.SwingRenderAble;

public class SwingRankIndicator extends RankIndicator implements SwingRenderAble {
	
	private Image redLight;
	private Image greenLight;
	private Image offLight;
	
	public SwingRankIndicator(int maxRank) {
		super(maxRank);
	}

	public void setSize(int width, int height) {
		super.setSize(width, height);
		/*
		 * Resize Images
		 */
		redLight = Toolkit.getDefaultToolkit().getImage("res/menu/light_red.png").getScaledInstance(width, height/3, Image.SCALE_DEFAULT);
		greenLight = Toolkit.getDefaultToolkit().getImage("res/menu/light_green.png").getScaledInstance(width, height/3, Image.SCALE_DEFAULT);
		offLight = Toolkit.getDefaultToolkit().getImage("res/menu/light_off.png").getScaledInstance(width, height/3, Image.SCALE_DEFAULT);
	}
	
	
	public void draw(Graphics g) {
		if (getWidth() != 0 && getHeight() != 0) {
			int dHeight = getHeight()/3;
			/*
			 * Draw lights
			 */
			for (int i = 1; i <= 3; i++) {
				if (i <= getMaxRank()) {
					if (i <= getRank()) {
						g.drawImage(greenLight, getX(), getY() + getHeight() - i*dHeight, null);
					} else {
						g.drawImage(redLight, getX(), getY() + getHeight() - i*dHeight, null);
					}
				} else {
					g.drawImage(offLight, getX(), getY() + getHeight() - i*dHeight, null);
				}
			}
		}
	}

}