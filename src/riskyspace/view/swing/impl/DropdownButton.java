package riskyspace.view.swing.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Graphics;
import java.awt.Point;

import riskyspace.view.Action;
import riskyspace.view.Button;
import riskyspace.view.Clickable;

public class DropdownButton<E> implements Clickable {
	
	private List<E> itemList;
	
	private List<SwingButton> buttonList = new ArrayList<SwingButton>();
	
	private Map<SwingButton, Integer> values = new HashMap<SwingButton, Integer>();
	
	private int selectedValue;
	
	private boolean open = false;

	private int x;
	private int y;
	private int width;
	private int height;
	
	private SwingButton mainButton;
	
	public DropdownButton(int x, int y, int width, int height, ArrayList<E> array) {
		itemList = array;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		createButtons();
		mainButton = new SwingButton(x,y,width,height);
		mainButton.setImage("res/menu/lobby/dropdownButton.png");
		mainButton.setText(buttonList.get(0).getText());
		mainButton.setAction(new Action(){
			@Override
			public void performAction() {
				setOpen(open == false? true : false);
			}
		});
		open = false;
	}

	public void createButtons() {
		buttonList.clear();
		int yPos = y + height;
		int buttonHeight = height;
		int value = 2;
		for(E item : itemList) {
			SwingButton b = new SwingButton(x, yPos, width, buttonHeight);
			b.setImage("res/menu/lobby/textItem.png");
			b.setText(item.toString());
			buttonList.add(b);
			values.put(b, value);
			yPos = yPos + buttonHeight;
			height = height + height;
			value = value + 1;
		}
	}
	
	public void addValue(E e) {
		itemList.add(e);
		createButtons();
	}
	
	public int getSelectedValue() {
		return selectedValue;
	}
	
	public void removeValue(E e) {
		itemList.remove(e);
		createButtons();
	}
	
	public void setOpen(boolean b) {
		open = b;
	}
	
	public void draw(Graphics g) {
		mainButton.draw(g);
		if(open) {
			for(SwingButton b : buttonList) {
				b.draw(g);
			}
		}
	}
	
	@Override
	public boolean contains(Point p) {
		boolean horizontal = p.x >= x && p.x <= x + width; 
		boolean vertical = p.y >= y && p.y <= y + height;
		return horizontal && vertical;
	}

	@Override
	public boolean mousePressed(Point p) {
			if (mainButton.mousePressed(p)) {return true;}
			for (Button b : buttonList) {
				if (b.mousePressed(p)) {
					selectedValue = values.get(b);
					mainButton.setText(b.getText());
					open = false;
					return true;
				}
			}
			return this.contains(p);
	}
	
	@Override
	public boolean mouseReleased(Point p) {
		if (mainButton.mouseReleased(p)) {return true;}
		return this.contains(p);
	}
}