package demo;

import java.util.List;

public class Fleet {
	
	private Player owner = null;
	private List<Ship> ships = null;
	
	public Fleet(List<Ship> ships) {
		for (int i = 0; i < ships.size(); i++) {
			this.ships.add(ships.get(i));
		}
	}
	
	public int shipCount(ShipType type) {
		int nbrOfType = 0;
		for (int i = 0; i < ships.size(); i++) {
			if (ships.get(i).getType() == type) {
				nbrOfType++;
			}
		}
		return nbrOfType;
	}
	
	public void reset() {
		for (int i = 0; i < ships.size(); i++) {
			ships.get(i).reset();
		}
	}
}