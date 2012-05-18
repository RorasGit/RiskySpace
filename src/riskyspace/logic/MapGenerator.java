package riskyspace.logic;

import java.util.HashMap;
import java.util.Map;

import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Resource;
import riskyspace.model.Ship;
import riskyspace.model.ShipType;
import riskyspace.model.Territory;

public class MapGenerator {
	
	public static Map<Position, Territory> generateMap(int rows, int cols, int numberOfPlayers){
		Map<Position, Territory> territories = new HashMap<Position, Territory>();
		initTerritories(territories, rows, cols);
		setPlanets(territories, rows, cols, numberOfPlayers);
		return territories;
	}
	
	private static void initTerritories(Map<Position, Territory> territories, int rows, int cols) {
		for (int row = 1; row <= rows; row++) {
			for (int col = 1; col <= cols; col++) {
				territories.put(new Position(row, col), new Territory());
			}
		}
	}
	
	private static void setPlanets(Map<Position, Territory> territories, int rows, int cols, int numberOfPlayers) {
		/*
		 * Starting planets
		 */
		setStartPlanets(territories, rows, cols, numberOfPlayers);

		int planetCount = 0;
		int resourceIntervall = 1;
		while (planetCount < 25) {
			Position random = new Position((int) (Math.random() * (rows-2) + 2),(int) (Math.random() * (cols-2) + 2));
			if (checkNeighboringPlanets(territories, random, rows, cols)) {
				territories.get(random).setPlanet(((resourceIntervall % 3) != 0 ? Resource.METAL: Resource.GAS));
				resourceIntervall++;
				planetCount++;
			} 
		}
	}

	private static void setStartPlanets(Map<Position, Territory> territories, int rows, int cols, int numberOfPlayers) {
		
		int[][] startPos = new int[][]{
				{3, 3},
				{rows - 3, cols - 3},
				{3, cols - 3},
				{rows - 3, 3},
		};
		int[][] closePos = new int[][]{
				{+2, -1, -1, +2},
				{-2, +1, +1, -2},
				{-2, -1, +1, -2},
				{-1, -2, +2, +1},
		};
		Player[] players = new Player[]{
				Player.RED,
				Player.BLUE,
				Player.PINK,
				Player.GREEN,
		};
		
		for (int i = 0; i < numberOfPlayers; i++) {
			Position pos = startPos(startPos[i][0], startPos[i][1]);
			territories.get(pos).setHomePlanet(players[i]);
			territories.get(new Position(pos.getRow()+ closePos[i][0],pos.getCol()+ closePos[i][1])).setPlanet(Resource.METAL);
			territories.get(new Position(pos.getRow()+ closePos[i][2],pos.getCol()+ closePos[i][3])).setPlanet(Resource.METAL);
			addStartingFleets(territories.get(pos));
		}
	}

	private static Position startPos(int row, int col) {
		return new Position(row + ((int) (Math.random() * 2)), col + ((int) (Math.random() * 2)));
	}
	
	/**
	 * Check for other planets around the current position.
	 * 
	 * @return return true if no planets are present.
	 */
	private static boolean checkNeighboringPlanets(Map<Position, Territory> territories, Position pos, int rows, int cols) {
		/*
		 * Check inner matrix
		 */
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				Position p = new Position(pos.getRow() + i, pos.getCol() + j);
				if (legalPos(p, rows, cols)) {
					if (!legalPlanetPosition(territories, p, rows, cols)) {
						return false;
					}
				}
			}
		}
		for (int i = -2; i <= 2; i += 4) {
			/*
			 *	Check midcolumn outer bounds 
			 */
			Position p = new Position(pos.getRow() + i, pos.getCol());
			if (legalPos(p, rows, cols)) {
				if (!legalPlanetPosition(territories, p, rows, cols)) {
					return false;
				}
			}
			/*
			 *	Check midrow outer bounds 
			 */
			p = new Position(pos.getRow(), pos.getCol() + i);
			if (legalPos(p, rows, cols)) {
				if (!legalPlanetPosition(territories, p, rows, cols)) {
					return false;
				}
			}

		}
		return true;
	}
	
	private static boolean legalPos(Position p, int rows, int cols) {
		return (p.getCol() > 0 && p.getCol() <= cols && p.getRow() > 0 && p
				.getRow() <= rows);
	}

	private static boolean legalPlanetPosition(Map<Position, Territory> territories, Position p, int rows, int cols) {
		if (territories.get(p).hasPlanet()) {
			return false;
		} else if (p.getRow() <= 5 && p.getCol() <= 5) {
			return false;
		} else if (p.getRow() >= rows-4 && p.getCol() >= cols-4) {
			return false;
		}
		return true;
	}
	
	private static void addStartingFleets(Territory t) {
		t.addFleet(new Fleet(new Ship(ShipType.SCOUT), t.getColony().getOwner()));
	}
}