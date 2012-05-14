package riskyspace.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.awt.Graphics;
import java.awt.Point;

public class DropdownButton implements Clickable {
	
	private List<String> itemList;
	
	private List<Button> buttonList = new ArrayList<Button>();
	private List<String> valuesList = new ArrayList<String>();
	
	private int selectedValue;
	
	private boolean open = false;

	private int x;
	private int y;
	private int width;
	private int height;
	
	private Button mainButton;
	
	public DropdownButton(int x, int y, int width, int height, ArrayList<String> array) {
		itemList = array;
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		createButtons();
		mainButton = new Button(x,y,width,height);
		mainButton.setImage("res/menu/lobby/dropdownMenu/dropdownButton.png");
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
		int menuHeight = height;
		for(String item : itemList) {
			Button b = new Button(x, yPos, width, menuHeight);
			b.setImage("res/menu/lobby/dropdownMenu/dropdownItem.png");
			b.setText(item.toString());
			buttonList.add(b);
			yPos = yPos + menuHeight;
			height = height + height;
		}
	}
	
	public void addValue(String s) {
		itemList.add(s);
		createButtons();
	}
	
	public int getSelectedValue() {
		return selectedValue;
	}
	
	public void removeValue(String s) {
		itemList.remove(s);
		createButtons();
	}
	
	public void setOpen(boolean b) {
		open = b;
	}
	
	public void draw(Graphics g) {
		mainButton.draw(g);
		if(open) {
			for(Button b : buttonList) {
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
					System.out.println(b.getText());
					selectedValue = Integer.parseInt(b.getText().substring(0, 1));
					System.out.println(selectedValue);
					return true;
				}
			}
			return false;
	}
	
	@Override
	public boolean mouseReleased(Point p) {
			if (mainButton.mouseReleased(p)) {return true;}
			if (this.contains(p)) {
				return true;
			} else {
				return false;
			}
	}
	

}
