import java.awt.Color;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

/**
 * 
 * Bubble is shot by the hero and traps monsters upon contact. If a monster is
 * caught, the bubble "transports" the monster to the top of the screen. The
 * bubble spawns fruit after a certain time of resting at the top. The bubble
 * pops after sitting untouched for a certain period of time.
 *
 * @author Joshua Eckels and Sabri Amer Created Nov 9, 2018.
 */
public class Bubble extends Projectile {

	private boolean reachedTop = false;
	boolean hasFruit = false;

	public Bubble(Level level, int x, int y, int dir) {
		super(level, x, y, dir);
		this.color = Color.CYAN;
		this.scoreValue = 10;
		this.sprites = new Image[3];
		this.sprites[0] = (new ImageIcon("sprites/bubble.gif")).getImage();
		this.sprites[1] = (new ImageIcon("sprites/bubble6.png")).getImage();
		this.sprites[2] = (new ImageIcon("sprites/fruit_bubble.png")).getImage();
		this.sprite = this.sprites[0];
	}

	/**
	 * Bubble moves horizontally for a certain distance, then moves vertically
	 * to the top of the screen.
	 *
	 */
	@Override
	public void updatePosition() {
		super.updatePosition();
		if (this.dx == 0 && !this.reachedTop) {
			this.dy = -DX;
			this.sprite = this.sprites[1];
		}
		if (this.positionY <= 0 && !this.reachedTop) {
			this.reachedTop = true;
			this.dy = 0;

			// Starts growing fruit
			Timer timer = new Timer();
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Bubble.this.hasFruit = true;
					Bubble.this.sprite = Bubble.this.sprites[2];
				}
			}, 5000);

			// Bubble should pop after certain time period
			timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Bubble.this.die();
				}
			}, 8000);
		}
	}

	@Override
	public void addToList() {
		this.level.bubbles.add(this);
	}

	/**
	 * 
	 * Spawns fruit into the level if hero collides with the bubble and it "has
	 * fruit".
	 *
	 */
	public void collideWithHero() {
		if (this.hasFruit) {
			this.die();
			int x = this.positionX;
			int y = this.positionY;
			this.level.addCollidable(new Fruit(this.level, x, y, SIZE, SIZE, this.scoreValue));
		} else {
			this.die();
		}
	}
}
