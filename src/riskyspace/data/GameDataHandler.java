package riskyspace.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.Position;
import riskyspace.model.Ship;
import riskyspace.model.ShipType;
import riskyspace.model.World;

/**
 * A class for handling game data used for saving and loading games.
 * All information required to load a game is saved to file. You can load last autosaved
 * game, or you can load a game through its name as String.
 * 
 * @author rapp
 */
public class GameDataHandler {
	
	private File riskySpace = null;
	private File riskySave = null;
	private File oldSave = null;
	private File newSave = null;
	
	private final static Position pos = new Position (1, 1);
	
	/**
	 * 
	 */
	public GameDataHandler() {
		this(System.getProperty("user.home"));
	}
	
	public GameDataHandler(String saveFolder) {
		saveFolder = saveFolder + File.separator + "RiskySpace" +
				File.separator;
		riskySpace = new File(saveFolder);
		riskySave = new File(saveFolder + File.separator +
				"Save");
		
		oldSave = new File(riskySave + File.separator + "previous_autosave.rsg");
		newSave = new File(riskySave + File.separator + "last_autosave.rsg");
		
		try {
			riskySpace.mkdir();
			riskySave.mkdir();
			oldSave.createNewFile();
			newSave.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void autoSave(World world, List<Player> players, Player currentPlayer, int turn,
			String gameMode) {

		try {
			oldSave.createNewFile();
			newSave.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}	
		
		newSave.renameTo(oldSave);

		saveGame(world, players, currentPlayer, turn, gameMode, "last_autosave");
	}
	
	public void saveGame(World world, List<Player> players, Player currentPlayer, int turn,
			String gameMode, String gameName) {
	
		try {
			FileOutputStream fos = new FileOutputStream(riskySave + File.separator + gameName + ".rsg");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
		
			oos.writeObject(world);
			oos.writeObject(players);
			oos.writeObject(currentPlayer);
			oos.writeInt(turn);
			oos.writeObject(gameMode);
			
			oos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}	
	
	public void loadGame(String gameName) throws IOException {	
		try {
			FileInputStream fis = new FileInputStream(riskySave + File.separator + gameName + ".rsg");
			ObjectInputStream ois = new ObjectInputStream(fis);
			
			World world = (World) ois.readObject();
			List<Player> players = (List<Player>) ois.readObject();
			Player currentPlayer = (Player) ois.readObject();
			int turn = (Integer) ois.readInt();
			String gameMode = (String) ois.readObject();
			
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
			loadGame("last_autosave");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String[] getSavedGames() {
		File directory = riskySave;
		FilenameFilter rsgFilter = new FilenameFilter() {
			public boolean accept(File directory, String fileName) {
				return fileName.endsWith(".rsg");
			}
		};
		System.out.println(directory.getPath());
		if (directory.list(rsgFilter).length == 0) {
			return new String[] {"No saved games found in: \n" + directory.getPath()};
		} else {
			String[] gameNames = directory.list(rsgFilter);
			for (int i = 0; i < gameNames.length; i++) {
				gameNames[i] = gameNames[i].substring(0, gameNames[i].length()-4);
			}
			return gameNames;
		}
	}
	
	public String[] getGameInfo(String gameName) {
		FileInputStream fis;
		ObjectInputStream ois = null;

			try {
				fis = new FileInputStream(riskySave + File.separator + gameName + ".rsg");
				ois = new ObjectInputStream(fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			String[] gameInfo = {null, null, null};
		
			try {
				World world = (World) ois.readObject();
				List<Player> players = (List<Player>) ois.readObject();
				Player currentPlayer = (Player) ois.readObject();
				int turn = (Integer) ois.readInt();
				String gameMode = (String) ois.readObject();
				
		
				gameInfo[0] = players.toString();
				gameInfo[1] = turn+"";
				gameInfo[2] = gameMode;
			
				ois.close();				
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		return gameInfo;
	}
	
	
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		World world = new World();
		
		for (int i = 0; i < 2; i++) {
			world.getTerritory(pos).addFleet(new Fleet(new Ship(ShipType.SCOUT), Player.BLUE));
		}
		
		GameDataHandler gd = new GameDataHandler();
		
		List<Player> players = new ArrayList<Player>();
		players.add(Player.BLUE);
		players.add(Player.RED);
		
		for (int i = 0; i < gd.getSavedGames().length; i++) {
			System.out.println(gd.getSavedGames()[i]);
		}
		
		gd.autoSave(world, players, Player.BLUE, 0, "Annihilation");
		
		gd.saveGame(world, players, Player.RED, 15, "Annihilation", "game7");
		
		for (int i = 0; i < gd.getGameInfo("last_autosave").length; i++) {
			System.out.println(gd.getGameInfo("last_autosave")[i]);
		}
		
		gd.loadAutoSave();
	}
}