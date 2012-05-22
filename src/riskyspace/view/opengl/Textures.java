package riskyspace.view.opengl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLException;
import javax.media.opengl.GLProfile;

import riskyspace.model.Player;
import riskyspace.view.PlayerColors;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;

public class Textures {

	private static final Map<GLAutoDrawable, Map<String, Texture>> textures = new HashMap<GLAutoDrawable, Map<String, Texture>>();
	
	public static Texture bindTexture(String textureName, GLAutoDrawable drawable) {
		Map<String, Texture> loadedTextures = textures.get(drawable);
		
		if (loadedTextures == null) {
			loadedTextures = new HashMap<String, Texture>();
			textures.put(drawable, loadedTextures);
		}
		
		Texture texture = loadedTextures.get(textureName);
		
		if (texture == null) {
			if (textureName.startsWith("colonymarker:")) {
				Player p = Enum.valueOf(Player.class, textureName.substring("colonymarker:".length()));
				texture = createColonyTexture(PlayerColors.getColor(p));
				loadedTextures.put(textureName, texture);
			} else {
				try {
					texture = TextureIO.newTexture(new File("res/textures/" + textureName + ".png"), false);
					loadedTextures.put(textureName, texture);
				} catch (GLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		texture.bind(drawable.getGL());
		
		return texture;
	}
	
	/**
	 * Creates a Circle Texture of any Color used as Colony marker
	 * @param color The Color of the Circle
	 */
	private static Texture createColonyTexture(Color color){
		BufferedImage img = new BufferedImage(64, 64, BufferedImage.TYPE_INT_ARGB);
		Graphics g = img.createGraphics();
		g.setColor(color);
		g.fillOval(2, 2, 59, 59);
		return AWTTextureIO.newTexture(GLProfile.getDefault(), img, false);
	}
}