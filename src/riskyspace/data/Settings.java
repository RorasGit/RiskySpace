package riskyspace.data;

public class Settings {

	private boolean musicOn;
	
	private String IP;
	
	public void setIP(String s) {
		IP = s;
	}
	
	public String getIP() {
		return IP;
	}
	
	public void setMusicOn(boolean b) {
		musicOn = b;
	}
	
	public boolean isMusicOn() {
		return musicOn;
	}
}
