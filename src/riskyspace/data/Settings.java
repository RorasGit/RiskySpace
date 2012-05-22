package riskyspace.data;

public class Settings {

	private int musicVolume;
	private int effectsVolume;
	
	private String IP;
	
	public void setIP(String s) {
		IP = s;
	}
	
	public String getIP() {
		return IP;
	}
	
	public int getMusicVolume() {
		return musicVolume;
	}
	
	public void setMusicVolume(int i) {
		musicVolume = i;
	}
	
	public int getEffectsVolume() {
		return effectsVolume;
	}
	
	public void setEffectsVolume(int i) {
		effectsVolume = i;
	}
}
