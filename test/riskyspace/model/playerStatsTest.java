package riskyspace.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class playerStatsTest {

	@Test
	public void testGainNewResources() {
		/*
		 * Default income is 50. If we set it to 100, resources should reach 250
		 * after one round's worth of income.
		 */
		PlayerStats ps = new PlayerStats();
		
		ps.setIncome(Resource.METAL, 100);
		
		ps.gainNewResources();
		
		assertTrue(ps.getResource(Resource.METAL) == 250);
	}
	
	@Test
	public void testPurchase() {
		/*
		 * gainNewResources is called when world is initiated: this simulates a first round purchase.
		 */
		PlayerStats ps = new PlayerStats();
		
		ps.gainNewResources();
		ps.purchase(ShipType.COLONIZER);
		
		assertTrue(ps.getResource(Resource.METAL) == 0);
	}
	
	@Test
	public void testPurchaseList() {
		PlayerStats ps = new PlayerStats();
		
		ps.gainNewResources();
		
		List<BuildAble> ships = new ArrayList<BuildAble>();
		ships.add(ShipType.SCOUT);
		ships.add(ShipType.SCOUT);
		ships.add(ShipType.SCOUT);
		ships.add(ShipType.SCOUT);
		
		assertTrue(ps.purchase(ships));
		
		PlayerStats ps2 = new PlayerStats();
		
		ps2.gainNewResources();
		
		List<BuildAble> ships2 = new ArrayList<BuildAble>();
		ships2.add(ShipType.DESTROYER);
		
		assertTrue(!ps2.purchase(ships2));
	}
	
	@Test
	public void testRefund() {
		PlayerStats ps = new PlayerStats();
		
		ps.gainNewResources();
		
		ps.refund(ShipType.HUNTER);
		
		assertTrue(ps.getResource(Resource.METAL) == 300 && ps.getResource(Resource.GAS) == 30);
	}

}
