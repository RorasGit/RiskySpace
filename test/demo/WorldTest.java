package demo;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import org.junit.Test;

public class WorldTest {

	@Test
	public void testWorld() {
		JFrame frame = new JFrame();
		frame.setLayout(new GridLayout(20, 20));
		frame.setSize(500, 500);
		Map<Position,Territory> map = new World().getTerritories();
		for (int row = 1; row <= 20; row++) {
			for (int col = 1; col <= 20; col++) {
				JPanel p = new JPanel();
				p.setBorder(new LineBorder(Color.white));
				if(map.get(new Position(row, col)).hasPlanet()){
					if (map.get(new Position(row, col)).getPlanet().getType() == Resource.METAL) {
						p.setBackground(Color.lightGray);
					}else{
						p.setBackground(Color.green);
					}
				}else{
					p.setBackground(Color.black);
				}
				frame.add(p);
			}
			System.out.println();
		}

		frame.setVisible(true);
		while (frame.isVisible()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}

}
