package riskyspace.view;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScrollableList<E> implements Clickable {
		
	private List<E> savedGamesList;
		
	private List<Button> displayedButtonList = new ArrayList<Button>();
		
	private Map<Integer, Button> values = new HashMap<Integer, Button>();
	
	private int selectedIndex;
	
	private E selectedSave = null;
	
	private Button decreaseIndexButton;
	private Button increaseIndexButton;

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
		decreaseIndexButton = new Button(x, y, width, height);
		decreaseIndexButton.setImage("res/menu/lobby/scrollUpButton.png");
		decreaseIndexButton.setAction(new Action(){
			@Override
			public void performAction() {
				selectedIndex = Math.max(selectedIndex - 1, 0);
				selectButtons();
				System.out.println(selectedIndex);
			}
		});
		
		increaseIndexButton = new Button(x, y + 11*height, width, height);
		increaseIndexButton.setImage("res/menu/lobby/scrollDownButton.png");
		increaseIndexButton.setAction(new Action(){
			@Override
			public void performAction() {
				selectedIndex = Math.min(selectedIndex + 1, savedGamesList.size() - 1);
				selectButtons();
				System.out.println(selectedIndex);
			}
		});
		selectedIndex = 0;
		selectButtons();
		
	}
	
	private void selectButtons() {
		displayedButtonList.clear();
		int yPos = y + height;
		int buttonHeight = height;
		for (int i = selectedIndex; i < selectedIndex+10; i++) {
			if (i < savedGamesList.size()) {
			Button b = new Button(x, yPos, width, buttonHeight);
			b.setImage("res/menu/lobby/dropdownMenu/dropdownItem.png");
			b.setText(savedGamesList.get(i).toString());
			displayedButtonList.add(b);
			yPos = yPos + buttonHeight;
			}
		}
	}
	
	public void draw(Graphics g) {
		decreaseIndexButton.draw(g);
		increaseIndexButton.draw(g);
		for (Button b : displayedButtonList) {
			b.draw(g);
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
		if (decreaseIndexButton.mousePressed(p)) {return true;}
		if (increaseIndexButton.mousePressed(p)) {return true;}
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
		// TODO Auto-generated method stub
		return false;
	}
	
}
