package riskyspace.data;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

import javax.swing.filechooser.FileNameExtensionFilter;

import com.sun.xml.internal.bind.v2.runtime.Name;

import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Ship;
import riskyspace.model.ShipType;
import riskyspace.model.World;

public class GameStatus {
	
	private Path riskySpace = null;
	private Path riskySave = null;
	private Path pathOldSave = null;
	private Path pathNewSave = null;
	
	private String saveFolder = "c:\\RiskySpace\\Save\\";
	
	File newSave = null;
	File oldSave = null;
	
	private final static Position pos = new Position (1, 1);
	
	public GameStatus() {
		
		pathOldSave = FileSystems.getDefault().getPath(saveFolder+"previous_autosave.rsg");
		pathNewSave = FileSystems.getDefault().getPath(saveFolder+"last_autosave.rsg");

		riskySpace = FileSystems.getDefault().getPath("c:\\RiskySpace");
		riskySave = FileSystems.getDefault().getPath(saveFolder);
		
		try {
			if (Files.notExists(riskySpace) && Files.notExists(riskySave)) {
				Files.createDirectory(riskySpace);
				Files.createDirectory(riskySave);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public GameStatus(String saveFolder) {
		this();
		this.saveFolder = saveFolder;
	}

	public void autoSave(World world, Player[] players, Player currentPlayer, int turn) throws IOException {

		new File(pathOldSave.toString()).createNewFile();	
		new File(pathNewSave.toString()).createNewFile();
		
		Files.copy(pathNewSave, pathOldSave, REPLACE_EXISTING);

		saveGame(world, players, currentPlayer, turn, null);
	}
	
	public void saveGame(World world, Player[] players, Player currentPlayer, int turn, String gameName) {
	
		FileOutputStream fos;
		try {
			if (gameName == null) {
				fos = new FileOutputStream(pathNewSave.toString());
			} else {
				File customSave = new File(saveFolder+ gameName + ".rsg");
				fos = new FileOutputStream(customSave);
			}
			ObjectOutputStream oos = new ObjectOutputStream(fos);
		
			oos.writeObject(world);
			oos.writeObject(players);
			oos.writeObject(currentPlayer);
			oos.writeInt(turn);
			oos.writeObject("last_autosave");
			
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
		
	
	public void loadGame(String gameName) throws IOException {	
		try {
			FileInputStream fis;
			if (gameName == null) {
				fis = new FileInputStream(pathNewSave.toString());
			} else {
				Path pathCustomSave = FileSystems.getDefault().getPath(saveFolder+ gameName + ".rsg");
				fis = new FileInputStream(pathCustomSave.toString());
			}
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			World world = (World) ois.readObject();
			Player[] players = (Player[]) ois.readObject();
			Player currentPlayer = (Player) ois.readObject();
			int turn = (Integer) ois.readInt();
			
			/* TODO
			 * Use this info to create a new game instance:
			 * new GameManager() <- needs to be implemented
			 * GameManager.init(world);  <- need new init for a world loaded from file
			 */
						
			ois.close();
		} catch (ClassNotFoundException  e) {
			System.out.println(e);
		}
	}
	
	public void loadAutoSave() {
		try {
			loadGame(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getSavedGames() {
		
		File directory = new File(saveFolder);
		FilenameFilter rsgFilter = new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				return fileName.endsWith(".rsg");
			}
		};
		if (directory.list(rsgFilter).length == 0) {
			return new String[] {"No saved games found in: \n" + saveFolder};
		} else {
			String[] gameNames = directory.list(rsgFilter);
			for (int i = 0; i < gameNames.length; i++) {
				gameNames[i] = gameNames[i].substring(0, gameNames[i].length()-4);
			}
			return gameNames;
		}
	}
	
	public String[] getGameInfo(String gameName) throws ClassNotFoundException, IOException {
		
		Path pathCustomSave = FileSystems.getDefault().getPath(saveFolder+ gameName + ".rsg");
		
		FileInputStream fis;
		ObjectInputStream ois = null;
		String[] gameInfo;


			fis = new FileInputStream(pathCustomSave.toString());
			ois = new ObjectInputStream(fis);
		
			World world = (World) ois.readObject();
			Player[] players = (Player[]) ois.readObject();
			Player currentPlayer = (Player) ois.readObject();
			int turn = (Integer) ois.readInt();


		
		return null;
	}
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		World world = new World();
		
		for (int i = 0; i < 2; i++) {
			world.getTerritory(pos).addFleet(new Fleet(new Ship(ShipType.SCOUT), Player.BLUE));
		}
		
		GameStatus gs = new GameStatus();
		
		for (int i = 0; i < gs.getSavedGames().length; i++) {
			System.out.println(gs.getSavedGames()[i]);
		}
		
		System.out.println(gs.getGameInfo("asd"));
		
		gs.autoSave(world, null, Player.BLUE, 0);
		
		gs.saveGame(world, null, Player.RED, 15, "game5");
		
		gs.loadAutoSave();
	}
}