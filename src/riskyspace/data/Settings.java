package riskyspace.data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Settings {

	public static final String IP = "ip=";
	public static final String MUSIC_ENABLED = "music_enabled=";
	
	private static boolean musicOn = true;
	private static String lastIp = "";
	
	public static void loadSetting(String loadFile) throws IOException {
		if (!new File(loadFile).createNewFile()) {
			try {
				BufferedReader buffReader = new BufferedReader(new FileReader(loadFile));
				String line;
				while ((line = buffReader.readLine()) != null) {
					setProperty(line);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void saveProperties() {
		try {
			File settingsFile = new File(GameDataHandler.getSaveFolder() + File.separator);
			settingsFile.delete();
			settingsFile.createNewFile();
			FileWriter fw = new FileWriter(settingsFile);
			fw.write(IP + lastIp + "\n");
			fw.write(MUSIC_ENABLED + musicOn);
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void setProperty(String property) {
		if (property.contains(IP)) {
			lastIp = property.substring(IP.length());
		} else if (property.contains(MUSIC_ENABLED)) {
			musicOn = Boolean.parseBoolean(property.substring(MUSIC_ENABLED.length()));
		}
	}
	
	public static String getLastIP() {
		return lastIp;
	}
	
	public static boolean isMusicOn() {
		return musicOn;
	}
}