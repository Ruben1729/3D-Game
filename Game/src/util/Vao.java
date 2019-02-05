package util;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_INT;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30.glVertexAttribIPointer;

import java.util.HashMap;

/**
 * Represents a Vertex Array Object (VAO).
 */
public class Vao {

	/**
	 * The VAO's ID.
	 */
	private int id;

	/**
	 * The VBOs serving as attributes of the VAO.
	 */
	private HashMap<Integer, Vbo> attributes = new HashMap<Integer, Vbo>();

	/**
	 * Creates a Vertex Array Object (VAO) with the specified ID.
	 * @param id The ID of the VAO.
	 */
	public Vao(int id) {
		this.id = id;
	}

	/**
	 * Binds the VAO for use and manipulation.
	 * @return [<b>void</b>]
	 */
	public void bind() {
		glBindVertexArray(id);
	}

	/**
	 * Unbinds the VAO.
	 * @return [<b>void</b>]
	 */
	public void unbind() {
		glBindVertexArray(0);
	}

	/**
	 * Enables the use of the specified attribute lists.
	 * @param attribs One or more attribute list indices.
	 * @return [<b>void</b>]
	 */
	public void bind(int... attribs) {
		bind();
		for (int i : attribs)
			glEnableVertexAttribArray(i);
	}

	/**
	 * Disables the use of the specified attribute lists.
	 * @param attribs One or more attribute list indices.
	 * @return [<b>void</b>]
	 */
	public void unbind(int... attribs) {
		for (int i : attribs)
			glDisableVertexAttribArray(i);
		unbind();
	}

	/**
	 * Returns the ID of the VAO.
	 * @return [<b>int</b>] The ID of the VAO.
	 */
	public int getID() {
		return id;
	}
	
	/**
	 * Store a floating-point array of data as a vertex attribute in the VAO.
	 * @param attribIndex The attribute list index in which to store the data.
	 * @param data The data to store.
	 * @param size The size of a single vertex worth of data (eg. 3 for a 3D position).
	 * @return [<b>void</b>]
	 */
	public void storeFloatData(int attribIndex, float[] data, int size) {
		
		// Create the VBO.
		Vbo buffer = Vbo.create(GL_ARRAY_BUFFER);
		
		// Bind the buffer.
		buffer.bind();
		
		// Add it to the attribute lists.
		attributes.put(attribIndex, buffer);
		
		// Store the data in it.
		buffer.store(data);
		
		// Make a pointer to the VBO in the VAO.
		glVertexAttribPointer(attribIndex, size, GL_FLOAT, false, 0, 0);
		
		// Unbind the VBO.
		buffer.unbind();
		
	}
	
	/**
	 * Store a integer array of data as a vertex attribute in the VAO.
	 * @param attribIndex The attribute list index in which to store the data.
	 * @param data The data to store.
	 * @param size The size of a single vertex worth of data (eg. 3 for a 3D position).
	 * @return [<b>void</b>]
	 */
	public void storeIntData(int attribIndex, int[] data, int size) {
		
		// Create the VBO.
		Vbo buffer = Vbo.create(GL_ARRAY_BUFFER);
		
		// Bind the buffer.
		buffer.bind();
		
		// Add it to the attribute lists.
		attributes.put(attribIndex, buffer);
		
		// Store the data in it.
		buffer.store(data);
		
		// Make a pointer to the VBO in the VAO.
		glVertexAttribIPointer(attribIndex, size, GL_INT, 0, 0);
		
		// Unbind the VBO.
		buffer.unbind();
		
	}
	
	/**
	 * Store a integer array of index data for vertices in the VAO.
	 * @param data The data to store.
	 * @return [<b>void</b>]
	 */
	public void storeIndices(int[] data) {
		
		// Create the VBO.
		Vbo buffer = Vbo.create(GL_ELEMENT_ARRAY_BUFFER);
		
		// Bind the buffer.
		buffer.bind();
		
		// Add it to the attribute lists.
		attributes.put(-1, buffer);
		
		// Store the data in it.
		buffer.storeIndices(data);
		
	}
	
	/**
	 * Deletes the VAO and all VBOs used as attributes.
	 * @return [<b>void</b>]
	 */
	public void delete() {
		for(int key : attributes.keySet())
			attributes.get(key).delete();
		glDeleteVertexArrays(id);
	}

	/**
	 * Creates and returns a Vertex Array Object.
	 * @return [{@link Vao}] The created object.
	 */
	public static Vao create() {
		int vaoID = glGenVertexArrays();
		return new Vao(vaoID);
	}

}
