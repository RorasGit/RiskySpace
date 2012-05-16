package riskyspace.model;

import java.io.Serializable;

public class Position implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8994210304523190880L;
	private int row;
	private int col;
	
	public Position(int row, int col) {
		this.col = col;
		this.row = row;
	}
	
	public int getCol() {
		return col;
	}
	public int getRow() {
		return row;
	}
	
	public int distanceTo(Position other) {
		int y = Math.abs(this.row - other.row);
		int x = Math.abs(this.col - other.col);
		return x + y;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else if (other == null || this.getClass() != other.getClass()) {
			return false;
		} else {
			Position otherPos = (Position) other;
			return otherPos.col == this.col && otherPos.row == this.row;
		}
	}
	
	@Override
	public int hashCode() {
		return col*3 + row*7919;
	}
	
	@Override
	public String toString() {
		return "Position [" + row + "," + col + "]";
	}
}
