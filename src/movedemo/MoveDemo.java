package movedemo;

public class MoveDemo {
	public static void main(String[] args) {
		new MoveDemo();
	}
	
	Position start = null;
	Position target = null;
	
	public MoveDemo() {
		start = new Position(1,1);
		target = new Position(2,4);
		
		Position[] path = calcPath(start, target);
		for (int i = 0; i < path.length; i++) {
			System.out.println(path[i]);
		}
	}
	
	public Position[] calcPath(Position start, Position target) {
		Position[] path = new Position[start.distanceTo(target) + 1];
		path[0] = start;
		int direction = 0;
		for (int i = 1; i < path.length; i++) {
			Position next = null;
			if (direction == 0 || path[i-1].getCol() == target.getCol()) {
				if (path[i-1].getRow() < target.getRow()) {
					next = new Position(path[i-1].getRow() + 1, path[i-1].getCol());
				} else if (path[i-1].getRow() > target.getRow()) {
					next = new Position(path[i-1].getRow() - 1, path[i-1].getCol());
				}
			} else if (direction == 1 || path[i-1].getRow() == target.getRow()) {
				if (path[i-1].getCol() < target.getCol()) {
					System.out.println("+col");
					next = new Position(path[i-1].getRow(), path[i-1].getCol() + 1);
				} else if (path[i-1].getCol() > target.getCol()) {
					System.out.println("-col");
					next = new Position(path[i-1].getRow(), path[i-1].getCol() - 1);
				}
			}
			path[i] = next;
			direction = (direction + 1) % 2;
		}
		return path;
	}
}
