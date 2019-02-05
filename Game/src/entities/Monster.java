package entities;

import org.joml.Vector3f;

import engine.TexturedMesh;

public class Monster extends Entity{

	private int health, damage, speed;
	
	public Monster(Vector3f pos, Vector3f rot, float scale, TexturedMesh model) {
		super(pos, rot, scale, model);
		// TODO Auto-generated constructor stub
	}

	public Monster(TexturedMesh model) {
		super(model);
		// TODO Auto-generated constructor stub
	}
	
	public int getHealth() {
		return health;
	}

	public void setHealth(int health) {
		this.health = health;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int speed) {
		this.speed = speed;
	}
	
}
