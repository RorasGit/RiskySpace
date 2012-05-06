package riskyspace.view;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GraphicsEnvironment;
import java.io.IOException;
import java.io.InputStream;

public class Fonts {
	
	private static Font ROBO;
	
	/**
	 * Returns the ROBO font in size 1. Use deriveFont(int size) method to get another size.
	 */
	public static Font getFont() {
		if (ROBO == null) {
			InputStream fontStream = Fonts.class.getResourceAsStream("ROBO.ttf");
			try {
				ROBO = Font.createFont(Font.TRUETYPE_FONT, fontStream);
			} catch (FontFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(ROBO);
		}
		return ROBO;
	}
}
