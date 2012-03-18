package worlddemo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import movedemo.Position;
import battledemo.Fleet;
import battledemo.Scout;

public class World {
	/*
	 * Fleets by Position
	 */
	Map<Position, List<Fleet>> fleets = null;
//	Map<Position, List<Planet>> planets = null;

	int rows, cols;
	
	public World() {
		cols = 20;
		rows = 20;
		
		initMap();
		fleets.get(new Position(1, 1)).add(new Fleet(new Scout()));
	}
	
	public boolean hasFleet(Position pos) {
		return !fleets.get(pos).isEmpty();
	}
	
	/*
	 * Wrong lazy code (Moves all ships)
	 */
	public void moveShips(Position from, Position to) {
		for (int i = 0; i < fleets.get(from).size(); i++) {
			fleets.get(to).add(fleets.get(from).get(i));
		}
		fleets.get(from).removeAll(fleets.get(from));
	}
	
	public boolean hasPlanet(Position pos) {
		// TODO:
		return false;
	}
	
	public void initMap() {
		fleets = new HashMap<Position, List<Fleet>>();
//		planets = new HashMap<Position, List<Planet>>();
		for (int i = 1; i <= rows; i++) {
			for (int j = 1; j <= cols; j++) {
				fleets.put(new Position(i, j), new ArrayList<Fleet>());
//				planets.put(new Position(i, j), new ArrayList<Planet>());
			}
		}
	}
}
