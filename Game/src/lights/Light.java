package lights;

import org.joml.Vector3f;

public class Light {
	
	/**
	 * Position and color of the light.
	 */
	private Vector3f pos, color;
	
	/**
	 * Creates a light at the origin with a pink color (LMAO).
	 */
	public Light() {
		
		this.pos = new Vector3f(0);
		this.color = new Vector3f(1.0f, 0.4117f, 0.7058f);
		
	}
	
	/**
	 * Creates a light at the specified position and with the specified color.
	 * @param pos The three-dimensional world-space position of the light.
	 * @param color The RGB color of the light.
	 */
	public Light(Vector3f pos, Vector3f color) {
		
		this.pos = new Vector3f(pos);
		this.color = new Vector3f(color);
		
	}

	
	/**
	 * Returns the current position of the light.
	 * @return [Vector3f] The world-space light position.
	 */
	public Vector3f getPosition() {
		return pos;
	}
	
	/**
	 * Moves the light using the specified translation vector.
	 * @param translation The translation to be applied on the light.
	 * @return [void]
	 */
	public void translate(Vector3f translation) {
		pos.add(translation);
	}
	
	/**
	 * Sets the current position of the light.
	 * @return [void]
	 */
	public void setPosition(Vector3f pos) {
		this.pos = new Vector3f(pos);
	}
	
	/**
	 * Returns the color of the light.
	 * @return [Vector3f] The light color.
	 */

	public Vector3f getColor() {
		return color;
	}
	
}
