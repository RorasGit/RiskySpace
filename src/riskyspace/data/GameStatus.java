package riskyspace.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.World;

public class GameStatus implements Serializable {
	
	static File oldSave = new File("c:\\old_saved.txt");
	static File newSave = new File("c:\\new_saved.txt");
	
	static Position pos = new Position (1, 1);
	

	
	public static void saveGame(World world, Player currentPlayer, int turn ) {
		
		try {
			
			FileOutputStream fos = new FileOutputStream(newSave);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			
			World vorld = world;
			
			oos.writeObject(vorld);
			
			oos.close();
			
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
		
	}
	
	public static void move(File from, File to) {
		
		try {
			
			FileInputStream fis = new FileInputStream(from);
			
			
			
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void loadGame() throws IOException {
		
		try {
			
			FileInputStream fis = new FileInputStream(newSave);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			World world2 = (World) ois.readObject();
		
		} catch (ClassNotFoundException  e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) throws IOException {
		saveGame(new World(), Player.BLUE, 0);
		loadGame();
	}

}
