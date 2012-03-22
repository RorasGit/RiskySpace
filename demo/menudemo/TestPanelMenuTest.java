package menudemo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;


public class TestPanelMenuTest extends JPanel implements MouseListener {
	
	private double width, height;
	public static Point camera = new Point(300, 100);
	public static int squareSize;
	private Point[] stars = new Point[10000];
	private Point selectedPoint;
	private Point cursorLocation;
	private Dimension sizeOfGrid;
	private int startX;
	private int startY;
	
	public TestPanelMenuTest() {
		measureScreen();
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Point((int) (Math.random()*squareSize*26), (int) (Math.random()*squareSize*26));
		}
		sizeOfGrid = new Dimension(20, 20);
		startX = 3;
		startY = 2;
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
		for (int x = startX; x <= sizeOfGrid.width+startX; x++) {
			g.drawLine(x * squareSize, startY*squareSize, x * squareSize, squareSize*(sizeOfGrid.width+startY));
		}
		for (int y = startY; y <= sizeOfGrid.height+startY; y++) {
			g.drawLine(startX*squareSize, y * squareSize, squareSize*(sizeOfGrid.height+startX), y * squareSize);
		}
		if (legalPos(new Point(selX,selY))) {
			g.setColor(new Color(110, 170, 10, 170));
			g.fillRect(selX - selX%squareSize + 1, selY - selY%squareSize + 1, squareSize -1, squareSize -1);
		}
		if (selectedPoint != null) {
			g.setColor(new Color(240, 255, 0, 100));
			g.fillRect(selectedPoint.x - selectedPoint.x%squareSize + 1, selectedPoint.y - selectedPoint.y%squareSize + 1, squareSize -1, squareSize -1);

		}
		if (selectedPoint != null) {
			g.translate(0, 0);
			g.setColor(new Color(100,100,180));
			g.fillRect((int) width - 300 + camera.x, camera.y, (int) width + camera.x, (int) height + camera.y);
			g.setColor(Color.BLACK);
			g.drawString("Units in area", (int) width - 200 + camera.x, camera.y + 20);
			//getUnitInArea()  + " x " + getNumberOfUnits()
			g.drawString("Scout    x 3", (int) width - 210 + camera.x, camera.y + 50);
			g.drawString("Hunter  x 2", (int) width - 210 + camera.x, camera.y + 70);
		}
	}
	
	private boolean legalPos(Point loc) {
		if (loc.x <= squareSize * startX || loc.x >= squareSize * (sizeOfGrid.width+startX) || loc.y <= squareSize * startY || loc.y >= squareSize * (sizeOfGrid.height+startY)) {
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
		} else {
			selectedPoint = null;
		}
	}

	@Override public void mouseEntered(MouseEvent me) {}
	@Override public void mouseExited(MouseEvent me) {}
	@Override public void mousePressed(MouseEvent me) {}
	@Override public void mouseReleased(MouseEvent me) {}
}