package entities;

import static java.lang.Math.toRadians;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.TexturedMesh;

/**
 * Represents an in-game entity with a position, rotation, and scale.
 */
public class Entity {
	
	/**
	 * Rotation, velocity and position of the entity.
	 */
	private Vector3f pos, rot, vel;
	
	/**
	 * Scale of the entity.
	 */
	private float scale;
	
	/**
	 * Mesh to be rendered.
	 */
	private TexturedMesh model;
	
	
	/**
	 * Creates an entity using the specified mesh.
	 * @param model The mesh of the entity.
	 */
	public Entity(TexturedMesh model) {
		this.pos = new Vector3f(0);
		this.rot = new Vector3f(0);
		this.scale = 1.0f;
		this.model = model;
	}
	
	/**
	 * Creates an entity with initial position, rotation, and scale.
	 * @param pos The initial position of the entity.
	 * @param rot The initial rotation of the entity.
	 * @param scale The initial scale of the entity.
	 * @param model The mesh of the entity.
	 */
	public Entity(Vector3f pos, Vector3f rot, float scale, TexturedMesh model) {
		this.pos = new Vector3f(pos);
		this.rot = new Vector3f(rot);
		this.scale = scale;
		this.model = model;
	}
	
	
	/**
	 * Returns the textured mesh of the entity.
	 * @return [{@link TexturedMesh}] The entity's mesh.
	 */
	public TexturedMesh getTexturedMesh() {
		return this.model;
	}
	
	/**
	 * Sets the rotation of the entity to the specified vector.
	 * @param rot The Euler rotation to set.
	 * @return [<b>void</b>]
	 */
	public void setRotation(Vector3f rot) {
		this.rot = new Vector3f(rot);
	}
	
	/**
	 * Sets the position of the entity to the specified vector.
	 * @param rot The position to set.
	 * @return [<b>void</b>]
	 */
	public void setPosition(Vector3f pos) {
		this.pos = new Vector3f(pos);
	}
	
	/**
	 * Sets the velocity of the entity.
	 * @param vel The new velocity of the entity.
	 * @return [<b>void</b>]
	 */
	public void setVelocity(Vector3f vel) {
		this.vel = vel;
	}
	
	/**
	 * Sets the scale of the entity to the specified value.
	 * @param scale The scale to set the entity.
	 * @return [<b>void</b>]
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}
	
	/**
	 * Rotates the entity using the specified rotation vector.
	 * @param rotation The rotation to be applied on the entity.
	 * @return [<b>void</b>]
	 */
	public void rotate(Vector3f rotation) {
		rot.add(rotation);
	}
	
	/**
	 * Moves the entity using the specified translation vector.
	 * @param translation The translation to be applied on the entity.
	 * @return [<b>void</b>]
	 */
	public void translate(Vector3f translation) {
		pos.add(translation);
	}
	
	/**
	 * Accelerates the velocity of the entity by the specified amount.
	 * @param dir A vector containing the direction and amplitude of the acceleration.
	 * @return [<b>void</b>]
	 */
	public void accelerate(Vector3f dir) {
		this.vel.add(dir, vel);
	}
	
	/**
	 * Sets the velocity of the entity to 0.
	 * @return [<b>void</b>]
	 */
	public void stop() {
		this.vel = new Vector3f(0);
	}
	
	/**
	 * Scales the entity by a percentage of its current size.
	 * @param scale The floating point quantity by which to scale the entity.
	 * @return [<b>void</b>]
	 */
	public void scale(float scale) {
		this.scale *= scale;
	}
	
	/**
	 * A method to get the position of an object as a vector
	 * @return [{@link Vector3f}] A vector representing the position of the object
	 */
	public Vector3f getPosition() {
		return pos;
	}
	
	/**
	 * Returns the x-component value of the entity's position.
	 * @return [<b>float</>] The x-value of the entity's position.
	 */ 
	public float getPosX() {
		return pos.x;
	}
	
	/**
	 * Returns the y-component value of the entity's position.
	 * @return [<b>float</>] The y-value of the entity's position.
	 */ 
	public float getPosY() {
		return pos.y;
	}
	
	/**
	 * Returns the z-component value of the entity's position.
	 * @return [<b>float</>] The z-value of the entity's position.
	 */ 
	public float getPosZ() {
		return pos.z;
	}
	
	/**
	 * Returns the velocity of the entity.
	 * @return [{@link Vector3f}] A vector containing the velocity of the entity;
	 */
	public Vector3f getVelocity() {
		return vel;
	}

	/**
	 * Returns the Euler rotation of the entity.
	 * @return [{@link Vector3f}] A vector containing the Euler rotation of the entity;
	 */
	public Vector3f getRotation() {
		return rot;
	}
	
	/**
	 * Returns the entity's model matrix.
	 * @return [{@link Matrix4f}] The model matrix of this entity.
	 */
	public Matrix4f getTransform() {
		
		// Identity matrix
		Matrix4f mat = new Matrix4f();
		mat.identity();
		
		// Position
		mat.translate(pos);
		
		// Rotation
		mat.rotate((float)toRadians(rot.x), new Vector3f(1, 0, 0));
		mat.rotate((float)toRadians(rot.y), new Vector3f(0, 1, 0));
		mat.rotate((float)toRadians(rot.z), new Vector3f(0, 0, 1));
		
		// Scale
		mat.scale(scale);
		
		// Return result
		return mat;
		
	}
	
}
