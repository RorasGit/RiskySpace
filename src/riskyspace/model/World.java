package riskyspace.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
	private int rows = 0;
	private int cols = 0;
	private Map<Position, Territory> territories = null;
	private Map<Player, PlayerStats> playerstats = null;
	private List<Player> players = null;

	public World(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		players = new ArrayList<Player>();
		players.add(Player.RED);
		players.add(Player.BLUE);
		initTerritories();
		initPlayers();
		setPlanets();

		/*
		 * Set Planets TODO: Roras Set Starting Fleets
		 */
	}

	public World() {
		this(20, 20);
	}

	/**
	 * Check for other planets in a 3x3 grid with current position in the
	 * middle.
	 * 
	 * @return return true if no planets are present.
	 */
	private boolean checkNeighboringPlanets(Position pos) {
		/*
		 * Check inner matrix
		 */
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				Position p = new Position(pos.getRow() + i, pos.getCol() + j);
				if (legalPos(p)) {
					if (!legalPlanetPosition(p)) {
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
			if (legalPos(p)) {
				if (!legalPlanetPosition(p)) {
					return false;
				}
			}
			/*
			 *	Check midrow outer bounds 
			 */
			p = new Position(pos.getRow(), pos.getCol() + i);
			if (legalPos(p)) {
				if (!legalPlanetPosition(p)) {
					return false;
				}
			}

		}
		return true;
	}

	private boolean legalPlanetPosition(Position p) {

		if (territories.get(p).hasPlanet()) {
			return false;
		} else if (p.getRow() <= 5 && p.getCol() <= 5) {
			return false;
		} else if (p.getRow() >= 16 && p.getCol() >= 16) {
			return false;
		}
		return true;
	}

	private boolean legalPos(Position p) {
		return (p.getCol() > 0 && p.getCol() <= cols && p.getRow() > 0 && p
				.getRow() <= rows);
	}

	private void setPlanets() {
		/*
		 * Starting planets
		 */
		setStartPlanets();

		int maxPlanets = 25;
		int planetCount = 0;
		int resourceIntervall = 1;
		int fail = 0;
		while (planetCount < maxPlanets) {

			// TODO: something that randoms out a planet at a position
			Position random = new Position((int) (Math.random() * (rows-2) + 2),(int) (Math.random() * (cols-2) + 2));
			if (checkNeighboringPlanets(random)) {
				territories.get(random).setPlanet(((resourceIntervall % 3) != 0 ? Resource.METAL: Resource.GAS));
				resourceIntervall++;
				planetCount++;
			} else {
				System.out.println(fail++);
			}
		}
	}

	private void setStartPlanets() {
		//Player 1
		Position pos = new Position(3 + ((int) (Math.random() * 2)),
				3 + ((int) (Math.random() * 2)));
		territories.get(pos).setPlanet(Resource.METAL);
		territories.get(pos).getPlanet().buildColony(Player.RED);
		//Player 1�s closest planets
		territories.get(new Position(pos.getRow()+2,pos.getCol()-1)).setPlanet(Resource.METAL);
		territories.get(new Position(pos.getRow()-1,pos.getCol()+2)).setPlanet(Resource.METAL);
		//Player 2
		pos = new Position((rows - 3) + ((int) (Math.random() * 2)), (cols - 3)
				+ ((int) (Math.random() * 2)));
		territories.get(pos).setPlanet(Resource.METAL);
		territories.get(pos).getPlanet().buildColony(Player.BLUE);
		//Player 2�s closest planets
		territories.get(new Position(pos.getRow()-2,pos.getCol()+1)).setPlanet(Resource.METAL);
		territories.get(new Position(pos.getRow()+1,pos.getCol()-2)).setPlanet(Resource.METAL);
		
		
	}

	private void initTerritories() {
		territories = new HashMap<Position, Territory>();
		for (int row = 1; row <= rows; row++) {
			for (int col = 1; col <= cols; col++) {
				territories.put(new Position(row, col), new Territory());
			}
		}
	}

	private void initPlayers() {
		playerstats = new HashMap<Player, PlayerStats>();
		playerstats.put(Player.BLUE, new PlayerStats());
		playerstats.put(Player.RED, new PlayerStats());
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		} else if (other == null || this.getClass() != other.getClass()) {
			return false;
		} else {
			World otherWorld = (World) other;
			return (rows == otherWorld.rows && cols == otherWorld.cols);
		}
	}

	@Override
	public String toString() {
		return "[" + "Rows: " + rows + ", " + "Columns: " + cols + "]";
	}

	@Override
	public int hashCode() {
		return rows * 17 + cols * 23;
	}

	/*
	 * Testmethod
	 */
	public Map<Position, Territory> getTerritories() {
		return territories;
	}
}
