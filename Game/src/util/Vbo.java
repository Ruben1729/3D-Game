package util;

import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;

/**
 * Represents a Vertex Buffer Object (VBO).
 */
public class Vbo {
	
	/**
	 * The VBO's ID.
	 */
	private int id;
	
	/**
	 * The VBO's type (eg. {@link org.lwjgl.opengl.GL15#GL_ARRAY_BUFFER GL_ARRAY_BUFFER} or 
	 * {@link org.lwjgl.opengl.GL15#GL_ELEMENT_ARRAY_BUFFER GL_ELEMENT_ARRAY_BUFFER}).
	 */
	private int type;
	
	/**
	 * Creates a Vertex Buffer Object (VBO) with the specified type and ID.
	 * @param id The ID of the VBO.
	 * @param type The type of VBO.
	 */
	private Vbo(int id, int type) {
		this.id = id;
		this.type = type;
	}
	
	/**
	 * Binds the VBO for use and manipulation.
	 * @return [void]
	 */
	public void bind() {
		glBindBuffer(type, id);
	}
	
	/**
	 * Unbinds the VAO.
	 * @return [void]
	 */
	public void unbind() {
		glBindBuffer(type, 0);
	}
	
	/**
	 * Returns the ID of this VBO.
	 * 
	 * @return [<b>int</b>] The ID of the VBO.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Stores a floating point array of data in the VBO.
	 * @param data The data to store in the VBO.
	 * @return [void]
	 */
	public void store(float[] data) {
		bind();
		glBufferData(type, data, GL_STATIC_DRAW);
	}
	
	/**
	 * Stores an integer array of data in the VBO.
	 * @param data The data to store in the VBO.
	 * @return [void]
	 */
	public void store(int[] data) {
		glBufferData(type, data, GL_STATIC_DRAW);
	}
	
	/**
	 * Stores an unsigned integer index array in the VBO.
	 * @param data The data to store in the VBO.
	 * @return [void]
	 */
	public void storeIndices(int[] data) {
		glBufferData(type, data, GL_STATIC_DRAW);
	}
	
	/**
	 * Deletes the VBO.
	 * @return [void]
	 */
	public void delete() {
		glDeleteBuffers(id);
	}
	
	/**
	 * Creates and returns a Vertex Buffer Object (VBO).
	 * @return [{@link Vao}] The created object.
	 */
	public static Vbo create(int type) {
		int vboID = glGenBuffers();
		return new Vbo(vboID, type);
	}
	
}
