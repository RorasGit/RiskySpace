package riskyspace.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import riskyspace.services.Event;
import riskyspace.services.EventText;

public class BuildQueue implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4795409046393892799L;
	private Map<Position,ArrayList<QueueItem>> colonyQueue = null;
	private int queueMaxSize = 0;

	public BuildQueue(int queueMaxSize) {
		this.queueMaxSize = queueMaxSize;
		colonyQueue = new HashMap<Position,ArrayList<QueueItem>>();
	}
	
	/**
	 * A class for describing queued items.
	 * This class is needed for assigning queued items a buildtime that can be reduced.
	 * @author rapp
	 *
	 */
	
	/**
	 * Adds the BuildAble item to the build queue at the provided position.
	 * @param buildAble - the BuildAble object that you wish to add.
	 * @param pos - the position of the colony whose build queue you want to interact with.
	 * @return returns true if the item was successfully added to the queue, false if queue is full.
	 */
	public boolean add(BuildAble buildAble, Position pos) {
		if (!colonyQueue.containsKey(pos)) {
			colonyQueue.put(pos, new ArrayList<QueueItem>());
		}

		if (!hasQueueSpace(1, pos)) {
			return false;
		}
		colonyQueue.get(pos).add(new QueueItem(buildAble));
		String announcement = buildAble + " has been added to the build queue!";
		EventText et = new EventText(announcement.substring(0, 1).toUpperCase() + announcement.substring(1).toLowerCase(), pos);
		Event event = new Event(Event.EventTag.EVENT_TEXT, et);
//		EventBus.INSTANCE.publish(event); TODO: Ignore evtText atm
		return true;
	}
	
	/**
	 * Adds all the BuildAble items in the provided list to a specific build queue if there is capacity to do so.
	 * @param buildAbles - a list containing the BuildAbles you want added to the build queue.
	 * @param pos - the position of the colony whose build queue you want to interact with.
	 * @return returns true if all the items in the list were successfully added to the queue, false if there was not enough capacity.
	 */
	public boolean add(List<BuildAble> buildAbles, Position pos) {
		if (!colonyQueue.containsKey(pos)) {
			colonyQueue.put(pos, new ArrayList<QueueItem>());
		}
		if (!hasQueueSpace(buildAbles.size(), pos)) {
			return false;
		}
		for (BuildAble ba : buildAbles) {
			add(ba, pos);
		}
		return true;
	}
	/**
	 * Processes the build queue simulating one game round.
	 * @return returns a list containing the BuildAbles that are to be build this round.
	 */
	public Map<Position, BuildAble> processQueue() {
		Map<Position, BuildAble> finishedBuilding = new HashMap<Position, BuildAble>();
		for (Position pos : colonyQueue.keySet()) {
			if (!colonyQueue.get(pos).isEmpty()) {
				if (colonyQueue.get(pos).get(0).getBuildTime() == 1) {
					finishedBuilding.put(pos, removeFirst(pos));
				} else {
					colonyQueue.get(pos).get(0).subtractBuildTime(1);
				}
			}
		}
		return finishedBuilding;
	}
	
	/**
	 * Clears the build queue at a specified position.
	 * @param pos - the position of the colony whose build queue you want to interact with.
	 * @return returns a list containing the BuildAbles removed.
	 */
	public List<BuildAble> clear(Position pos) {
		List<BuildAble> removedItems = new ArrayList<BuildAble>();
		if (colonyQueue.containsKey(pos)) {
			while (!colonyQueue.get(pos).isEmpty()) {
					removedItems.add(removeFirst(pos));
			}
		}
		return removedItems;
	}
	
	/**
	 * Clears all the build queues for all the player's colonies.
	 * @return returns a list containing the BuildAbles removed.
	 */
	public List<BuildAble> clearAll() {
		List <BuildAble> removedItems = new ArrayList<BuildAble>();
		for (Position pos : colonyQueue.keySet()) {
			for (BuildAble ba : clear(pos)) {
				removedItems.add(ba);
			}
		}
		return removedItems;
	}
	
	/**
	 * Clears every BuildAble that occupies supply from the build queue.
	 * @param saveFirst - if true the first BuildAble objects in queues will not be removed, even if they cost supply.
	 * @return returns a list containing the BuildAbles removed.
	 */
	public List<BuildAble> clearSupply(boolean saveFirst) {
		List <BuildAble> returnItems = new ArrayList<BuildAble>();
		List <QueueItem> removeItems = new ArrayList<QueueItem>();
		for (Position pos : colonyQueue.keySet()) {
			for (QueueItem queueItem : colonyQueue.get(pos)) {
				if (queueItem.getItem().getSupplyCost() > 0) {
					if (colonyQueue.get(pos).indexOf(queueItem) > 0 || !(saveFirst)) {
						returnItems.add(queueItem.getItem());
						removeItems.add(queueItem);
					}
				}
			}
			colonyQueue.get(pos).removeAll(removeItems);
		}
		return returnItems;
	}
	
	/**
	 * Removes a specific BuildAble item from the build queue at a specified index.
	 * @param pos - the position of the colony at which the build queue is found.
	 * @param index - the index in the build queue of the item that you wish to remove.
	 * @return returns the BuildAble item that has been removed.
	 */
	public BuildAble remove(Position pos, int index) {
		QueueItem removed = colonyQueue.get(pos).remove(index);
		return removed.getItem();
	}
	
	/**
	 * Removes the first BuildAble object from the build queue at specified position.
	 * @param pos - the position of the colony at which the build queue is found.
	 * @return returns the BuildAble item that has been removed.
	 */
	public BuildAble removeFirst(Position pos) {
		return remove(pos, 0);
	}
	
	/**
	 * Removes the last BuildAble object from the build queue at specified position.
	 * @param pos- the position of the colony at which the build queue is found.
	 * @return returns the BuildAble item that has been removed.
	 */
	public BuildAble removeLast(Position pos) {
		return remove(pos, queueSize(pos));
	}
	
	/**
	 * Tells you the amount of items queued at all colonies.
	 * @return returns the number of elements currently in the build queue.
	 */
	public int queueSize() {
		int queuedItems = 0;
		for (Position pos : colonyQueue.keySet()) {
			queuedItems += queueSize(pos);
		}
		return queuedItems;
	}
	
	/**
	 * Tells you the size of the queue at a given position.
	 * @param pos - the position of the colony at which the build queue is found.
	 * @return returns the number of elements in the build queue.
	 */
	public int queueSize(Position pos) {
		if (!colonyQueue.containsKey(pos)) {
			colonyQueue.put(pos, new ArrayList<QueueItem>());
		}
		return colonyQueue.get(pos).size();
	}
	
	/**
	 * Returns the amount of supply used by the buildqueue.
	 * @param currentlyBuilding Return only supply used by the items currently building.
	 * @return Supply amount used by this BuildQueue
	 */
	public int queuedSupply(boolean currentlyBuilding) {
		int supply = 0;
		for (Position pos : activePositions()) {
			for (QueueItem queueItem : colonyQueue.get(pos)) {
				if (queueItem.getItem().getSupplyCost() > 0) {
					if (currentlyBuilding && colonyQueue.get(pos).indexOf(queueItem) == 0) {
						supply += queueItem.getItem().getSupplyCost();
					} else {
						supply += queueItem.getItem().getSupplyCost();
					}
				}
			}
		}
		return supply;
	}
	
	/**
	 * Peek at all the build queues.
	 * @return returns a Map containing the non-empty build queues from all colonies.
	 */
	// TODO
	// QI.getItem() is mutable! Need to implement toClone() or similar in BuildAble interface.
	public Map<Position, List<BuildAble>> peek() {
		Map<Position, List<BuildAble>> copiedQueues = new HashMap<Position, List<BuildAble>>();
		for (Position pos : colonyQueue.keySet()) {
			if (!colonyQueue.get(pos).isEmpty()) {
				copiedQueues.put(pos, peek(pos));
			}
		}
		return copiedQueues;
	}

	public List<BuildAble> peek(Position pos) {
		List<BuildAble> copiedList = new ArrayList<BuildAble>();
		if (!colonyQueue.get(pos).isEmpty()) {
			for (QueueItem QI : colonyQueue.get(pos)) {
				copiedList.add(QI.getItem());
			}
		}
		return copiedList;
	}
	
	public BuildAble peek(Position pos, int index) throws IllegalArgumentException {
		if (colonyQueue.get(pos).isEmpty()) {
			throw new IllegalArgumentException("Requested position is empty");
		}
		if (queueSize(pos) < index) {
			throw new IllegalArgumentException("Requested index in build queue is empty");
		}
		return colonyQueue.get(pos).get(index).getItem();
	}
	
	public Set <Position> activePositions() {
		Set<Position> activePositions = new HashSet<Position>();
		for (Position pos : colonyQueue.keySet()) {
			if (!colonyQueue.get(pos).isEmpty()) {
				activePositions.add(pos);
			}
		}
		return activePositions;
	}
	
	public int numberOfActivePositions() {
		return activePositions().size();
	}

	/**
	 * Determines if the number of elements you wish to add will fit into the queue at the given position.
	 * @param space - the number of elements to add
	 * @param pos - the position of the colony at which the build queue is found.
	 * @return returns true if the queue has sufficient space available
	 */
	public boolean hasQueueSpace(int space, Position pos) {
		if (queueSize(pos) + space  > this.queueMaxSize) {
			EventText et;
			if (space == 1) {
				et = new EventText("Build queue is full!", pos);	
			} else {
				 et = new EventText("Build queue does not have space for this!", pos);
			}
			Event event = new Event(Event.EventTag.EVENT_TEXT, et);
//			EventBus.INSTANCE.publish(event);
			return false;
		} else {
			return true;
		}
	}
}
