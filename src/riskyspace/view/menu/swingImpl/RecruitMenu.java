package riskyspace.view.menu.swingImpl;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import riskyspace.model.Colony;
import riskyspace.model.Player;
import riskyspace.model.ShipType;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;
import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.Clickable;
import riskyspace.view.View;
import riskyspace.view.menu.AbstractSideMenu;
import riskyspace.view.menu.IMenu;

public class RecruitMenu extends AbstractSideMenu {
	
	private Color ownerColor = null;

	private int margin = 30;
	
	private Image colonyPicture = null;
	
	private Button buildScoutButton = null;
	private Button buildHunterButton = null;
	private Button buildDestroyerButton = null;
	private Button buildColonizerButton = null;
	private Button backButton = null;
	
	/*
	 * Images
	 */
	private Image cityBlue = null;
	private Image cityRed = null;
	
	public RecruitMenu(int x, int y, int menuWidth, int menuHeight) {
		super(x, y, menuWidth, menuHeight);
		cityBlue = Toolkit.getDefaultToolkit().getImage("res/menu/blue/city" + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT);
		cityRed = Toolkit.getDefaultToolkit().getImage("res/menu/red/city" + View.res).
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT);
		
		buildScoutButton = new Button(x + menuWidth/2 - margin/3 - 90, 2*y + menuHeight - (2*menuWidth), 90, 90);
		buildScoutButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.BUILD_SHIP, ShipType.SCOUT);
				EventBus.INSTANCE.publish(evt);
			}
		});
		buildHunterButton = new Button(x + menuWidth/2 + margin/3, 2*y + menuHeight - (2*menuWidth), 90, 90);
		buildHunterButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.BUILD_SHIP, ShipType.HUNTER);
				EventBus.INSTANCE.publish(evt);
			}
		});
		buildDestroyerButton = new Button(x + menuWidth/2 - margin/3 - 90, 2*y + menuHeight - (2*menuWidth) + 90 + margin/2, 90, 90);
		buildDestroyerButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.BUILD_SHIP, ShipType.DESTROYER);
				EventBus.INSTANCE.publish(evt);
			}
		});
		buildColonizerButton = new Button(x + menuWidth/2 + margin/3, 2*y + menuHeight - (2*menuWidth) + 90 + margin/2, 90, 90);
		buildColonizerButton.setAction(new Action() {
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.BUILD_SHIP, ShipType.COLONIZER);
				EventBus.INSTANCE.publish(evt);
			}
		});
		backButton = new Button(x + margin, y + menuHeight - 2*(menuWidth - 2*margin)/4, menuWidth-2*margin, (menuWidth - 2*margin)/4);
		backButton.setImage("res/menu/btn.jpg");
		backButton.setText("Back");
		backButton.setAction(new Action(){
			@Override
			public void performAction() {
				Event evt = new Event(Event.EventTag.BACK, null);
				EventBus.INSTANCE.publish(evt);
			}
		});
		EventBus.INSTANCE.addHandler(this);
	}
	
	public void setColony(Colony colony) {
		setMenuName(colony.getName());
		setPlayer(colony.getOwner());
		ownerColor = colony.getOwner() == Player.BLUE ? Color.BLUE : Color.RED;
		colonyPicture = colony.getOwner() == Player.BLUE ? cityBlue : cityRed;
		String player = colony.getOwner().toString().toLowerCase();
		buildScoutButton.setImage("res/menu/" + player + "/button/scoutButton" + View.res);
		buildHunterButton.setImage("res/menu/" + player + "/button/hunterButton" + View.res);
		buildDestroyerButton.setImage("res/menu/" + player + "/button/destroyerButton" + View.res);
		buildColonizerButton.setImage("res/menu/" + player + "/button/colonizerButton" + View.res);
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.SHOW_RECRUITMENU) {
			if (evt.getObjectValue() instanceof Colony) {
				setColony((Colony) evt.getObjectValue());
				setVisible(true);
			}
		} else if (evt.getTag() == Event.EventTag.HIDE_MENU || evt.getTag() == Event.EventTag.BACK) {
				setVisible(false);
		}
	}
		
	


	@Override
	public boolean mousePressed(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (isVisible()) {
			if (buildScoutButton.mousePressed(p)) {return true;}
			else if (buildHunterButton.mousePressed(p)) {return true;}
			else if (buildDestroyerButton.mousePressed(p)) {return true;} 
			else if (buildColonizerButton.mousePressed(p)) {return true;}
			else if (backButton.mousePressed(p)) {return true;}
			if (this.contains(p)) {return true;}
			else {
				return false;
			}
		}
		return false;
	}

	@Override
	public boolean mouseReleased(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (isVisible()) {
			if (buildScoutButton.mouseReleased(p)) {return true;}
			else {
				return false;
			}
		}
		return false;
	}

	@Override
	public void draw(Graphics g) {
		super.draw(g);
		/*
		 * Only draw if enabled
		 */
		if (isVisible()) {
			g.drawImage(colonyPicture, getX() + margin, getY() + margin + 15,null);
			drawColonyName(g);
			buildScoutButton.draw(g);
			buildHunterButton.draw(g);
			buildDestroyerButton.draw(g);
			buildColonizerButton.draw(g);
			backButton.draw(g);
		}
	}
	
	private void drawColonyName(Graphics g) {
		g.setColor(ownerColor);
		Font saveFont = g.getFont();
		g.setFont(new Font("Monotype", Font.BOLD, 38));
		int textX = getX() - (g.getFontMetrics().stringWidth(getMenuName()) / 2) + (getMenuWidth() / 2);
		int textY = getY() + (g.getFontMetrics().getHeight() / 2) + (2*margin + colonyPicture.getHeight(null));
		g.drawString(getMenuName(), textX, textY);
		g.setFont(saveFont);
	}
}
