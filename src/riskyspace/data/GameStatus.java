package riskyspace.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import riskyspace.model.Player;
import riskyspace.model.World;

public class GameStatus implements Serializable {
	
	static File oldSave = new File("c:\\old_saved.txt");
	static File newSave = new File("c:\\new_saved.txt");
	

	
	public static void saveGame(World world, Player currentPlayer, int turn ) {
		
		try {
			
			FileOutputStream fos = new FileOutputStream(newSave);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			oos.writeObject(world);
			
			oos.close();
			
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
		
	}
	
	public static void main(String[] args) {
		saveGame(new World(), Player.BLUE, 0);
	}

}
