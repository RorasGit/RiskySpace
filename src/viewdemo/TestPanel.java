package viewdemo;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;


public class TestPanel extends JPanel {
	
	private double width, height;
	public static Point camera = new Point();
	public static int squareSize;
	private Point[] stars = new Point[10000];
	
	public TestPanel() {
		measureScreen();
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Point((int) (Math.random()*squareSize*21), (int) (Math.random()*squareSize*21));
		}
	}
	
	public void paintComponent(Graphics g) {
		Point loc = MouseInfo.getPointerInfo().getLocation();
		int selX = loc.x + camera.x;
		int selY = loc.y + camera.y;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0,(int) width,(int) height);
		g.translate(-camera.x, -camera.y);
		g.setColor(Color.WHITE);
		for (int i = 0; i < stars.length; i++) {
			g.fillRect(stars[i].x, stars[i].y, 1, 1);
		}
		g.setColor(new Color(130,130,130));
		for (int x = 0; x < 21; x++) {
			g.drawLine(x * squareSize, 0, x * squareSize, squareSize*21);
		}
		for (int y = 0; y < 21; y++) {
			g.drawLine(0, y * squareSize, squareSize*21, y * squareSize);
		}
		
		g.setColor(new Color(140, 230, 10, 110));
		g.fillRect(selX - selX%squareSize + 1, selY - selY%squareSize + 1, squareSize -1, squareSize -1);
	}
	
	public void measureScreen() {
		width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		squareSize = Math.min((int) (width/6),(int) (height/6));
	}
}