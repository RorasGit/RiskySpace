package riskyspace.logic;

import java.util.ArrayList;
import java.util.List;

import riskyspace.GameManager;
import riskyspace.logic.data.*;
import riskyspace.model.*;

public class SpriteMapData {
	private static World world;
	
	private List<PlanetData> planetData = new ArrayList<PlanetData>();
	private List<ColonizerData> colonizerData = new ArrayList<ColonizerData>();
	private List<FleetData> fleetData = new ArrayList<FleetData>();
	private List<ColonyData> colonyData = new ArrayList<ColonyData>();
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
			}
			if (terr.hasFleet()) {
				if (terr.getFleetsFlagships() != ShipType.COLONIZER) {
					data.fleetData.add(new FleetData(pos, terr.controlledBy(), terr.getFleetsFlagships()));
				}
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
		// TODO Test Method!
		int size = 0;
		for(Fleet fleet : world.getTerritory(position).getFleets()) {
			size += fleet.fleetSize();
		}
		return size - getColonizerAmount(position);
	}
	
	public int getColonizerAmount(Position position) {
		return world.getTerritory(position).shipCount(ShipType.COLONIZER);
	}
}