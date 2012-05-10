package riskyspace.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import riskyspace.logic.MapGenerator;
import riskyspace.model.building.Ranked;

public class World implements Serializable {
	private int rows = 0;
	private int cols = 0;
	private Map<Position, Territory> territories = null;
	private Map<Player, PlayerStats> playerstats = null;
	private Map<Player, BuildQueue> buildqueue = null;

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
		/*
		 * Support for more players
		 */
		playerstats.put(Player.BLUE, new PlayerStats());
		playerstats.put(Player.RED, new PlayerStats());
		
		buildqueue = new HashMap<Player, BuildQueue>();
		
		buildqueue.put(Player.BLUE, new BuildQueue(5));
		buildqueue.put(Player.RED, new BuildQueue(5));
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
	
	public void addToBuildQueue(BuildAble buildAble, Player player, Position position) {
		buildqueue.get(player).add(buildAble, position);
		updatePlayerStats(player);
	}
	
	/**
	 * Removes all BuildAbles from one position and refunds the Resources used
	 * @param player The owner of the queue to remove from
	 * @param position The position to remove
	 */
	public void removeBuildQueue(Player player, Position position){
		List<BuildAble> items = buildqueue.get(player).clear(position);	
		playerstats.get(player).refund(items);
		updatePlayerStats(player);
	}
	
	public void processBuildQueue(Player player) {
		if (playerstats.get(player).getSupply().fieldSupply() + buildqueue.get(player).queuedSupply(true) > playerstats.get(player).getSupply().getMax()) {
			buildqueue.get(player).clearSupply(true);
		}
		Map<Position, BuildAble> itemsToBuild = buildqueue.get(player).processQueue();
		for (Position pos : itemsToBuild.keySet()) {
			if (itemsToBuild.get(pos) instanceof ShipType) {
				getTerritory(pos).addFleet(new Fleet(new Ship((ShipType) itemsToBuild.get(pos)), getTerritory(pos).getColony().getOwner()));
			} else if (itemsToBuild.get(pos) instanceof Ranked) {
				((Ranked) itemsToBuild.get(pos)).upgrade();
			}
		}
	}
	
	public void resetAllQueues(Player player) {
		buildqueue.get(player).clearAll();
	}
	
	public List<Position> getContentPositions(){
		return territoriesWithContent();
	}
	
	public int getCols() {
		return cols;
	}
	
	public PlayerStats getStats(Player player) {
		return playerstats.get(player).getImmutableStats();
	}
	
	public void giveIncome(Player player) {
		playerstats.get(player).gainNewResources();
	}
	
	public boolean purchase(Player player, BuildAble buildAble) {
		return playerstats.get(player).purchase(buildAble);
	}
	
	public boolean canAfford(Player currentPlayer, BuildAble buildAble) {
		return playerstats.get(currentPlayer).canAfford(buildAble);
	}
	
	public void setIncome(Player player, Resource type, int amount) {
		playerstats.get(player).setIncome(type, amount);
	}
	
	public void updatePlayerStats(Player player) {
		int numberOfColonies = 0;
		int supply = 0;
		for (Position pos : this.getContentPositions()) {
			if (getTerritory(pos).hasColony() && getTerritory(pos).getColony().getOwner() == player) {
				numberOfColonies++;
			}
			if (getTerritory(pos).hasFleet()) {
				for (Fleet fleet : getTerritory(pos).getFleets()) {
					if (fleet.getOwner() == player) {
						supply += fleet.supplyCost();
					}
				}
			}
		}
		playerstats.get(player).update(numberOfColonies, supply, buildqueue.get(player).queuedSupply(false));
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