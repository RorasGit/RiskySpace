package riskyspace.view;

import java.awt.Point;

public interface Clickable {
	public boolean contains(Point p);
	public boolean mousePressed(Point p);
	public boolean mouseReleased(Point p);
}
