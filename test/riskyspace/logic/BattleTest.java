package riskyspace.logic;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.Resource;
import riskyspace.model.Ship;
import riskyspace.model.ShipType;
import riskyspace.model.Territory;

public class BattleTest {

	@Test
	public void testDoBattle() {
		/*
		 * Battle between 2 Fleets should result in 
		 * atleast one of them being destroyed and
		 * removed from the territory.
		 */
		Territory territory = new Territory();
		List<Fleet> fleets = new ArrayList<Fleet>();
		List<Ship> blue = new ArrayList<Ship>();
		List<Ship> red = new ArrayList<Ship>();
		blue.add(new Ship(ShipType.SCOUT));
		blue.add(new Ship(ShipType.SCOUT));
		red.add(new Ship(ShipType.SCOUT));
		red.add(new Ship(ShipType.SCOUT));
		fleets.add(new Fleet(blue, Player.BLUE));
		fleets.add(new Fleet(red, Player.RED));
		territory.addFleets(fleets);
		Battle.doBattle(territory);
		assertTrue(territory.getFleets().size() < 2);
		
		/*
		 * Battle between lone Planet and Fleets should
		 * result in the fleets being destroyed on control
		 * of Territiory changed
		 */
		territory = new Territory();
		fleets = new ArrayList<Fleet>();
		blue = new ArrayList<Ship>();
		for (int i = 0; i < (int) (Math.random()*3) + 1; i++) {
			/*
			 * add 1 to 3 ships
			 */
			blue.add(new Ship(ShipType.SCOUT));
		}
		fleets.add(new Fleet(blue, Player.BLUE));
		territory.addFleets(fleets);
		
		// Add planet
		territory.setPlanet(Resource.METAL);
		territory.getPlanet().buildColony(Player.RED);
		
		Battle.doBattle(territory);
		assertTrue(!territory.hasFleet() || territory.controlledBy() == Player.BLUE);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testOnePlayerTerritory() {
		Territory territory = new Territory();
		List<Fleet> fleets = new ArrayList<Fleet>();
		fleets.add(new Fleet(new Ship(ShipType.SCOUT), Player.BLUE));
		territory.addFleets(fleets);
		Battle.doBattle(territory);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testEmptyTerritory() {
		Territory territory = new Territory();
		Battle.doBattle(territory);
	}
}