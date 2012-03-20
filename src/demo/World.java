package demo;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class World {
	private int rows = 0;
	private int cols = 0;
	private Map<Position, Territory> territories = null;
	private Map<Player, PlayerStats> playerstats = null;

	public World(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		initTerritories();
		initPlayers();
		setPlanets();
		
		/*
		 * Set Planets TODO: Roras Set Starting Fleets
		 */
	}

	private void setPlanets() {
		Object[] t = territories.values().toArray();
		Random r = new Random();
		int maxPlanets = 20;
		for (int i = 0; i < maxPlanets; i++) {
			((Territory) t[r.nextInt(400)]).setPlanet(i % 2 == 0 ? Resource.GAS
					: Resource.METAL);
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

	public World() {
		this(20, 20);
	}

	public static void main(String[] args) {
		new World();
	}
}