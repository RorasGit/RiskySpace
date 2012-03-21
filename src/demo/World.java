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
		
		/*
		 * Set Planets TODO: Roras Set Starting Fleets
		 */
	}
	
	/*
	 * Check for other planets in the perimeter. 3x3 grid with current planet in the middle.
	 */
	private boolean checkNeighboringPlanets(Position pos) {
		//TODO: make it happen :P
		return true;
	}

	private void setPlanets() {
		// Object[] t = territories.values().toArray();
		Random r = new Random();
		
		/*
		 * Starting planets
		 */
		Position pos = new Position(3 + ((int) Math.random()*2), 3 + ((int) Math.random()*2));
		territories.get(pos).setPlanet(Resource.METAL);
		territories.get(pos).getPlanet().buildColony(Player.RED);
		pos = new Position(16 + ((int) Math.random()*2), 16 + ((int) Math.random()*2));
		territories.get(pos).setPlanet(Resource.METAL);
		territories.get(pos).getPlanet().buildColony(Player.BLUE);
		
		int maxPlanets = 20;
		int planetCount = 0;
		while (planetCount <= maxPlanets) {
			
			//TODO: something that randoms out a planet at a position
			Position random = new Position(r.nextInt(20),r.nextInt(20));
			if (checkNeighboringPlanets(random)) {
				//TODO: place a planet.
			}
			
			planetCount++;
		}
		
		
		//int maxPlanets = 20;
		//for (int i = 0; i < maxPlanets; i++) {
		//	((Territory) t[r.nextInt(400)]).setPlanet(i % 2 == 0 ? Resource.GAS
		//			: Resource.METAL);
		//}

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

	public World() {
		this(20, 20);
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