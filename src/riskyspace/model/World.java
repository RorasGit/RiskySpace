package riskyspace.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import riskyspace.logic.MapGenerator;

public class World {
	private int rows = 0;
	private int cols = 0;
	private Map<Position, Territory> territories = null;
	private Map<Player, PlayerStats> playerstats = null;

	public World(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		initPlayers();
		territories = MapGenerator.generateMap(rows, cols);
	}

	public World() {
		this(20, 20);
	}

	private void initPlayers(/*Player[] players ??*/) {
//		for players.size()
		playerstats = new HashMap<Player, PlayerStats>();
		playerstats.put(Player.BLUE, new PlayerStats());
		playerstats.put(Player.RED, new PlayerStats());
	}

	public Territory getTerritory(Position p) {
		return territories.get(p);
	}

	public int getRows() {
		return rows;
	}
	
	private List<Position> territoriesWithContent(){
		List<Position> hasContent = new ArrayList<Position>();
		for (int row = 1; row <= rows; row++) {
			for (int col = 1; col <= cols; col++) {
				if(!territories.get(new Position(row, col)).isEmpty()){
					hasContent.add(new Position(row, col));
				}
			}
		}
		return hasContent;
	}
	
	public void resetShips() {
		for (Position pos : getContentPositions()) {
			for (Fleet fleet : getTerritory(pos).getFleets()) {
				fleet.reset();
			}
		}
	}
	
	public List<Position> getContentPositions(){
		return territoriesWithContent();
	}
	
	public int getCols() {
		return cols;
	}
	
	public void giveIncome(Player player) {
		playerstats.get(player).gainNewResources();
	}
	
	public int getResources(Player player, Resource resource) {
		return playerstats.get(player).getResource(resource);
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
		return "World [" + "Rows: " + rows + ", " + "Columns: " + cols + "]";
	}

	@Override
	public int hashCode() {
		return rows * 17 + cols * 23;
	}

}