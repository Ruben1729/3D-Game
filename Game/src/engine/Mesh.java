package engine;

import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDrawElements;

import util.Vao;

/**
 * Represents a static three-dimensional model for render.
 */
public class Mesh {
	
	/**
	 * The VAO and vertex count for the mesh.
	 */
	private int vertexCount;
	private Vao vao;
	
	/**
	 * Creates a mesh using the specified VAO and vertex count. 
	 * @param vaoID The ID of the Vertex Array Object containing the mesh data.
	 * @param vertexCount The vertex count of the mesh.
	 */
	public Mesh(Vao vao, int vertexCount) {
		this.vao = vao;
		this.vertexCount = vertexCount;
	}
	
	/**
	 * Returns the VAO ID of the mesh.
	 * @return [<b>int</b>] The ID of the VAO used for the mesh.
	 */
	public int getVaoID() {
		return vao.getID();
	}

	/**
	 * Returns the VAO associated with the mesh.
	 * @return [{@link Vao}] The Vertex Array Object (VAO) in which the mesh data is stored.
	 */ 
	public Vao getVao() {
		return vao;
	}
	
	/**
	 * Returns the vertex count of the mesh.
	 * @return [<b>int</b>] The vertex count of the mesh.
	 */
	public int getVertexCount() {
		return vertexCount;
	}
	
	/**
	 * Draws the mesh on screen using the indices provided.
	 * @return [<b>void</b>]
	 */
	public void draw(){
		glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
	}
	
}
