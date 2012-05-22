package riskyspace.view.swing.impl;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import riskyspace.view.Button;
import riskyspace.view.Clickable;

public class ScrollableList<E> implements Clickable {
		
	private List<E> savedGamesList;
		
	private List<SwingButton> displayedButtonList = new ArrayList<SwingButton>();
		
	private Runnable scroll;
	private Thread thread;
	
	private int selectedIndex;
	
	private E selectedSave = null;
	
	private SwingButton decreaseIndexButton;
	private SwingButton increaseIndexButton;
	
	private boolean mousePressed = false;
	private boolean clicked = false;
	private boolean up;
	
	private int x;
	private int y;
	private int width;
	private int height;
		
	public ScrollableList(int x, int y, int width, int height, ArrayList<E> array) {
		savedGamesList = array;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		decreaseIndexButton = new SwingButton(x, y, width, height);
		decreaseIndexButton.setImage("res/menu/lobby/scroll_up_button.png");
		
		increaseIndexButton = new SwingButton(x, y + 11*height, width, height);
		increaseIndexButton.setImage("res/menu/lobby/scroll_down_button.png");
		
		selectedIndex = 0;
		selectButtons();
		scroll = new Runnable(){
			@Override
			public void run() {
				while (true) {
					if(mousePressed || clicked) {
						clicked = false;
						if (up) {
							selectedIndex = Math.min(selectedIndex + 1, savedGamesList.size() - 1);
						} else {
							selectedIndex = Math.max(selectedIndex - 1, 0);
						}
						selectButtons();
					}
					try {
						Thread.sleep(1000/5);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		
	}
	
	private void selectButtons() {
		synchronized (displayedButtonList) {
			displayedButtonList.clear();
			int yPos = y + height;
			int buttonHeight = height;
			for (int i = selectedIndex; i < selectedIndex+10; i++) {
				if (i < savedGamesList.size()) {
					SwingButton b = new SwingButton(x + 5, yPos, width - 10, buttonHeight);
					b.setImage("res/menu/lobby/text_item.png");
					b.setText(savedGamesList.get(i).toString());
					displayedButtonList.add(b);
					yPos = yPos + buttonHeight;
				}
			}
		}
	}
	
	public void draw(Graphics g) {
		decreaseIndexButton.draw(g);
		increaseIndexButton.draw(g);
		synchronized (displayedButtonList) {
			for (SwingButton b : displayedButtonList) {
				b.draw(g);
			}
		}
	}
	
	public E getSelectedSave() {
		return selectedSave;
	}
		
	@Override
	public boolean contains(Point p) {
		boolean horizontal = p.x >= x && p.x <= x + width; 
		boolean vertical = p.y >= y && p.y <= y + height;
		return horizontal && vertical;
	}
	
	@Override
	public boolean mousePressed(Point p) {
		if (thread == null || !thread.isAlive()) {
			thread = new Thread(scroll);
			thread.start();
		}
		if (decreaseIndexButton.mousePressed(p)) {
			mousePressed = true;
			clicked = true;
			up = false;
			return true;
		} else if (increaseIndexButton.mousePressed(p)) {
			mousePressed = true;
			clicked = true;
			up = true;
			return true;
		}
		for (Button b : displayedButtonList) {
			if(b.mousePressed(p)) {
				selectedSave = savedGamesList.get(displayedButtonList.indexOf(b));
				return true;
			}
		}
		return false;
		
	}
	@Override
	public boolean mouseReleased(Point p) {
		if(decreaseIndexButton.mouseReleased(p) || !decreaseIndexButton.contains(p)) {
			mousePressed = false;
			return true;
		} else if(increaseIndexButton.mouseReleased(p) || !increaseIndexButton.contains(p)) {
			mousePressed = false;
			return true;
		}
		return false;
	}
}