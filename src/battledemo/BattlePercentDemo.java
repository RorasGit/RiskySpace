package battledemo;

import java.util.ArrayList;
import java.util.List;

public class BattlePercentDemo {
	
	static int drawCounter = 0;
	static int p1Counter = 0;
	static int p2Counter = 0;
	
	public static void main(String[] args) {

		for (int p = 0; p < 100000; p++) {
			int scout1 = 1;
			int hunter1 = 0;

			int scout2 = 1;
			int hunter2 = 0;

			List<Ship> player1 = new ArrayList<Ship>();
			List<Ship> player2 = new ArrayList<Ship>();

			for (int i = 0; i < scout1; i++) {
				player1.add(new Scout());
			}
			for (int i = 0; i < hunter1; i++) {
				player1.add(new Hunter());
			}

			for (int i = 0; i < scout2; i++) {
				player2.add(new Scout());
			}
			for (int i = 0; i < hunter2; i++) {
				player2.add(new Hunter());
			}
			doBattle(player1, player2);
//			System.out.println(doBattle(player1, player2));
		}
		System.out.println("DRAW: " + drawCounter/1000 + "%\n"
				+ "Player 1 won: " + p1Counter/1000 + "%\n"
				+ "Player 2 won: " + p2Counter/1000 + "%");
	}

	private static String doBattle(List<Ship> player1, List<Ship> player2) {
		while (!player1.isEmpty() && !player2.isEmpty()) {
			int[] player1dmg = new int[player1.size()];
			int[] player2dmg = new int[player2.size()];

			for (int i = 0; i < player1.size(); i++) {
				player1dmg[i] = player1.get(i).fire();
			}
			for (int i = 0; i < player2.size(); i++) {
				player2dmg[i] = player2.get(i).fire();
			}
			List<Ship> remove1 = new ArrayList<Ship>();
			List<Ship> remove2 = new ArrayList<Ship>();
			for (int i = 0; i < player1dmg.length; i++) {
				int target = (int) (Math.random() * player2.size());
				if (!player2.get(target).shield(player1dmg[i])) {
					remove2.add(player2.get(target));
				}
			}
			for (int i = 0; i < player2dmg.length; i++) {
				int target = (int) (Math.random() * player1.size());
				if (!player1.get(target).shield(player2dmg[i])) {
					remove1.add(player1.get(target));
				}
			}
			player1.removeAll(remove1);
			player2.removeAll(remove2);
		}
		String returnString = null;
		if (player1.isEmpty() && player2.isEmpty()) {
			returnString = "DRAW!";
			drawCounter++;
		} else if (!player1.isEmpty()) {
			returnString = "Player 1 won!";
			p1Counter++;
		} else {
			returnString = "Player 2 won!";
			p2Counter++;
		}
		return returnString;
	}
}
