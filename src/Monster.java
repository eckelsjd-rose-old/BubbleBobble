import java.awt.Image;
import java.awt.geom.Point2D;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * The Monster abstract class defines the basic characteristics of any monster
 * type in the game, including platform collisions and basic AI movement.
 *
 * @author Joshua Eckels and Sabri Amer Created Nov 9, 2018.
 */
public abstract class Monster extends Collidable {

	private static final double PROB_OF_JUMP = 0.005;
	private static final double PROB_OF_DIR_CHANGE = 0.2;
	private boolean readyToJump;
	protected boolean isBubbled = false;
	private Point2D heroLocation;
	private double maxDistance;

	public Monster(Level level, int x, int y, int width, int height) {
		super(level, x, y, width, height);
		this.sprites = new Image[3];
		Random rand = new Random();
		this.direction = (rand.nextBoolean()) ? 1 : -1;
		this.maxDistance = 100 + (Math.random() * 100) * this.direction;
	}

	/**
	 * Updates the monsters position based on probabilities and basic AI
	 * movement.
	 */
	@Override
	public void updatePosition() {
		if (!this.isBubbled) {
			this.determineMovement();

			// attempt to jump
			if (this.readyToJump && !this.isJumping && !this.isFalling) {
				this.vy = JUMP_VELOCITY;
				this.isJumping = true;
				this.readyToJump = false;
			}

			// update x and y position
			this.isCollidingLeft = false;
			this.isCollidingRight = false;
			this.positionX += 1 * this.direction;
			this.currentDistance += 1 * this.direction;

			this.isFalling = (this.vy < 1) ? false : true;
			this.vy = this.vy + GRAVITY * DT;
			this.positionY += this.vy * DT;
		} else {
			if (this.positionY >= 0) {
				this.sprite = this.sprites[2];
				this.positionY -= DX;
			}
		}
	}

	/**
	 * Monster collides with platforms similarly to a Hero, but changes its
	 * direction upon collision.
	 */
	@Override
	public void collideWithPlatform(Platform p) {
		super.collideWithPlatform(p);

		if (this.isAlive) {

			// right collision
			if (this.isCollidingRight) {
				this.isCollidingRight = false;
				this.positionX -= PRECISION;
				this.direction *= -1;
				this.changeSpriteDirection();
				this.currentDistance = 0;
			}

			// left collision
			if (this.isCollidingLeft) {
				this.isCollidingLeft = false;
				this.positionX += PRECISION;
				this.direction *= -1;
				this.changeSpriteDirection();
				this.currentDistance = 0;
			}
		}
	}

	/**
	 * 
	 * Monster determines its movement based on the location of the Hero and
	 * randomly chosen distances. If hero is above the monster, the monster will
	 * have a chance to jump. Monster chooses a random distance and moves to it,
	 * then changes direction randomly and repeats.
	 *
	 */
	public void determineMovement() {
		// decide if monster should jump ( P(jump) = .5% ) when hero is above
		// monster )
		if (this.positionY > (this.heroLocation.getY() + PRECISION) && Math.random() < PROB_OF_JUMP) {
			this.readyToJump = true;
		}

		// give monster a new target distance and direction
		if (Math.abs(this.currentDistance) >= Math.abs(this.maxDistance)) {
			this.changeDirection();
			this.maxDistance = 100 + (Math.random() * 100) * this.direction;
			this.currentDistance = 0;
		}

	}

	public void updateHeroLocation(Point2D heroLocation1) {
		this.heroLocation = heroLocation1;
	}

	/**
	 * 
	 * Gives monster a 20% chance of changing direction.
	 *
	 */
	public void changeDirection() {
		this.direction = (Math.random() < PROB_OF_DIR_CHANGE) ? 1 : -1;
		this.changeSpriteDirection();
	}

	/**
	 * 
	 * Monster will rise to top of screen upon collision with a bubble. After a
	 * certain time, (dependent on the current height), the monster will free
	 * itself.
	 *
	 */
	public void collideWithBubble() {
		this.isBubbled = true;
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Monster.this.isBubbled = false;
			}
		}, 10000 + this.positionY / 4);
	}

	public void changeSpriteDirection() {
		if (this.direction == -1) {
			this.sprite = this.sprites[0];
		}
		if (this.direction == 1) {
			this.sprite = this.sprites[1];
		}
	}
}
