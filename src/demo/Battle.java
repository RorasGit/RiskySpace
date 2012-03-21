package demo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Battle {
	
	// initiatives is temporary solution for demo, this probably belongs elsewhere and
	// should be possible to change (or change itself according to ships initiative in fleets)
	private int initiatives = 5;
	
	private int player1Targets = 0;
	private int player2Targets = 0;
	
	public Battle () {
		;
	}
	
	public void doBattle(Territory territory) {
		List<Fleet> player1 = new ArrayList<Fleet>();
		List<Fleet> player2 = new ArrayList<Fleet>();
		
		player1.add(territory.getFleets().get(0));
		player1Targets = territory.getFleets().get(0).fleetSize();
		for (int i = 1; i < territory.getFleets().size(); i++) {
			if (territory.getFleets().get(i).getOwner() != player1.get(0).getOwner()) {
				player2.add(territory.getFleet(i));
				player2Targets = player2Targets + territory.getFleet(i).fleetSize();
			} else {
				player1.add(territory.getFleet(i));
				player1Targets = player1Targets + territory.getFleet(i).fleetSize();
			}
		}
		
		List<Integer> player1AttackIndex = null;
		List<Integer> player2AttackIndex = null;
		for (int i = initiatives; i >= 0; i--) {
			for (int j = 0; j < mergeAttacks(player1, i).size(); j++) {
				player1AttackIndex.add((int) (Math.random() * player2Targets + 1));
			}
			for (int j = 0; j < mergeAttacks(player2, i).size(); j++) {
				player2AttackIndex.add((int) (Math.random() * player1Targets + 1));
			}
			
		}


		territory.controlledBy(
				//winner of the battle!
				);
	}
	
	private List<Integer> mergeAttacks(List<Fleet> playerFleets, int initiative) {
		List<Integer> playerAttacks = new ArrayList<Integer>();
		for (int fleetIndex = 0; fleetIndex < playerFleets.size(); fleetIndex++) {
			for (int j = 0; j < playerFleets.get(fleetIndex).getAttacks(initiative).size(); j++) {
				playerAttacks.add(playerFleets.get(fleetIndex).getAttacks(initiative).get(j));
			}
		}
		return playerAttacks;
	}

	private class BattleGroup {
		List<Fleet> fleets = null;
		Colony colony = null;
		
		BattleGroup(List<Fleet> fleets, Colony colony) {
			this.fleets = fleets;
			this.colony = colony;
		}
		
		void takeDamage(List<Integer> attacks, List<Integer> targetIndexes) {
			/*
			 * Each fleet will have a list of attacks and targetIndexes
			 */
			Map<Fleet, List<Integer>> fleetTargetIndexes = new HashMap<Fleet, List<Integer>>();
			Map<Fleet, List<Integer>> fleetAttacks = new HashMap<Fleet, List<Integer>>();
			
			/*
			 * 
			 */
			for (int i = 0; i < fleets.size(); i++) {
				fleetTargetIndexes.put(fleets.get(i), new ArrayList<Integer>());
				fleetAttacks.put(fleets.get(i), new ArrayList<Integer>());
			}
			
			for (int i = 0; i < targetIndexes.size(); i++) {
				if (colony != null && targetIndexes.get(i) == numberOfUnits()-1) {
					if (colony.takeDamage(attacks.get(i))) {
						colony = null;
					}
				} else {
					int indexMod = 0;
					for (int j = 0; j < fleets.size(); j++) {
						/* BG: [0,1,2,3,4]  length == 5
						 * F1: [0,1,2]		length == 3
						 * F2: [0,1]		length == 2
						 * [0,1,2] => [0,1,2]
						 * [3,4]   => [0,1]
						 */ 
						 if (targetIndexes.get(i) < fleets.get(j).fleetSize() - indexMod) {
							 /*
							  * Split the data from the large list into the hit fleets
							  * Lists.
							  */
							 fleetTargetIndexes.get(fleets.get(j)).add(targetIndexes.get(i));
							 fleetAttacks.get(fleets.get(j)).add(attacks.get(i));
							 /*
							  * We found the hit fleet, now break for the next targetIndex.
							  */
							 break;
						 } else {
						 	indexMod += fleets.get(j).fleetSize();
						 }
					}
				}
			}
			/*
			 * Loop through all fleets and have them take damage for
			 * every hit on that fleet.
			 */
			for (Fleet fleet : fleets) {
				/*
				 * Tell each fleet to take damage earlier set to that fleet.
				 */
				fleet.takeDamage(fleetAttacks.get(fleet), fleetTargetIndexes.get(fleet));
			}
		}
		
		int numberOfUnits() {
			int units = 0;
			for (int i = 0; i < fleets.size(); i++) {
				units += fleets.get(i).fleetSize();
			}
			if (colony != null) {
				units++;
			}
			return units;
		}
		
		public boolean isDefeated() {
			return numberOfUnits() == 0;
		}
	}
}
