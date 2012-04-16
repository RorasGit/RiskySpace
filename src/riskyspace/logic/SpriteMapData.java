package riskyspace.logic;

import java.util.ArrayList;
import java.util.List;

import riskyspace.logic.data.*;
import riskyspace.model.*;

public class SpriteMapData {
	private static World world;
	private static ViewEventController vec;

	private List<PlanetData> planetData = new ArrayList<PlanetData>();
	private List<ColonizerData> colonizerData = new ArrayList<ColonizerData>();
	private List<FleetData> fleetData = new ArrayList<FleetData>();
	private List<ColonyData> colonyData = new ArrayList<ColonyData>();

	public static void init(World world, ViewEventController vec) {
		SpriteMapData.world = world;
		SpriteMapData.vec = vec;
	}

	public void refreshData() {
		planetData.clear();
		colonizerData.clear();
		fleetData.clear();
		colonyData.clear();
		for (Position pos : world.getContentPositions()) {
			Territory terr = world.getTerritory(pos);

			if (terr.hasPlanet()) {
				planetData.add(new PlanetData(pos, null, terr.getPlanet().getType()));
				if (terr.hasColony()) {
					colonyData.add(new ColonyData(pos, terr.controlledBy()));
				}
			}
			if (terr.hasColonizer()) {
				colonizerData.add(new ColonizerData(pos, terr.controlledBy()));
			}
			if (terr.hasFleet()) {
				if (terr.getFleetsFlagships() != ShipType.COLONIZER) {
					fleetData.add(new FleetData(pos, terr.controlledBy(), terr.getFleetsFlagships()));
				}
			}
		}

	}

	public Position[][] getPaths(Player player) {
		return vec.getPaths(player);
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
}
