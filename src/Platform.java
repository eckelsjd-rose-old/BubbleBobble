import java.awt.Graphics2D;
import java.awt.Image;

/**
 * 
 * Platforms act as a surface for other Collidables to walk on and jump through.
 *
 * @author Joshua Eckels and Sabri Amer Created Nov 9, 2018.
 */
public class Platform extends Collidable {

	public Platform(Level level, int x, int y, int width, int height, Image sprite) {
		super(level, x, y, width, height);
		this.sprite = sprite;
	}

	@Override
	public void updatePosition() {
		// Platform does not change position
	}

	@Override
	public void draw(Graphics2D g) {
		g.drawImage(this.sprite, this.positionX, this.positionY, null);
	}

	@Override
	public void addToList() {
		this.level.platforms.add(this);
	}

}
