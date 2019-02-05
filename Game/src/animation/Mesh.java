package animation;


/**
 * Represents a static three-dimensional model for render.
 */
public class Mesh {
	
	/**
	 * The VAO ID and the vertex count of the mesh.
	 */
	private int vaoID, vertexCount;
	
	/**
	 * Creates a mesh using the specified VAO and vertex count. 
	 * @param vaoID The ID of the Vertex Array Object containing the mesh data.
	 * @param vertexCount The vertex count of the mesh.
	 */
	public Mesh(int vaoID, int vertexCount) {
		this.vaoID = vaoID;
		this.vertexCount = vertexCount;
	}
	
	/**
	 * Returns the VAO ID of the mesh.
	 * @return [int] The ID of the VAO used for the mesh.
	 */
	public int getVaoID() {
		return vaoID;
	}

	/**
	 * Returns the vertex count of the mesh.
	 * @return [int] The vertex count of the mesh.
	 */
	public int getVertexCount() {
		return vertexCount;
	}
	
}
