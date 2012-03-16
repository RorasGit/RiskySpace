package movedemo;

public class MoveDemo {
	public static void main(String[] args) {
		new MoveDemo();
	}
	
	Position start = null;
	Position target = null;
	
	public MoveDemo() {
		start = new Position(1,1);
		target = new Position(3,4);
		
		Position[] path = calcPath(start, target);
		for (int i = 0; i < path.length; i++) {
			System.out.println(path[i]);
		}
	}
	
	public static Position[] calcPath(Position start, Position target) {
		Position[] path = new Position[start.distanceTo(target) + 1];
		path[0] = start;
		int direction = 0;
		int index = 1;
		while (index < path.length) {
			Position next = null;
			if (path[index-1].getRow() != target.getRow() && direction == 0) {
				if (path[index-1].getRow() < target.getRow()) {
					next = new Position(path[index-1].getRow() + 1, path[index-1].getCol());
					System.out.println(index + ": A " + next + " d:" + direction);
				} else if (path[index-1].getRow() > target.getRow()) {
					next = new Position(path[index-1].getRow() - 1, path[index-1].getCol());
					System.out.println(index + ": B " + next + " d:" + direction);
				}
			} else if (path[index-1].getCol() != target.getCol() && direction == 1) {
				if (path[index-1].getCol() < target.getCol()) {
					next = new Position(path[index-1].getRow(), path[index-1].getCol() + 1);
					System.out.println(index + ": C " + next + " d:" + direction);
				} else if (path[index-1].getCol() > target.getCol()) {
					next = new Position(path[index-1].getRow(), path[index-1].getCol() - 1);
					System.out.println(index + ": D " + next + " d:" + direction);
				}
			}
			if (next != null) {
				path[index] = next;
				index++;
			}
			direction = (direction + 1) % 2;
		}
		return path;
	}
}
