package camera;

import org.joml.Vector3f;

import entities.Entity;

public class CameraTPS extends Camera{

	/**
	 * The entity to follow.
	 */
	private Entity target;
	
	private float pitch = 0;
	private float distanceFromTarget = 20;
	private float angleAroundTarget = 0;
	
	/**
	 * Creates a third-person camera with the specfied target.
	 * @param target The target entity.
	 */
	public CameraTPS(Entity target){
		this.target = target;
	}
	
	/**
	 * {@inheritDoc}
	 * @return [<b>void</b>]
	 */
	public void move(int dx, int dy) {
		
		float pitchChange = dy * 0.1f;
		pitch -= pitchChange;
		
		float angleChange = dx * 0.1f;
		angleAroundTarget += angleChange;
		
		float horDis = distanceFromTarget * (float) Math.cos(Math.toRadians(pitch));
		float verDis = distanceFromTarget * (float) Math.sin(Math.toRadians(pitch));
		
		float theta = angleAroundTarget;
		
		float offsetX = (float) (horDis * Math.sin(Math.toRadians(theta)));
		float offsetZ = (float) (horDis * Math.cos(Math.toRadians(theta)));
		
		setPosition(new Vector3f(target.getPosX() - offsetX, target.getPosY() + verDis, target.getPosZ() - offsetZ));
		
		lookAt(target.getPosition());

		if(!target.getVelocity().equals(new Vector3f(0)))
			target.setRotation(new Vector3f(target.getRotation().x, target.getRotation().y, (-rot.y + 180)));
		
	}
	
	/**
	 * Returns the entity the camera is currently following.
	 * @return [{@link Entity}] The target entity.
	 */
	public Entity getTarget() {
		return target;
	}
	
	/**
	 * Sets the entity the camera is currently following.
	 * @param target The target entity to follow.
	 * @return [<b>void</b>]
	 */
	public void setTarget(Entity target) {
		this.target = target;
	}
	
	/**
	 * Sets the distance between the camera and the target entity.
	 * @param distance The distance between the camera and the target entity.
	 * @return [<b>void</b>]
	 */
	public void setDistanceFromTarget(float distance) {
		this.distanceFromTarget = distance;
	}
	
}
