package riskyspace;

import java.util.List;
import java.util.Map;

import riskyspace.data.Settings;
import riskyspace.logic.SpriteMapData;
import riskyspace.model.BuildAble;
import riskyspace.model.Colony;
import riskyspace.model.Fleet;
import riskyspace.model.Player;
import riskyspace.model.PlayerStats;
import riskyspace.model.Territory;
import riskyspace.model.World;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;
import riskyspace.sound.PlayList;
import riskyspace.view.View;
import riskyspace.view.ViewFactory;

public class LocalGame {
	
	private final View mainView;
	private final PlayList playList;
	private final Thread renderThread;
	
	public LocalGame(int numberOfPlayers) {
		World world = new World(20, 20, numberOfPlayers);
		SpriteMapData.init(world);
		GameManager.INSTANCE.init(world, numberOfPlayers);
		GameManager.INSTANCE.start();
		/*
		 * New Event Handlers
		 */
		new ControllerEventHandler();
		new ViewEventHandler();
		
		mainView = ViewFactory.getView(ViewFactory.OPEN_GL_IMPL, 20, 20);
		mainView.updateData(SpriteMapData.getData(GameManager.INSTANCE.getCurrentPlayer()));
		mainView.setPlayerStats(world.getStats(GameManager.INSTANCE.getCurrentPlayer()));
		mainView.setViewer(GameManager.INSTANCE.getCurrentPlayer());
		mainView.setActivePlayer(GameManager.INSTANCE.getCurrentPlayer());
		mainView.setVisible(true);

		playList = new PlayList(PlayList.STANDARD_GAME_LOOP);
		playList.start();
		renderThread = new Thread(new Runnable() {
			@Override public void run() {
				while(mainView.draw()) {
					try {
						Thread.sleep(1000/60);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		});
		renderThread.start();
	}
	
	private class ViewEventHandler implements EventHandler {

		public ViewEventHandler() {
			EventBus.CLIENT.addHandler(this);
		}
		
		@Override
		public void performEvent(Event evt) {
			if (evt.getTag() == Event.EventTag.SOUND) {
				if (Settings.isMusicOn()) {
					playList.start();
				} else {
					playList.pause();
				}
			} else if (evt.getTag() == Event.EventTag.DISCONNECT) {
				mainView.setVisible(false);
				playList.pause();
				mainView.dispose();
			} else if (evt.getTag() == Event.EventTag.SHOW_GAME_MENU) {
				mainView.showGameContextMenu();
			} else {
				Player p = GameManager.INSTANCE.getCurrentPlayer();
				GameManager.INSTANCE.handleEvent(evt, p);
			}
		}
	}

	private class ControllerEventHandler implements EventHandler {

		public ControllerEventHandler() {
			EventBus.SERVER.addHandler(this);
		}
		
		@Override
		public void performEvent(Event event) {
			if (event.getPlayer() != GameManager.INSTANCE.getCurrentPlayer() && event.getPlayer() != null) {
				return;
			}
			if (event.getTag() == Event.EventTag.STATS_CHANGED) {
				mainView.setPlayerStats((PlayerStats) event.getObjectValue());
			} else if (event.getTag() == Event.EventTag.UPDATE_SPRITEDATA) {
				mainView.updateData(SpriteMapData.getData(GameManager.INSTANCE.getCurrentPlayer()));
			} else if (event.getTag() == Event.EventTag.ACTIVE_PLAYER_CHANGED) {
				mainView.setViewer(GameManager.INSTANCE.getCurrentPlayer());
				mainView.setActivePlayer(GameManager.INSTANCE.getCurrentPlayer());
				mainView.updateData(SpriteMapData.getData(GameManager.INSTANCE.getCurrentPlayer()));
				mainView.hideMenus();
			} else if (event.getTag() == Event.EventTag.SELECTION) {
				Object selection = event.getObjectValue();
				if (selection instanceof Colony) {
					mainView.showColony((Colony) selection);
				} else if (selection instanceof Territory) {
					mainView.showPlanet((Territory) selection);
				} else if (selection instanceof Fleet) {
					mainView.showFleet((Fleet) selection);
				} else if(selection == null){
					mainView.hideMenus();
				}
			} else if(event.getTag() == Event.EventTag.BUILDQUEUE_CHANGED){
				mainView.setQueue((Map<Colony, List<BuildAble>>) event.getObjectValue());
			} else if (event.getTag() == Event.EventTag.GAME_OVER) {
				mainView.showWinnerScreen();
				mainView.hideMenus();
			}
		}
	}
}