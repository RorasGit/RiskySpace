package viewdemo;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;
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
	private Map<String, Image> images = null;
	
	public TestPanel() {
		measureScreen();
		for (int i = 0; i < stars.length; i++) {
			stars[i] = new Point((int) (Math.random()*squareSize*26), (int) (Math.random()*squareSize*26));
		}
		sizeOfGrid = new Dimension(20, 20);
		startX = 3;
		startY = 2;
		addMouseListener(this);
		loadImages();
	}
	
	public void loadImages() {
		images = new HashMap<String, Image>();
		images.put("HEAD", Toolkit.getDefaultToolkit().createImage("res/head.png").getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
		images.put("STRAIGHT", Toolkit.getDefaultToolkit().createImage("res/straight.png").getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
		images.put("START", Toolkit.getDefaultToolkit().createImage("res/start.png").getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
		images.put("TURN", Toolkit.getDefaultToolkit().createImage("res/turn.png").getScaledInstance(squareSize, squareSize, Image.SCALE_DEFAULT));
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
			/*
			 * Rita ut pilar baserat på entry och exit med ett enum och en metod för att
			 * få fram värdet. ex entry = Path.DOWN, exit = null ritar pil från down till
			 * mitten.
			 */
			System.out.println("--->>LOOP<<---");
			for (int i = 0; i < path.length; i++) {
//				fillRect(path[i], g, new Color(240, 50, 50, 100));
				Graphics2D g2D = (Graphics2D) g;
				
				Position previous = i == 0 ? null: path[i-1];; 
				Position current = path[i]; 
				Position next = i + 1 < path.length ? path[i+1]: null;
				
				String key = getArrow(previous, next);
				double rotation = getRotation(previous, current, next);
				System.out.println("{");
				System.out.println(previous);
				System.out.println(current);
				System.out.println(next);
				System.out.println("i: " + i  + ") " + rotation);
				System.out.println("}");
				
				g2D.rotate(rotation, (path[i].getCol()+2.5)*squareSize, (path[i].getRow()+1.5)*squareSize);
				g2D.drawImage(images.get(key), (path[i].getCol()+2)*squareSize, (path[i].getRow()+1)*squareSize, null);
				g2D.rotate(-rotation, (path[i].getCol()+2.5)*squareSize, (path[i].getRow()+1.5)*squareSize);
			}
			System.out.println("-->>END-LOOP<<--");
		}
	}
	
	private double getRotation(Position previous, Position current, Position next) {
		boolean prevAbove = false;
		boolean prevUnder = false;
		boolean prevLeft = false;
		boolean prevRight = false;
		
		boolean nextRight = false;
		boolean nextUnder = false;
		boolean nextLeft = false;
		boolean nextAbove = false;
		
		if (previous != null) {
			prevAbove = previous.getRow() < current.getRow();
			prevUnder = previous.getRow() > current.getRow();
			prevLeft = previous.getCol() < current.getCol();
			prevRight = previous.getCol() > current.getCol();
		}
		if (next != null) {
			nextRight = next.getCol() > current.getCol();
			nextUnder = next.getRow() > current.getRow();
			nextLeft = next.getCol() < current.getCol();
			nextAbove = next.getRow() < current.getRow();
		}
		if (previous == null) {
			if (nextAbove) {
				return 3*Math.PI/2;
			} else if (nextUnder) {
				return Math.PI/2;
			} else if (nextLeft) {
				return Math.PI;
			} else if (nextRight) {
				return 0;
			}
		}
		if (next == null) {
			if (prevAbove) {
				return Math.PI/2;
			} else if (prevUnder) {
				return 3*Math.PI/2;
			} else if (prevLeft) {
				return 0;
			} else if (prevRight) {
				return Math.PI;
			}
		}
		
		if (prevAbove) {
			if (nextUnder) {
				return Math.PI/2;
			} else if (nextLeft) {
				return Math.PI/2;
			} else if (nextRight) {
				return Math.PI;
			}
		} else if (nextAbove) {
			if (prevUnder) {
				return Math.PI/2;
			} else if (prevLeft) {
				return Math.PI/2;
			} else if (prevRight) {
				return Math.PI;
			}
		} else if (prevUnder) {
			if (nextAbove) {
				return Math.PI/2;
			} else if (nextLeft) {
				return 0;
			} else if (nextRight) {
				return 3*Math.PI/2;
			}
		} else if (nextUnder) {
			if (prevAbove) {
				return Math.PI/2;
			} else if (prevLeft) {
				return 0;
			} else if (prevRight) {
				return 3*Math.PI/2;
			}
		}
		return 0;
	}
	
	private String getArrow(Position previous, Position next) {
		if (previous == null) {
			return "START";
		}
		if (next == null) {
			return "HEAD";
		} if (previous.getCol() != next.getCol() && previous.getRow() != next.getRow()) {
			return "TURN";
		} else {
			return "STRAIGHT";
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