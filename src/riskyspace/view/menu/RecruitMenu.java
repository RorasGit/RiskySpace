package riskyspace.view.menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

import riskyspace.model.Colony;
import riskyspace.model.Player;
import riskyspace.services.Event;
import riskyspace.services.EventBus;
import riskyspace.services.EventHandler;
import riskyspace.view.Button;
import riskyspace.view.Clickable;

public class RecruitMenu implements IMenu, Clickable, EventHandler {
	
	private boolean enabled;
	
	private String colonyName = null;
	
	private Color ownerColor = null;
	
	private int x, y;
	private int menuHeight = 0;
	private int menuWidth = 0;
	private int margin = 30;
	
	private Image background = null;
	private Image colonyPicture = null;
	
	private Button buildScoutButton = null;
	private Button buildHunterButton = null;
	private Button buildDestroyerButton = null;
	private Button buildColonizerButton = null;
	
	/*
	 * Images
	 */
	private Image colonyBlue = null;
	private Image colonyRed = null;
	
	public RecruitMenu(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		menuHeight = height;
		menuWidth = width;
		background = Toolkit.getDefaultToolkit().getImage("res/menu/menubackground.png")
				.getScaledInstance(menuWidth, menuHeight-40, Image.SCALE_DEFAULT);
		colonyBlue = Toolkit.getDefaultToolkit().getImage("res/menu/city_blue.png").
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT);
		colonyRed = Toolkit.getDefaultToolkit().getImage("res/menu/city_red.png").
				getScaledInstance(menuWidth - 2*margin, ((menuWidth - 2*margin)*3)/4, Image.SCALE_DEFAULT);
		
		buildScoutButton = new Button(x + margin, menuHeight - (2*menuWidth), 90, 90);
		buildScoutButton.setImage("res/icons/ships/scoutbutton.png");
		
		buildHunterButton = new Button(x + width - (margin+90), menuHeight - (2*menuWidth), 90, 90);
		buildHunterButton.setImage("res/icons/ships/hunterButton.png");
		
		buildDestroyerButton = new Button(x + margin , menuHeight - (2*menuWidth) + 90 + margin/2, 90, 90);
		buildDestroyerButton.setImage("res/icons/ships/destroyerButton.png");
		
		buildColonizerButton = new Button(x + width - (margin+90), menuHeight - (2*menuWidth) + 90 + margin/2, 90, 90);
		buildColonizerButton.setImage("res/icons/ships/colonizerButton.png");
		
		EventBus.INSTANCE.addHandler(this);
		
	}
	
	public void setColony(Colony colony) {
		colonyName = colony.getName();
		ownerColor = colony.getOwner() == Player.BLUE ? Color.BLUE : Color.RED;
		colonyPicture = colony.getOwner() == Player.BLUE ? colonyBlue : colonyRed;
	}

	@Override
	public void performEvent(Event evt) {
		if (evt.getTag() == Event.EventTag.SHOW_RECRUITMENU) {
			if (evt.getObjectValue() instanceof Colony) {
				setColony((Colony) evt.getObjectValue());
				setVisible(true);
			}
		} else if (evt.getTag() == Event.EventTag.HIDE_MENU) {
				setVisible(false);
		}
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
			if (buildScoutButton.mousePressed(p)) {
				Event evt = new Event(Event.EventTag.BUILD_SCOUT, null);
				EventBus.INSTANCE.publish(evt);
				return true;
			} else if (buildHunterButton.mousePressed(p)) {
				Event evt = new Event(Event.EventTag.BUILD_HUNTER, null);
				EventBus.INSTANCE.publish(evt);
				return true;
			} else if (buildDestroyerButton.mousePressed(p)) {
				Event evt = new Event(Event.EventTag.BUILD_DESTROYER, null);
				EventBus.INSTANCE.publish(evt);
				return true;
			} else if (buildColonizerButton.mousePressed(p)) {
				Event evt = new Event(Event.EventTag.BUILD_COLONIZER, null);
				EventBus.INSTANCE.publish(evt);
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
			if (buildScoutButton.mouseReleased(p)) {return true;}
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
			buildScoutButton.draw(g);
			buildHunterButton.draw(g);
			buildDestroyerButton.draw(g);
			buildColonizerButton.draw(g);
		}
	}
	
	private void drawColonyName(Graphics g) {
		g.setColor(ownerColor);
		Font saveFont = g.getFont();
		g.setFont(new Font("Monotype", Font.BOLD, 38));
		int textX = x - (g.getFontMetrics().stringWidth(colonyName) / 2) + (menuWidth / 2);
		int textY = y + (g.getFontMetrics().getHeight() / 2) + (2*margin + colonyPicture.getHeight(null));
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
}
