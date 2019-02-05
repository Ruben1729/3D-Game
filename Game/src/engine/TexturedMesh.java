package engine;

public class TexturedMesh {
	
	private Texture tex;
	private Mesh mesh;
	
	public TexturedMesh(Mesh mesh, Texture texture) {
		this.mesh = mesh;
		this.tex = texture;
	}

	public Texture getTexture() {
		return tex;
	}

	public Mesh getMesh() {
		return mesh;
	}
	
}
