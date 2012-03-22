package demo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class World {
	private int rows = 0;
	private int cols = 0;
	private Map<Position, Territory> territories = null;
	private Map<Player, PlayerStats> playerstats = null;
	private List<Player> players = null;

	public World(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		players.add(Player.RED);
		players.add(Player.BLUE);
		initTerritories();
		initPlayers();
		setPlanets();
	}

	public World() {
		this(20, 20);
	}
	
	/**
	 * Check for other planets in a 3x3 grid with current position in the middle.
	 * @return return true if no planets are present.
	 */
	private boolean checkNeighboringPlanets(Position pos) {
		for (int i=-1 ; i<=1 ; i++) {
			for (int j=-1; j<=1 ; j++) {
				Position p = new Position(pos.getRow()+i,pos.getCol()+j);
				if (territories.get(p).hasPlanet()) {
					return false;
				}
			}
		}
		return true;
	}

	private void setPlanets() {
		/*
		 * Starting planets
		 */
		Position pos = new Position(3 + ((int) (Math.random()*2)), 3 + ((int) (Math.random()*2)));
		territories.get(pos).setPlanet(Resource.METAL);
		territories.get(pos).getPlanet().buildColony(Player.RED);
		pos = new Position((rows - 4) + ((int) (Math.random()*2)), (cols - 4) + ((int) (Math.random()*2)));
		territories.get(pos).setPlanet(Resource.METAL);
		territories.get(pos).getPlanet().buildColony(Player.BLUE);
		
		int maxPlanets = 20;
		int planetCount = 0;
		int resourceIntervall = 1;
		while (planetCount < maxPlanets) {
			Position random = new Position((int)(Math.random()*(rows+1)),(int)(Math.random()*(cols+1)));
			if (checkNeighboringPlanets(random)) {
				territories.get(random).setPlanet(((resourceIntervall % 3) != 0? Resource.METAL : Resource.GAS));
				resourceIntervall++;
				planetCount++;
			} else {
				// else do nothing
			}
		}

	}

	private void initTerritories() {
		territories = new HashMap<Position, Territory>();
		for (int row = 0; row < rows; row++) {
			for (int col = 0; col < cols; col++) {
				territories.put(new Position(row, col), new Territory());
			}
		}
	}

	private void initPlayers() {
		playerstats = new HashMap<Player, PlayerStats>();
		playerstats.put(Player.BLUE, new PlayerStats());
		playerstats.put(Player.RED, new PlayerStats());
	}

	public static void main(String[] args) {
		new World();
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
		return rows*17 + cols*23;
	}
}
