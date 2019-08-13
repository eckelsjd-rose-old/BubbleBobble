import java.awt.Color;

import javax.swing.ImageIcon;

/**
 * 
 * Incendo extends the Monster class. In addition to all the abilites of a
 * BubbleBuster, and Incendo periodically shoots fireball objects in the current
 * direction.
 *
 * @author Joshua Eckels and Sabri Amer Created Nov 9, 2018.
 */
public class Incendo extends Monster {

	private static final double PROB_OF_FIREBALL = 0.001;

	public Incendo(Level level, int x, int y, int width, int height) {
		super(level, x, y, width, height);
		this.color = Color.BLUE;
		this.scoreValue = 3000;
		this.sprites[0] = (new ImageIcon("src/sprites/incendo_left.gif")).getImage();
		this.sprites[1] = (new ImageIcon("src/sprites/incendo_right.gif")).getImage();
		this.sprites[2] = (new ImageIcon("src/sprites/incendo_bubble.gif")).getImage();
		this.sprite = (this.direction == 1) ? this.sprites[1] : this.sprites[0];
	}

	@Override
	public void updatePosition() {
		super.updatePosition();
		if (Math.random() < PROB_OF_FIREBALL && !this.isBubbled) {
			this.shootFireball();
		}
	}

	@Override
	public void addToList() {
		this.level.monsters.add(this);
	}

	/**
	 * 
	 * Shoots a fireball.
	 *
	 */
	public void shootFireball() {
		Fireball fireball = new Fireball(this.level, this.positionX, this.positionY, this.direction);
		this.level.addCollidable(fireball);
	}
}
