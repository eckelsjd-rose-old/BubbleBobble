import java.awt.Color;
import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

/**
 * 
 * Hero is the main character of the game and listens to user input from the
 * keyboard. The hero can capture monsters by shooting bubbles. The hero can
 * score points by collecting fruit and bubbles. The game ends when the hero has
 * beaten the final level, or has run out of lives.
 *
 * @author Joshua Eckels and Sabri Amer Created Nov 9, 2018.
 */
public class Hero extends Collidable {

	private static final int MAX_LIVES = 5;
	protected int lives;
	Point2D startingPoint;
	boolean invincible = false;

	public Hero(Level level, int x, int y, int width, int height) {
		super(level, x, y, width, height);
		this.startingPoint = new Point2D.Double(this.positionX, this.positionY);
		this.lives = MAX_LIVES;
		this.color = Color.YELLOW;

		this.sprites = new Image[5];
		this.sprites[0] = (new ImageIcon("sprites/hero0.png")).getImage();
		this.sprites[1] = (new ImageIcon("sprites/hero_left.gif")).getImage();
		this.sprites[2] = (new ImageIcon("sprites/hero_right.gif")).getImage();
		this.sprites[3] = (new ImageIcon("sprites/hero_invincible_left.gif")).getImage();
		this.sprites[4] = (new ImageIcon("sprites/hero_invincible_right.gif")).getImage();
		this.sprite = this.sprites[0];

	}

	@Override
	public void updatePosition() {
		this.changeSpriteDirection();
		this.isCollidingLeft = false;
		this.isCollidingRight = false;
		this.isFalling = (this.vy < 1) ? false : true;
		this.vy = this.vy + GRAVITY * DT;
		this.positionY += this.vy * DT;
	}

	@Override
	public void addToList() {
		this.level.hero = this;
	}

	@Override
	public void collideWithPlatform(Platform p) {
		super.collideWithPlatform(p);

		if (this.isAlive) {

			// right collision
			if (this.isCollidingRight) {
				this.positionX -= PRECISION / 2;
			}

			// left collision
			if (this.isCollidingLeft) {
				this.positionX += PRECISION / 2;
			}
		}
	}

	/**
	 * 
	 * Reverse sprite direction based on hero's current direction.
	 *
	 */
	public void changeSpriteDirection() {
		if (!this.invincible) {
			if (this.direction == -1) {
				this.sprite = this.sprites[1];
			}
			if (this.direction == 1) {
				this.sprite = this.sprites[2];
			}
		} else {
			if (this.direction == -1) {
				this.sprite = this.sprites[3];
			}
			if (this.direction == 1) {
				this.sprite = this.sprites[4];
			}
		}

	}

	/**
	 * 
	 * Update hero position based on the given direction.
	 *
	 * @param direction1
	 */
	public void move(String direction1) {
		if (this.isAlive && !this.isPaused) {

			// move right
			if (direction1.equals("Right") && !this.isCollidingRight) {
				this.positionX += DX;
				this.direction = 1;
			}

			// move left
			if (direction1.equals("Left") && !this.isCollidingLeft) {
				this.positionX -= DX;
				this.direction = -1;
			}

			// jump (if not already jumping or falling)
			if (direction1.equals("Up") && !this.isJumping && !this.isFalling) {
				this.vy = JUMP_VELOCITY;
				this.isJumping = true;
			}
		}
	}

	/**
	 * 
	 * Constructs a bubble object.
	 *
	 */
	public void shootBubble() {
		if (this.isAlive && !this.isPaused) {
			// spawn a bubble far enough off the hero to avoid immediate
			// collision
			int x = this.positionX + ((this.width + PRECISION) * this.direction);
			Bubble bubble = new Bubble(this.level, x, this.positionY, this.direction);
			this.level.addCollidable(bubble);
		}
	}

	/**
	 * 
	 * Reacts to a collision with an enemy (Fireball, BubbleBuster, or Incendo).
	 * Causes the level to pause and performs a death animation. Becomes
	 * invincible for a certain period of time to allow for respawning. Loses a
	 * life upon collision with enemy.
	 *
	 */
	public void collideWithEnemy() {
		if (!this.invincible) {
			this.invincible = true;
			this.isAlive = false;
			this.lives -= 1;
			this.vy = JUMP_VELOCITY;

			Timer timer = new Timer();
			
			// schedule respawn event
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Hero.this.isAlive = true;
					Hero.this.positionX = (int) Hero.this.startingPoint.getX();
					Hero.this.positionY = (int) Hero.this.startingPoint.getY() - 3 * PRECISION;
					Hero.this.color = Color.PINK;
				}
			}, 1500);

			// schedule end of invincibility period
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					Hero.this.invincible = false;
					Hero.this.color = Color.YELLOW;
				}
			}, 5000);
		}
	}
}
