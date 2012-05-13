package riskyspace.view.openglImpl;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLException;

import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureIO;

public class Textures {

	private static final Map<GLAutoDrawable, Map<String, Texture>> textures = new HashMap<GLAutoDrawable, Map<String, Texture>>();
	
	public static Texture bindTexture(String textureName, GLAutoDrawable drawable) {
		Map<String, Texture> loadTextures = textures.get(drawable);
		
		if (loadTextures == null) {
			loadTextures = new HashMap<String, Texture>();
			textures.put(drawable, loadTextures);
		}
		
		Texture texture = loadTextures.get(textureName);
		
		if (texture == null) {
			try {
				texture = TextureIO.newTexture(new File(textureName + ".png"), false);
				loadTextures.put(textureName, texture);
			} catch (GLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		texture.bind(drawable.getGL());
		
		return texture;
	}
}