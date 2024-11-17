package com.bubble_bobble;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.Rectangle2D;

/**
 * 
 * The Collidable class is the superclass for all "collidable" objects in the game. Collidables are affected by
 * gravity and store information about their location, graphics, and current state. Collidables can move around
 * on screen and collide with each other.
 *
 * @author Joshua Eckels and Sabri Amer
 *         Created Nov 9, 2018.
 */
public abstract class Collidable {
	
	protected static final double DT = 0.02; //differential time for calculating speeds
	protected static final int PRECISION = 6; //precision in detecting collisions
	protected static final double GRAVITY = 60;
	protected static final double JUMP_VELOCITY = -100;
	protected static final int DX = 1;
	protected static final int SIZE = 35;
	
	//for use by "character" objects (ex. Hero, BubbleBuster,. . .)
	protected double vy = 0; //vertical velocity
	protected int direction = 1; // -1=left, 1=right
	protected double currentDistance = 0; //tracks distance travelled for enemies
	protected boolean isJumping = false;
	protected boolean isFalling = true;
	protected boolean isCollidingLeft = false;
	protected boolean isCollidingRight = false;

	//for use by all Collidable objects
	protected Level level;
	protected Image sprite;
	protected Image sprites[];
	protected int positionX;
	protected int positionY;
	protected int width;
	protected int height;
	protected Color color;
	protected int scoreValue;
	protected boolean isPaused = false;
	protected boolean isAlive = true;
	
	public Collidable(Level level, int x, int y, int width, int height) {
		this.level = level;
		this.positionX = x;
		this.positionY = y;
		this.width = width;
		this.height = height;
	}
	
	/**
	 * 
	 * Draws the collidable onto the given graphics object.
	 *
	 * @param g -> graphics object
	 */
	public void draw(Graphics2D g) {
		g.drawImage(this.sprite, this.positionX, this.positionY, null);
	}
	
	// Mainly for use in the design phase (as well as object colors)
	public Rectangle2D getShape() {
		return new Rectangle2D.Double(this.positionX, this.positionY, this.width, this.height);
	}
	
	public void changePause() {
		this.isPaused = (this.isPaused) ? false : true;
	}
	
	public void die(){
		this.level.removeCollidable(this);
	}
	
	/**
	 * 
	 * Adds a score to the current level score.
	 *
	 */
	public void setScore() {
		this.level.score += this.scoreValue;
	}
	
	/**
	 * 
	 * For use by character-like collidables (i.e. Hero, Monster, etc.). Detects top, right, and left collisions
	 * with platform objects.
	 *
	 * @param p -> Platform object
	 */
	public void collideWithPlatform(Platform p) {
		double bottomY = this.positionY + this.height;
		double rightX = this.positionX + this.width;
		
		if(this.isAlive) {
			
			//top collision
			if (((bottomY < (p.positionY + PRECISION)) && (bottomY > p.positionY) && this.isFalling)) {
				this.vy = 0;
				this.isJumping = false;
			}
			
			//right collision
			if (rightX > p.positionX && rightX < (p.positionX + PRECISION) && !(this.positionY < p.positionY)) {
				this.isCollidingRight = true;
			}
			
			//left collision
			if (this.positionX < (p.positionX + p.width) && this.positionX > (p.positionX + p.width - PRECISION)
					&& !(this.positionY < p.positionY)) {
				this.isCollidingLeft = true;
			}
		}
	}
	
	public abstract void updatePosition();
	
	public abstract void addToList();
}
