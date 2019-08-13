import java.awt.Color;

import javax.swing.ImageIcon;

/**
 * 
 * BubbleBuster is the most basic kind of monster in the game. Follows all the
 * methods in Monster superclass.
 *
 * @author Joshua Eckels and Sabri Amer Created Nov 9, 2018.
 */
public class BubbleBuster extends Monster {

	public BubbleBuster(Level level, int x, int y, int width, int height) {
		super(level, x, y, width, height);
		this.color = Color.MAGENTA;
		this.scoreValue = 500;
		this.sprites[0] = (new ImageIcon("src/sprites/bubble_buster_left.gif")).getImage();
		this.sprites[1] = (new ImageIcon("src/sprites/bubble_buster_right.gif")).getImage();
		this.sprites[2] = (new ImageIcon("src/sprites/bubble_buster_bubble.gif")).getImage();
		this.sprite = (this.direction == 1) ? this.sprites[1] : this.sprites[0];
	}

	@Override
	public void addToList() {
		this.level.monsters.add(this);
	}
}
