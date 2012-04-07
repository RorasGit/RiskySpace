package riskyspace.logic;

import java.util.LinkedList;

import riskyspace.model.Position;

public class Path {
	private LinkedList<Position> path = null;
	private Position current = null;
	
	public Path(Position start) {
		path = new LinkedList<Position>();
		current = start;
	}
	
	public Path(Position start, Position target){
		path = new LinkedList<Position>();
		current = start;
		addPath(target);
	}
	
	public void appendTarget(Position target) {
		addPath(target);
	}
	
	public void setTarget(Position target) {
		path = new LinkedList<Position>();
		addPath(target);
	}
	
	public Position[] getPositions() {
		Position[] path = new Position[this.path.size() + 1];
		path[0] = current;
		for (int i = 0; i < this.path.size(); i++) {
			path[i+1] = this.path.get(i);
		}
		return path;
	}
	
	private void addPath(Position target) {
		Position start = path.size() > 0 ? path.getLast() : current;
		int rows = Math.abs(start.getRow() - target.getRow());
		int cols = Math.abs(start.getCol() - target.getCol());
		int length = path.size() + cols + rows;
		int dRow;
		int dCol;
		int[] colExtra = new int[Math.min(cols, rows) + 1];
		int[] rowExtra = new int[Math.min(cols, rows) + 1];
		
		int direction = 1;
		
		if (rows >= cols) {
			direction = 0;
			dRow = rows/(cols+1);
			dCol = 1;
			for (int i = 0; i < rows%(cols+1); i++) {
				rowExtra[i] = 1;
			}
		} else {
			dRow = 1;
			dCol = cols/(rows+1);
			for (int i = 0; i < cols%(rows+1); i++) {
				colExtra[i] = 1;
			}
		}
		int index = path.size();
		int colIndex = 0;
		int rowIndex = 0;
		while (index < length) {
			Position prev = path.isEmpty() ? current : path.getLast();
			if (prev.getRow() != target.getRow() && direction == 0) {
				for (int i = 0; i < dRow + rowExtra[rowIndex]; i++) {
					prev = path.isEmpty() ? current : path.getLast();
					int rowStep = prev.getRow() < target.getRow() ? 1 : -1;
					path.add(new Position(prev.getRow() + rowStep, prev.getCol()));
					if (path.get(index).getRow() == target.getRow()) {
						index++;
						break;
					}
					index++;
				}
				rowIndex++;
			}
			if (prev.getCol() != target.getCol() && direction == 1) {
				for (int i = 0; i < dCol + colExtra[colIndex]; i++) {
					prev = path.isEmpty() ? current : path.getLast();
					int colStep = prev.getCol() < target.getCol() ? 1 : -1;
					path.add(new Position(prev.getRow(), prev.getCol() + colStep));
					if (path.get(index).getCol() == target.getCol()) {
						index++;
						break;
					}
					index++;
				}
				colIndex++;
			}
			direction = (direction + 1) % 2;
		}
	}
	
	public Position step(){
		if(path.size() > 0){
			current = path.removeFirst();
		}
		return current;
	}
	
	public Position getCurrentPos(){
		return current;
	}
	
	public int getLength(){
		return path.size();
	}
	
	@Override
	public String toString() {
		String currentS = "Path:\n" + current + "\n";
		String steps = "";
		for (int i = 0; i < path.size(); i++) {
			steps += path.get(i);
			if (!path.get(i).equals(path.getLast())) {
				steps += "\n";
			}
		}
		return currentS + steps;
	}
}