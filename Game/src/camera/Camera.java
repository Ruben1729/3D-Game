package camera;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import static java.lang.Math.*;

/**
 * Represents a the camera in the game.
 */
public abstract class Camera {
	
	/**
	 * Position and rotation of the camera.
	 */
	protected Vector3f pos, rot;
	
	/**
	 * Creates a camera and sets its position and rotation to the origin.
	 */
	public Camera() {
		this.pos = new Vector3f(0);
		this.rot = new Vector3f(0);
	}
	
	/**
	 * Sets the rotation of the camera to the specified vector.
	 * @param rot The Euler rotation to set.
	 * @return [<b>void</b>]
	 */
	public void setRotation(Vector3f rot) {
		this.rot = new Vector3f(rot);
	}
	
	/**
	 * Sets the position of the camera to the specified vector.
	 * @param rot The position to set.
	 * @return [<b>void</b>]
	 */
	public void setPosition(Vector3f pos) {
		this.pos = new Vector3f(pos);
	}
	
	/**
	 * Rotates the camera so that it faces the specified three-dimensional coordinate.
	 * @param point The point the camera is to look at.
	 * @return [<b>void</b>]
	 */
	public void lookAt(Vector3f point) {
		
		// Direction relative to camera.
		Vector3f dir = new Vector3f();
		point.sub(pos, dir);
		
		// Calculate yaw.
		float yaw = (float) toDegrees(atan2(dir.z, dir.x)) + 90.0f;
		
		// Calculate pitch.
		float horizLength = (float) sqrt(pow(dir.x, 2) + pow(dir.z, 2));
		float pitch = (float) toDegrees(atan(dir.y / horizLength));
		
		// Apply the rotation to the camera.
		this.rot = new Vector3f(pitch, yaw, 0.0f);
		
	}
	
	/**
	 * Rotates the camera using the specified rotation vector.
	 * @param rotation The rotation to be applied on the camera.
	 * @return [<b>void</b>]
	 */
	public void rotate(Vector3f rotation) {
		rot.add(rotation);
	}
	
	/**
	 * Moves the camera using the specified translation vector.
	 * @param translation The translation to be applied on the camera.
	 * @return [<b>void</b>]
	 */
	public void translate(Vector3f translation) {
		pos.add(translation);
	}
	
	/**
	 * Mirrors the camera's pitch around the 0° mark.
	 * @return [<b>void</b>]
	 */
	public void invertPitch() {
		this.rot.x = -this.rot.x;
	}
	
	/**
	 * Returns a three-dimensional unit vector representing the camera's current direction.
	 * @return [{@link Vector3f}] The unit vector direction.
	 */
	public Vector3f getDirection() {
		return new Vector3f((float)(Math.sin(Math.toRadians(rot.z))), 0.0f, (float)(Math.cos(Math.toRadians(rot.z))));
	}
	
	/**
	 * Moves the Camera depending on the last change in position of the mouse cursor.
	 * @param dx The difference in the x-position of the mouse cursor.
	 * @param dy The difference in the y-position of the mouse cursor.
	 * @return [<b>void</b>]
	 * */
	public abstract void move(int dx, int dy);
	
	/**
	 * Returns the camera's view matrix.
	 * @return [{@link Matrix4f}] The view matrix.
	 */
	public Matrix4f getTransform() {
		
		// Create the matrix and set it to identity.
		Matrix4f mat = new Matrix4f().identity();
		
		// Rotation.
		mat.rotate((float)toRadians(-rot.x), new Vector3f(1, 0, 0)); 	// pitch
		mat.rotate((float)toRadians(rot.y), new Vector3f(0, 1, 0)); 	// yaw
		mat.rotate((float)toRadians(rot.z), new Vector3f(0, 0, 1)); 	// roll
		
		// Position.
		mat.translate(new Vector3f(-pos.x, -pos.y, -pos.z));
		
		// Return the transformation matrix.
		return mat;
		
	}
	
	/**
	 * Returns the camera's yaw transform matrix.
	 * @return [{@link Matrix4f}] The yaw transform matrix.
	 */
	public Matrix4f getYawTransform() {

        // Create the matrix and set it to identity.
        Matrix4f mat = new Matrix4f().identity();

        // Rotation.
        mat.rotate((float)toRadians(rot.y), new Vector3f(0, 1, 0));     // yaw

        // Return the transformation matrix.
        return mat;

    }
	
}
