package riskyspace.model;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class BuildQueueTest {
	
	@Test
	public void testAdd() {
		/*
		 * The queue size and active positions should change when items are added to the queue.
		 */
		BuildQueue bq = new BuildQueue(5);
		Position pos1 = new Position(1, 1);
		Position pos2 = new Position(2, 2);
		bq.add(ShipType.COLONIZER, pos1);
		bq.add(ShipType.SCOUT, pos2);
		
		assertTrue(bq.numberOfActivePositions() == 2 && bq.queueSize() == 2);
	}
	
	@Test
	public void testAddList() {
		/*
		 * Since we set our max build queue size for each colony to 5,
		 * a list longer than 5 should be rejected.
		 */
		BuildQueue bq = new BuildQueue(5);
		Position pos1 = new Position(1, 1);
		
		// A list with length 4 should be accepted.
		List<BuildAble> ships = new ArrayList<BuildAble>();
		ships.add(ShipType.SCOUT);
		ships.add(ShipType.SCOUT);
		ships.add(ShipType.COLONIZER);
		ships.add(ShipType.COLONIZER);
		
		assertTrue(bq.add(ships, pos1));
		
		// Was it correctly added?
		assertTrue(bq.queueSize(pos1) == 4);
		
		/*
		 * If we now try add a list with 2 ships we should be rejected,
		 * since the build queue is limited to 5 ships.
		 */
		ships = new ArrayList<BuildAble>();
		ships.add(ShipType.HUNTER);
		ships.add(ShipType.HUNTER);
		
		assertTrue(!bq.add(ships, pos1));
		
		// The list we added previously should remain unaffected.
		
		assertTrue(bq.queueSize(pos1) == 4);
		
		// We should be able to add 1 last ship without complications
		bq.add(ShipType.DESTROYER, pos1);
		
		assertTrue(bq.queueSize(pos1) == 5);
	}

	@Test
	public void testProcessQueue() {
		/* 
		 * A Scout should be built the next round if processQueue works as intended.
		 */
		BuildQueue bq = new BuildQueue(5);
		Position pos1 = new Position(0, 0);
		Position pos2 = new Position(1, 1);
		bq.add(ShipType.SCOUT, pos1);
		bq.add(ShipType.DESTROYER, pos1);
		Map<Position, BuildAble> built = bq.processQueue();
		assertTrue(built.size() == 1 && built.get(pos1) == ShipType.SCOUT);
		
		/*
		 * Now a Destroyer should be first queued for pos1, which takes 2 rounds to build.
		 * Nothing should be built at pos1, the Destroyed at pos1 should remain queued.
		 * A Hunter should be built right away at pos2 and a new Destroyer queued behind it.
		 */
		bq.add(ShipType.HUNTER, pos2);
		bq.add(ShipType.DESTROYER, pos2);
		
		built = bq.processQueue();
		assertTrue(built.size() == 1 && built.get(pos2) == ShipType.HUNTER && bq.queueSize(pos1) == 1);
		
		/*
		 * Now the first Destroyer should be built at pos1.
		 * The second Destroyer queued at pos2 should remain queued.
		 */
		built = bq.processQueue();
		assertTrue(built.size() == 1 && built.get(pos1) == ShipType.DESTROYER && bq.queueSize(pos2) == 1);
	}

	@Test
	public void testClear() {
		/*
		 * After clearing a position it should be empty and a list containing
		 * what has been cleared from the build queue should be returned.
		 */
		BuildQueue bq = new BuildQueue(5);
		
		// This position represents the colony to be cleared.
		Position pos1 = new Position(1, 1);
		
		bq.add(ShipType.SCOUT, pos1);
		bq.add(ShipType.COLONIZER, pos1);
		
		List<BuildAble> cleared = bq.clear(pos1);
		
		assertTrue(cleared.size() == 2 && bq.queueSize(pos1) == 0 && cleared.get(1) == ShipType.COLONIZER);
	}

	@Test
	public void testClearAll() {
		/*
		 * After clearing all positions the queue size should be 0.
		 */
		BuildQueue bq = new BuildQueue(5);
		
		// These positions represent colonies with individual build queues
		Position pos1 = new Position(1, 1);
		Position pos2 = new Position(2, 2);
		Position pos3 = new Position(3, 3);
		
		List<BuildAble> ships = new ArrayList<BuildAble>();
		ships.add(ShipType.SCOUT);
		ships.add(ShipType.SCOUT);
		ships.add(ShipType.COLONIZER);
		ships.add(ShipType.COLONIZER);
		
		bq.add(ships, pos1);
		bq.add(ships, pos2);
		bq.add(ships, pos3);
		
		bq.clearAll();
		assertTrue(bq.queueSize() == 0);
	
	}

	@Test
	public void testClearSupply() {
		/*
		 * This should only clear items that occupy supply: Colonizers should
		 * remain in queue. Calling clearSupply(true) should not clear away the first
		 * item in queue even if it does occupy supply. This means the build queue should
		 * not be altered by a clearSupply(true) call if there are only Colonizers on index
		 * 2 and upwards in the queues for each colony.
		 */
		BuildQueue bq = new BuildQueue(5);
		
		Position pos1 = new Position(1, 1);
		Position pos2 = new Position(2, 2);
		Position pos3 = new Position(3, 3);
		
		List<BuildAble> ships = new ArrayList<BuildAble>();
		ships.add(ShipType.SCOUT);
		ships.add(ShipType.COLONIZER);
		
		bq.add(ships, pos1);
		bq.add(ships, pos2);
		bq.add(ships, pos3);
		
		bq.clearSupply(true);
		
		assertTrue(bq.queueSize() == 6);
		
		/*
		 * Calling clearSupply(false) should remove the scouts and the
		 * queue size should be halved (since half the ships are of type Colonizer
		 * which does not occupy supply).
		 */
		bq.clearSupply(false);
		
		assertTrue(bq.queueSize() == 3);
	}

	@Test
	public void testPeek() {
		/*
		 * Peek should return a map of everything that is currently queued
		 * at all positions.
		 */
		BuildQueue bq = new BuildQueue(5);
		
		Position pos1 = new Position(1, 1);
		Position pos2 = new Position(2, 2);
		Position pos3 = new Position(3, 3);
		
		List<BuildAble> ships = new ArrayList<BuildAble>();
		ships.add(ShipType.HUNTER);
		ships.add(ShipType.COLONIZER);
		ships.add(ShipType.SCOUT);
		
		bq.add(ships, pos1);
		bq.add(ships, pos2);
		bq.add(ships, pos3);
		
		Map<Position, List<BuildAble>> peeked = bq.peek();
		
		assertTrue(peeked.size() == bq.numberOfActivePositions());
		
		/*
		 *  A counter for the number of elements returned from peek() - will it match
		 *  the number of elements in the build queue?
		 */
		int queuedElements = 0;
		for (Position pos : peeked.keySet()) {
			queuedElements += peeked.get(pos).size();
		}
		
		assertTrue(bq.queueSize() == queuedElements);	
	}

	@Test
	public void testPeekPosition() {
		/*
		 * peek(Position pos) should let us peek at the list
		 * in the build queue representing that position.
		 */
		BuildQueue bq = new BuildQueue(5);
		
		Position pos = new Position(1, 1);
		bq.add(ShipType.HUNTER, pos);
		bq.add(ShipType.COLONIZER, pos);
		bq.add(ShipType.SCOUT, pos);
		
		assertTrue(bq.peek(pos).size() == bq.queueSize(pos));
		
		// Are they returned identical? (Items returned in the same order)
		assertTrue(bq.peek(pos).get(2) == bq.remove(pos, 2));
	}
}
