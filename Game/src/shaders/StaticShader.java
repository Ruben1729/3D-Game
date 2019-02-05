package shaders;

import lights.Light;

public class StaticShader extends ShaderProgram{
	
	/**
	 * Vertex shader file.
	 */
	private static final String VERTEX_SHADER = "/entities/staticVertexShader.glsl";
	
	/**
	 * Fragment shader file.
	 */
	private static final String FRAGMENT_SHADER = "/entities/staticFragmentShader.glsl";
	
	/**
	 * The maximum number of lights that affect each entity.
	 */
	private static final int MAX_LIGHTS = 1;
	
	/**
	 * Locations for uniform variables in the shaders.
	 */
	public int location_modelMatrix, location_viewMatrix, location_projectionMatrix, location_texture;
	public int[] location_lightPositions, location_lightColors;
	
	/**
	 * Static shader program for models with no animations.
	 */
	public StaticShader() {
		super(VERTEX_SHADER, FRAGMENT_SHADER);
	}
	
	/**
	 * {@inheritDoc}
	 * @return [void]
	 */
	protected void bindAttributes() {
		bindAttribute(0, "pos");
		bindAttribute(1, "normal");
		bindAttribute(2, "uv");
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
		
		location_lightPositions = new int[MAX_LIGHTS];
		location_lightColors = new int[MAX_LIGHTS];
		
		for(int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPositions[i] = getUniformLocation("lightPositions[" + i + "]");
			location_lightColors[i] = getUniformLocation("lightColors[" + i + "]");
		}
	}
	
	/**
	 * Loads a light as a uniform into the shader.
	 * @param index The index at which to load the light.
	 * @param l The light to be loaded.
	 * @return [void]
	 */
	public void loadLight(int index, Light l) {
		this.loadVector(location_lightPositions[index], l.getPosition());
		this.loadVector(location_lightColors[index], l.getColor());
	}
	
}
