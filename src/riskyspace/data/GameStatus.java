package riskyspace.data;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Ship;
import riskyspace.model.ShipType;
import riskyspace.model.World;

public class GameStatus {
	
	static File oldSave = new File("c:\\old_saved.txt");
	static File newSave = new File("c:\\new_saved.txt");
	
	static Path pathOldSave = FileSystems.getDefault().getPath("c:\\old_saved.txt");
	static Path pathNewSave = FileSystems.getDefault().getPath("c:\\new_saved.txt");
	
	static Position pos = new Position (1, 1);

	public static void saveGame(World world, Player currentPlayer, int turn ) throws ClassNotFoundException {
		
		try {
			
			move(pathNewSave, pathOldSave);
			
			FileOutputStream fos = new FileOutputStream(newSave);
			ObjectOutputStream oos = new ObjectOutputStream(fos);

			for (int i = 0; i < 2000; i++) {
				world.getTerritory(pos).addFleet(new Fleet(new Ship(ShipType.COLONIZER), Player.BLUE));
			}
			
			oos.writeObject(world);
			oos.close();
			
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
		
	}
	
	public static void move(Path from, Path to) {
				
			try {
				Files.move(from, to, REPLACE_EXISTING);
			} catch (IOException e) {
				e.printStackTrace();
			}

	}
		
	
	public static void loadGame() throws IOException {
		
		try {
			
			FileInputStream fis = new FileInputStream(newSave);
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			World world2 = (World) ois.readObject();
			
			ois.close();
			
			System.out.println("Content positions :" + world2.getContentPositions());
		
		} catch (ClassNotFoundException  e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		saveGame(new World(), Player.BLUE, 0);

		loadGame();
	}

}
