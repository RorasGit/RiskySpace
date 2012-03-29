package riskyspace.logic;

import java.util.LinkedList;

import riskyspace.model.Position;

public class Path {
	private LinkedList<Position> path = null;
	private int length;
	
	public Path(Position start, Position target){
		path = new LinkedList<Position>();
		calcPath(start, target);
	}
	private void calcPath(Position start, Position target) throws IllegalArgumentException{
		int rows = Math.abs(start.getRow() - target.getRow());
		int cols = Math.abs(start.getCol() - target.getCol());
		this.length = cols + rows;
		path.add(start);
		
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
		int index = 1;
		int colIndex = 0;
		int rowIndex = 0;
		while (index < length) {
			if (path.get(index-1).getRow() != target.getRow() && direction == 0) {
				for (int i = 0; i < dRow + rowExtra[rowIndex]; i++) {
					int rowStep = path.get(index-1).getRow() < target.getRow() ? 1 : -1;
					path.add(new Position(path.get(index-1).getRow() + rowStep, path.get(index-1).getCol()));
					if (path.get(index).getRow() == target.getRow()) {
						index++;
						break;
					}
					index++;
				}
				rowIndex++;
			}
			if (path.get(index-1).getCol() != target.getCol() && direction == 1) {
				for (int i = 0; i < dCol + colExtra[colIndex]; i++) {
					int colStep = path.get(index-1).getCol() < target.getCol() ? 1 : -1;
					path.add(new Position(path.get(index-1).getRow(), path.get(index-1).getCol() + colStep));
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
		if(length > 1){
			this.path.removeFirst();
			this.length--;
		}
		return path.getFirst();
	}
	public Position getCurrentPos(){
		return path.getFirst();
	}
	public int getLength(){
		return length;
	}
	
}
