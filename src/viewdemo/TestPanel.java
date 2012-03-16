package viewdemo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import movedemo.MoveDemo;
import movedemo.Position;


public class TestPanel extends JPanel implements MouseListener {
	
	private double width, height;
	public static Point camera = new Point(300, 100);
	public static int squareSize;
	private Point[] stars = new Point[10000];
	private Position selectedPoint;
	private Dimension sizeOfGrid;
	private int startX;
	private int startY;
	private Position[] path = null;
	
	public TestPanel() {
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
		Point cursorLocation = MouseInfo.getPointerInfo().getLocation();
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
			fillRect(selectedPoint, g, new Color(240, 255, 0, 100));
		}
		if (path != null) {
			for (int i = 1; i < path.length; i++) {
				fillRect(path[i], g, new Color(240, 50, 50, 100));
			}
		}		
	}
	
	private void fillRect(Position pos, Graphics g, Color c) {
		g.setColor(c);
		g.fillRect((pos.getCol() + 2)*squareSize + 1, (pos.getRow() + 1)*squareSize + 1, squareSize -1, squareSize -1);
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

	@Override public void mouseClicked(MouseEvent me) {}
	@Override public void mouseEntered(MouseEvent me) {}
	@Override public void mouseExited(MouseEvent me) {}
	@Override public void mousePressed(MouseEvent me) {
		Point p = new Point(me.getPoint().x + camera.x, me.getPoint().y+ camera.y);
		if (legalPos(p)) {
			Position pos = getPos(p);
			if (me.getButton() == MouseEvent.BUTTON1) {
				selectedPoint = pos;
				path = null;
			}
			if (selectedPoint != null && me.getButton() == MouseEvent.BUTTON3) {
				path = MoveDemo.calcPath2(selectedPoint, pos);
			}
		} else {
			if (me.getButton() == MouseEvent.BUTTON1) {
				selectedPoint = null;
				path = null;
			}
		}
	}
	private Position getPos(Point p) {
		int col = (p.x) / squareSize - 2;
		int row = (p.y) / squareSize - 1;
		return new Position(row, col);
	}

	@Override public void mouseReleased(MouseEvent me) {}
}