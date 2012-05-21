package riskyspace;

import riskyspace.view.swingImpl.LobbyView;

public class LobbyDemo {
	
	private LobbyView mainView = null;
	
	public static void main(String[] args) {
		new LobbyDemo();
	}
	
	public LobbyDemo() {
		mainView = new LobbyView();
		
		while(true) {
			mainView.draw();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	

}
