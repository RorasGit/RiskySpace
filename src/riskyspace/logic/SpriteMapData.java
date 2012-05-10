package riskyspace.logic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import riskyspace.GameManager;
import riskyspace.logic.data.ColonizerData;
import riskyspace.logic.data.ColonyData;
import riskyspace.logic.data.FleetData;
import riskyspace.logic.data.PlanetData;
import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.ShipType;
import riskyspace.model.Territory;
import riskyspace.model.World;

public class SpriteMapData implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6477313136946984127L;

	private static World world;
	
	private List<PlanetData> planetData = new ArrayList<PlanetData>();
	private List<ColonizerData> colonizerData = new ArrayList<ColonizerData>();
	private List<FleetData> fleetData = new ArrayList<FleetData>();
	private List<ColonyData> colonyData = new ArrayList<ColonyData>();
	private Map<Position, Integer> fleetSize = new HashMap<Position, Integer>();
	private Map<Position, Integer> colonizerAmount = new HashMap<Position, Integer>();
	private Player player;
	
	private SpriteMapData() {}
	
	public static void init(World world) {
		SpriteMapData.world = world;
	}
	
	/**
	 * Create Sprite Data for a Player. This data is used by SpriteMap to draw contents of the game.
	 * @param player The Player that data is requested for.
	 * @return SpriteMapData for a Player.
	 */
	public static SpriteMapData getData(Player player) { //Player for sight and paths
		SpriteMapData data = new SpriteMapData();
		data.player = player;
		data.planetData.clear();
		data.colonizerData.clear();
		data.fleetData.clear();
		data.colonyData.clear();
		for (Position pos : world.getContentPositions()) {
			Territory terr = world.getTerritory(pos);

			if (terr.hasPlanet()) {
				data.planetData.add(new PlanetData(pos, null, terr.getPlanet().getType()));
				if (terr.hasColony()) {
					data.colonyData.add(new ColonyData(pos, terr.controlledBy()));
				}
			}
			if (terr.hasColonizer()) {
				data.colonizerData.add(new ColonizerData(pos, terr.controlledBy()));
				data.colonizerAmount.put(pos, world.getTerritory(pos).shipCount(ShipType.COLONIZER));
			}
			if (terr.hasFleet()) {
				if (terr.getFleetsFlagships() != ShipType.COLONIZER) {
					data.fleetData.add(new FleetData(pos, terr.controlledBy(), terr.getFleetsFlagships()));
				}
				int size = 0;
				for(Fleet fleet : world.getTerritory(pos).getFleets()) {
					size += fleet.fleetSize();
				}
				size = size - world.getTerritory(pos).shipCount(ShipType.COLONIZER);
				data.fleetSize.put(pos, size);
			}
		}
		return data;
	}

	public Position[][] getPaths() {
		return GameManager.INSTANCE.getPaths(player);
	}

	public List<PlanetData> getPlanetData() {
		return planetData;
	}

	public List<FleetData> getFleetData() {
		return fleetData;
	}

	public List<ColonizerData> getColonizerData() {
		return colonizerData;
	}

	public List<ColonyData> getColonyData() {
		return colonyData;
	}

	public int getFleetSize(Position position) {
		return fleetSize.get(position);
	}
	
	public int getColonizerAmount(Position position) {
		return colonizerAmount.get(position);
	}
}