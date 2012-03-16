package viewdemo;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;


public class TestPanel extends JPanel implements MouseListener {
	
	private double width, height;
	public static Point camera = new Point(300, 100);
	public static int squareSize;
	private Point[] stars = new Point[10000];
	private Point selectedPoint;
	private Point cursorLocation;
	
	public TestPanel() {
		measureScreen();
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Point((int) (Math.random()*squareSize*26), (int) (Math.random()*squareSize*26));
		}
		addMouseListener(this);
	}
	
	public void paintComponent(Graphics g) {
		cursorLocation = MouseInfo.getPointerInfo().getLocation();
		int selX = cursorLocation.x + camera.x;
		int selY = cursorLocation.y + camera.y;
		g.setColor(Color.BLACK);
		g.fillRect(0, 0,(int) width,(int) height);
		g.translate(-camera.x, -camera.y);
		g.setColor(Color.WHITE);
		for (int i = 0; i < stars.length; i++) {
			g.fillRect(stars[i].x, stars[i].y, 1, 1);
		}
		g.setColor(new Color(130,130,130));
		for (int x = 3; x < 21; x++) {
			g.drawLine(x * squareSize, 2*squareSize, x * squareSize, squareSize*20);
		}
		for (int y = 2; y < 21; y++) {
			g.drawLine(3*squareSize, y * squareSize, squareSize*20, y * squareSize);
		}
		if (legalPos(new Point(selX,selY))) {
		g.setColor(new Color(110, 170, 10, 170));
		g.fillRect(selX - selX%squareSize + 1, selY - selY%squareSize + 1, squareSize -1, squareSize -1);
		}
		if (selectedPoint != null) {
			g.setColor(new Color(240, 255, 0, 100));
			g.fillRect(selectedPoint.x - selectedPoint.x%squareSize + 1, selectedPoint.y - selectedPoint.y%squareSize + 1, squareSize -1, squareSize -1);
		}
		
	}
	
	private boolean legalPos(Point loc) {
		if (loc.x <= squareSize * 3 || loc.x >= squareSize * 23 || loc.y <= squareSize * 2 || loc.y >= squareSize * 22) {
			return false;
		} else {
			return true;
		}
	}

	public void measureScreen() {
		width = Toolkit.getDefaultToolkit().getScreenSize().getWidth();
		height = Toolkit.getDefaultToolkit().getScreenSize().getHeight();
		squareSize = Math.min((int) (width/6),(int) (height/6));
	}

	@Override
	public void mouseClicked(MouseEvent evt) {
		Point p = new Point(evt.getPoint().x + camera.x, evt.getPoint().y+ camera.y);
		if (legalPos(p)) {
			selectedPoint = p;
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent evt) {

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}