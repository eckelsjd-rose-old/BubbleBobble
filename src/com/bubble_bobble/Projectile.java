package com.bubble_bobble;

/**
 * 
 * Projectile acts as the superclass for bubbles and fireballs. Projectiles
 * travel horizontally for a certain distance and run into other Collidables.
 *
 * @author Joshua Eckels and Sabri Amer Created Nov 9, 2018.
 */
public abstract class Projectile extends Collidable {

	private static final int MAX_DISTANCE = 125;
	protected double dx = 3;
	protected double dy = 0;

	public Projectile(Level level, int x, int y, int dir) {
		super(level, x, y, SIZE, SIZE);
		this.direction = dir;
		this.dx = this.dx * this.direction;
		this.width = SIZE;
		this.height = SIZE;
	}

	@Override
	public void updatePosition() {
		if (Math.abs(this.currentDistance) >= MAX_DISTANCE) {
			this.dx = 0;
		}
		this.positionX += this.dx;
		this.currentDistance += this.dx;
		this.positionY += this.dy;
	}
}
