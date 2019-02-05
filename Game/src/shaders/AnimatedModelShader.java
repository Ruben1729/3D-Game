package shaders;

import shaders.ShaderProgram;

public class AnimatedModelShader extends ShaderProgram {
	
	/**
	 * Maximum number of joints in a skeleton.
	 */
	private static final int MAX_JOINTS = 50;

	/**
	 * Vertex shader file.
	 */
	private static final String VERTEX_SHADER = "/animated/animatedEntityVertex.glsl";
	
	/**
	 * Fragment shader file.
	 */
	private static final String FRAGMENT_SHADER = "/animated/animatedEntityFragment.glsl";
	
	/**
	 * Locations for uniform variables in the shaders.
	 */
	public int location_projectionMatrix, location_viewMatrix, location_modelMatrix;
	public int location_lightDirection, location_texture;
	public int[] location_jointTransforms;
	
	
	/**
	 * Creates the shader program for the {@link AnimatedModelRenderer} by
	 * loading up the vertex and fragment shader code files. It also gets the
	 * location of all the specified uniform variables.
	 */
	public AnimatedModelShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}

	/**
	 * {@inheritDoc}
	 * @return [void]
	 */
	protected void bindAttributes() {
		bindAttribute(0, "in_position");
		bindAttribute(1, "in_normal");
		bindAttribute(2, "in_textureCoords");
		bindAttribute(3, "in_jointIndices");
		bindAttribute(4, "in_weights");
	}
	
	/**
	 * {@inheritDoc}
	 * @return [void]
	 */
	protected void getAllUniformLocations() {
		
		location_modelMatrix = getUniformLocation("modelMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		
		location_texture = getUniformLocation("tex");
		location_lightDirection = getUniformLocation("lightDirection");
		
		location_jointTransforms = new int[MAX_JOINTS];
		
		for(int i = 0; i < MAX_JOINTS; i++)
			location_jointTransforms[i] = getUniformLocation("jointTransforms[" + i + "]");
		
	}

}
