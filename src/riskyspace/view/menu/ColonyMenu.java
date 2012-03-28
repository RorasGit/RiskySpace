package riskyspace.view.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import javax.tools.Tool;

import riskyspace.model.Colony;
import riskyspace.model.Player;
import riskyspace.services.ModelEvent;
import riskyspace.services.ModelEventBus;
import riskyspace.services.ModelEventHandler;
import riskyspace.services.ViewEvent;
import riskyspace.services.ViewEventBus;
import riskyspace.view.Button;
import riskyspace.view.Clickable;
/**
 * 
 * @author flygarn
 * Menu showing information and options for a Colony
 */
public class ColonyMenu implements IMenu, Clickable, ModelEventHandler {

	/*
	 * Strings to be printed on the menu
	 */
	private String colonyName = "Atlantis";
	
	private Color ownerColor = null;
	
	private boolean enabled;
	
	private int x, y;
	private int menuHeight = 0;
	private int menuWidth = 0;
	private int margin = 8;
	
	private Image background = null;
	private Image colonyPicture = null;
	private Button buildShip = null;
	
	/*
	 * Images
	 */
	private Image colonyBlue = null;
	private Image colonyRed = null;
	
	public ColonyMenu(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		menuHeight = height;
		menuWidth = width;
		background = Toolkit.getDefaultToolkit().getImage("res/menu/background.png")
				.getScaledInstance(menuWidth, menuHeight, Image.SCALE_DEFAULT);
		colonyBlue = Toolkit.getDefaultToolkit().getImage("res/menu/city_blue.png").
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT);
		colonyRed = Toolkit.getDefaultToolkit().getImage("res/menu/city_red.png").
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT);
		buildShip = new Button(x + margin, menuHeight - 2*menuWidth, menuWidth-2*margin, (menuWidth - 2*margin)/2);
		buildShip.setImage("res/menu/btn.jpg");
		buildShip.setText("Build Ship");
		ModelEventBus.INSTANCE.addHandler(this);
	}
	
	public void setColony(Colony colony) {
		colonyName = colony.getName();
		ownerColor = colony.getOwner() == Player.BLUE ? Color.BLUE : Color.RED;
		colonyPicture = colony.getOwner() == Player.BLUE ? colonyBlue : colonyRed;
	}
	
	@Override
	public boolean contains(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (enabled) {
			boolean xLegal = p.x >= x && p.x <= x + menuWidth;
			boolean yLegal = p.y >= y && p.y <= y + menuHeight;
			return xLegal && yLegal;
		}
		return false;
	}

	@Override
	public boolean mousePressed(Point p) {
		/*
		 * Only handle mouse event if enabled
		 */
		if (enabled) {
			if (buildShip.mousePressed(p)) {
				ViewEvent evt = new ViewEvent(ViewEvent.EventTag.BUILD_SHIP, null, null);
				ViewEventBus.INSTANCE.publish(evt);
				return true;
			}
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
		if (enabled) {
			if (buildShip.mouseReleased(p)) {return true;}
			else {
				return false;
			}
		}
		return false;
	}

	@Override
	public void draw(Graphics g) {
		/*
		 * Only draw if enabled
		 */
		if (enabled) {
			g.drawImage(background, x, y, null);
			g.drawImage(colonyPicture, x + margin, y + margin ,null);
			drawColonyName(g);
			buildShip.draw(g);
		}
	}
	
	private void drawColonyName(Graphics g) {
		g.setColor(ownerColor);
		Font saveFont = g.getFont();
		g.setFont(new Font("Monotype", Font.BOLD, 38));
		int textX = x - (g.getFontMetrics().stringWidth(colonyName) / 2) + (menuWidth / 2);
		int textY = y + (g.getFontMetrics().getHeight() / 2) + (5*margin + colonyPicture.getHeight(null));
		g.drawString(colonyName, textX, textY);
		g.setFont(saveFont);
	}

	@Override
	public void setVisible(boolean set) {
		enabled = set;
	}

	@Override
	public boolean isVisible() {
		return enabled;
	}

	@Override
	public void performEvent(ModelEvent evt) {
		// TEST EVENT (if object sent is colony or smth)
		if (evt.getTag() == ModelEvent.EventTag.SHOW_MENU) {
			if (evt.getObjectValue() instanceof Colony) {
				setColony((Colony) evt.getObjectValue());
				setVisible(true);
			}
		} else if (evt.getTag() == ModelEvent.EventTag.HIDE_MENU) {
			setVisible(false);
		}
	}
}