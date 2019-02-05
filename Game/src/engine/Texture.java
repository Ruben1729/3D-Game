package engine;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glDeleteTextures;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

public class Texture {
	
	/**
	 * The texture ID and type (eg. {@link org.lwjgl.opengl.GL11#GL_TEXTURE_2D GL_TEXTURE_2D}).
	 */
	private int texID, type;
	
	/**
	 * Creates a texture of the given type using the given ID.
	 * @param texID The ID of the texture.
	 * @param type The type of the texture.
	 */
	protected Texture(int texID, int type) {
		this.texID = texID;
		this.type = type;
	}
	
	/**
	 * Returns the ID of the texture.
	 * @return [<b>int</b>] The ID of the VAO.
	 */
	public int getID() {
		return texID;
	}
	
	/**
	 * Binds the texture to the specified texture unit in OpenGL.
	 * @param unit The ID of the texture unit to bind to.
	 * @return [<b>void</b>]
	 */
	public void bindToUnit(int unit) {
		glActiveTexture(GL_TEXTURE0 + unit);
		glBindTexture(type, texID);
	}
	
	/**
	 * Deletes the texture.
	 * @return [<b>void</b>]
	 */
	public void delete() {
		glDeleteTextures(texID);
	}
}
