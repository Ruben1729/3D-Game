package entities;

import org.joml.Vector3f;

import engine.TexturedMesh;
import input.Keybinds;
import input.KeyboardInput;
import main.Game;

/**
 * Class used to create an object of a player
 */
public class Player extends Entity{

	private float health, damage, speed;//player stats
	
	/**
	 * Constructor that takes in a model to create the shape.
	 * @param model Model of the character.
	 */
	public Player(TexturedMesh model) {
		super(model);
		// TODO Auto-generated constructor stub
		speed = 0.5f;
		
	}

	/**
	 * Constructor that takes in a position, rotation, scale and model to create a player.
	 * @param pos position of the player.
	 * @param rot rotation of the player.
	 * @param scale scale of the player.
	 * @param model model of the player.
	 */
	public Player(Vector3f pos, Vector3f rot, float scale, TexturedMesh model) {
		super(pos, rot, scale, model);
		// TODO Auto-generated constructor stub
		
	}
	
	/**
	 * Update method for the player object
	 */
	public void tick() {
		
		updateVelocity();
		translate(getVelocity());
		
	}
	
	/**
	 * Method that updates the velocity of the player with the given speed.
	 */
	public void updateVelocity() {
		
		Vector3f direction = new Vector3f(0);
		
		if(KeyboardInput.isKeyDown(Keybinds.PLYR_FORWARD))
			direction.add(new Vector3f(0, 0, -1));
		
		if(KeyboardInput.isKeyDown(Keybinds.PLYR_BACKWARD))
			direction.add(new Vector3f(0, 0, 1));

		
		if(KeyboardInput.isKeyDown(Keybinds.PLYR_LEFT))
			direction.add(new Vector3f(-1, 0, 0));

		
		if(KeyboardInput.isKeyDown(Keybinds.PLYR_RIGHT))
			direction.add(new Vector3f(1, 0, 0));
		
		direction.mul(speed);
		setVelocity(direction.mulTransposePosition(Game.getCamera().getYawTransform()));
		
	}
	
	//GETTERS AND SETTERS
	public float getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public float getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}

}
