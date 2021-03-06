package riskyspace.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import riskyspace.logic.MapGenerator;
import riskyspace.model.building.Ranked;
import riskyspace.services.Event;
import riskyspace.services.EventBus;

public class World implements Serializable {
	/**
	 * Serializable id
	 */
	private static final long serialVersionUID = 6882167268368604629L;
	private int rows = 0;
	private int cols = 0;
	private Map<Position, Territory> territories = null;
	private Map<Player, PlayerStats> playerstats = null;
	private Map<Player, BuildQueue> buildqueue = null;
	
	public World(){
		//Default 20x20 map with 2 players
		this(20, 20, 2);
	}
	
	public World(int rows, int cols, int numberOfPlayers) {
		this.rows = rows;
		this.cols = cols;
		initPlayers();
		territories = MapGenerator.generateMap(rows, cols, numberOfPlayers);
	}

	private void initPlayers() {
		playerstats = new HashMap<Player, PlayerStats>();
		
		playerstats.put(Player.BLUE, new PlayerStats());
		playerstats.put(Player.RED, new PlayerStats());
		playerstats.put(Player.YELLOW, new PlayerStats());
		playerstats.put(Player.GREEN, new PlayerStats());
		
		buildqueue = new HashMap<Player, BuildQueue>();
		
		buildqueue.put(Player.BLUE, new BuildQueue(5));
		buildqueue.put(Player.RED, new BuildQueue(5));
		buildqueue.put(Player.YELLOW, new BuildQueue(5));
		buildqueue.put(Player.GREEN, new BuildQueue(5));
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
	
	public Map<Colony, List<BuildAble>> getBuildQueue(Player player){
		Map <Position, List<BuildAble>> buildQueue = buildqueue.get(player).peek();
		Map <Colony, List<BuildAble>> returnedBuildQueue = new HashMap<Colony, List<BuildAble>>();
		for (Position pos : buildQueue.keySet()) {
			returnedBuildQueue.put(this.territories.get(pos).getColony(), buildQueue.get(pos));
		}
		return returnedBuildQueue;
	}
	
	/**
	 * Reset ships owned by a Player
	 * @param player The owner of the fleets that should be repaired.
	 */
	public void resetShips(Player player) {
		for (Position pos : getContentPositions()) {
			Territory terr = getTerritory(pos);
			boolean repair = terr.hasColony() && terr.getColony().canRepair() && terr.getColony().getOwner() == player;
			for (Fleet fleet : getTerritory(pos).getFleets()) {
				if (fleet.getOwner() == player) {
					fleet.reset(repair);
				}
			}
		}
	}
	
	public boolean addToBuildQueue(BuildAble buildAble, Player player, Position position) {
		boolean added = buildqueue.get(player).add(buildAble, position);
		updatePlayerStats(player);
		Event evt = new Event(Event.EventTag.BUILDQUEUE_CHANGED, getBuildQueue(player));
		evt.setPlayer(player);
		EventBus.SERVER.publish(evt);
		return added;
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
		Event evt = new Event(Event.EventTag.BUILDQUEUE_CHANGED, getBuildQueue(player));
		evt.setPlayer(player);
		EventBus.SERVER.publish(evt);
	}
	
	public void processBuildQueue(Player player) {
		if (playerstats.get(player).getSupply().fieldSupply() + buildqueue.get(player).queuedSupply(true) > playerstats.get(player).getSupply().getMax()) {
			buildqueue.get(player).clearSupply(true);
		}
		Map<Position, BuildAble> itemsToBuild = buildqueue.get(player).processQueue();
		for (Position pos : itemsToBuild.keySet()) {
			if (itemsToBuild.get(pos) instanceof ShipType) {
				getTerritory(pos).addFleet(new Fleet(new Ship((ShipType) itemsToBuild.get(pos)), player));
			} else if (itemsToBuild.get(pos) instanceof Ranked) {
				((Ranked) itemsToBuild.get(pos)).upgrade();
				incomeChanged(player);
			}
		}
		Event evt = new Event(Event.EventTag.BUILDQUEUE_CHANGED, getBuildQueue(player));
		evt.setPlayer(player);
		if (itemsToBuild.size() > 0) {
			Event event = new Event(Event.EventTag.UPDATE_SPRITEDATA, null);
			EventBus.SERVER.publish(event);
		}
		EventBus.SERVER.publish(evt);
	}
	
	public void resetAllQueues(Player player) {
		List<BuildAble> deletedItems = buildqueue.get(player).clearAll();
		playerstats.get(player).refund(deletedItems);
		updatePlayerStats(player);
		Event evt = new Event(Event.EventTag.BUILDQUEUE_CHANGED, getBuildQueue(player));
		evt.setPlayer(player);
		EventBus.SERVER.publish(evt);
	}
	private void incomeChanged(Player affectedPlayer) {
		int metalIncome = 10;
		int gasIncome = 0;
		for (Position pos : getContentPositions()) {
			Territory terr = getTerritory(pos);
			if (terr.hasColony()) {
				if (terr.getColony().getOwner() == affectedPlayer) {
					if (terr.getPlanet().getType() == Resource.METAL) {
						metalIncome += terr.getColony().getIncome();
					} else if (terr.getPlanet().getType() == Resource.GAS) {
						gasIncome += terr.getColony().getIncome();
					}
				}
			}
		}
		setIncome(affectedPlayer, Resource.METAL, metalIncome);
		setIncome(affectedPlayer, Resource.GAS, gasIncome);
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
		Event evt = new Event(Event.EventTag.STATS_CHANGED, getStats(player));
		evt.setPlayer(player);
		EventBus.SERVER.publish(evt);
	}
	
	public boolean purchase(Player player, BuildAble buildAble) {
		if( playerstats.get(player).purchase(buildAble)){
			Event evt = new Event(Event.EventTag.STATS_CHANGED, getStats(player));
			evt.setPlayer(player);
			EventBus.SERVER.publish(evt);
			return true;
		}
		return false;
	}
	
	public boolean canAfford(Player currentPlayer, BuildAble buildAble) {
		return playerstats.get(currentPlayer).canAfford(buildAble);
	}
	
	public boolean isQueueFull(Player currentPlayer, int space, Position pos) {
		return !buildqueue.get(currentPlayer).hasQueueSpace(space, pos);
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
		Event evt = new Event(Event.EventTag.STATS_CHANGED, getStats(player));
		evt.setPlayer(player);
		EventBus.SERVER.publish(evt);
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
