package riskyspace;

import riskyspace.logic.SpriteMapData;
import riskyspace.model.World;
import riskyspace.view.View;
import riskyspace.view.ViewFactory;

public class Demo {
	
	private View mainView = null;
	
	public static void main(String[] args) {
		new Demo();
	}
	
	public Demo () {
		World world = new World(20, 20, 2);
		SpriteMapData.init(world);
		GameManager.INSTANCE.init(world, 2);
		mainView = ViewFactory.getView(ViewFactory.SWING_IMPL, world.getRows(), world.getCols());
		mainView.setViewer(GameManager.INSTANCE.getCurrentPlayer());
		GameManager.INSTANCE.start();
		mainView.setVisible(true);
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
