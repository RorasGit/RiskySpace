package riskyspace.logic;

import java.io.Serializable;
import java.util.LinkedList;

import riskyspace.model.Position;

import static java.lang.Math.PI;

public class Path implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -489479371459853926L;
	
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
	
	/**
	 * Determine the Rotation in radians for a Path arrow part depending on path segments, or
	 * for a ship depending on its Path.
	 * @param previous The path segment previous to the current one.
	 * @param current The current path segment.
	 * @param next The next path segment.
	 * @return Rotation for the current segment in radians.
	 * @see Math.toDegrees(double radian)
	 */
	public static double getRotation(Position previous, Position current, Position next) {
		/*
		 * Return 0 if we can not calculate a rotation.
		 * - current is null
		 * - next equals previous
		 */
		if (current == null || (next == null && previous == null) || (next != null && next.equals(previous) 
				|| (previous != null && previous.equals(next)))) {
			return 0;
		}
		
		/*
		 * Variables for determining where the previous and next positions
		 * are relative to the current position
		 */
		boolean prevAbove = false;
		boolean prevUnder = false;
		boolean prevLeft = false;
		boolean prevRight = false;
		
		boolean nextRight = false;
		boolean nextUnder = false;
		boolean nextLeft = false;
		boolean nextAbove = false;
		
		/*
		 * Only calculate values if there is a previous
		 * respective next position.
		 */
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
		/*
		 * If previous is null then only determine rotation based on 
		 * next position.
		 */
		if (previous == null) {
			if (nextAbove) {
				return 2*PI/2;
			} else if (nextUnder) {
				return 0;
			} else if (nextLeft) {
				return PI/2;
			} else if (nextRight) {
				return 3*PI/2;
			}
		}
		/*
		 * If next is null then only determine rotation based on 
		 * previous position.
		 */
		if (next == null) {
			if (prevAbove) {
				return 0;
			} else if (prevUnder) {
				return PI;
			} else if (prevLeft) {
				return 3*PI/2;
			} else if (prevRight) {
				return PI/2;
			}
		}
		
		/*
		 * Column movement first
		 */
		if (prevLeft) {
			if (nextRight) {
				return 3*PI/2;
			} else if (nextAbove) {
				return 0;
			} else {
				return 3*PI/2;
			}
		} else if (prevRight) {
			if (nextLeft) {
				return PI/2;
			} else if (nextAbove) {
				return PI/2;
			} else {
				return PI;
			}
		}
		
		/*
		 * Row movement first
		 */
		if (prevAbove) {
			if (nextUnder) {
				return 0; // Standard rotation
			} else if (nextLeft) {
				return 0;
			} else {
				return PI/2;
			}
		} else if (prevUnder) {
			if (nextAbove) {
				return PI;
			} else if (nextLeft) {
				return 3*PI/2;
			} else {
				return PI;
			}
		}
		
		return 0;
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