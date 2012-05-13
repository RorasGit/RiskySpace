package riskyspace.view.openglImpl;

public class Rectangle {
	private int x, y, width, height;
	
	public Rectangle(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;		
		this.height = height;
	}
	
	public Rectangle(Rectangle other) {
		this(other.x, other.y, other.width, other.height);
	}
	
	public boolean intersects(Rectangle other) {
		if (other == null || other.width == 0 || other.height == 0) {
			return false;
		}
		boolean thisContains = contains(other.x, other.y) || contains(other.x + other.width, other.y) 
				|| contains(other.x, other.y + other.height) || contains(other.x + other.width, other.y + other.height);
		boolean otherContains = thisContains || other.contains(x, y) || other.contains(x + width, y) 
				|| other.contains(x, y + height) || other.contains(x + width, y + height);
		return thisContains || otherContains;
	}
	
	public boolean contains(int x, int y) {
		return (x >= this.x && x <= this.x + width) && (y >= this.y && y <= this.y + this.height);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public String toString() {
		return "Rectangle [x: " + x + ", y: " + y + ", width: " + width
				+ ", height: " + height + "]";
	}
}