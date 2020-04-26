import java.awt.Image;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 
 * The Level class stores all of the collidable objects in a given level, as
 * well as the state of the current level. The level tells all of its
 * collidables to update their positions. The level also checks for collisions
 * between all of its collidables.
 *
 * @author Joshua Eckels and Sabri Amer. Created Nov 9, 2018.
 */
public class Level {

	// Level states and values
	protected Image screen;
	protected boolean isReadyToChange = false;
	protected boolean gameOver = false;
	protected int score;
	private Timer timer = new Timer();
	private Point2D heroLocation = new Point2D.Double();

	// Lists for game objects
	protected ArrayList<Collidable> collidables = new ArrayList<>();
	protected ArrayList<Collidable> toAdd = new ArrayList<>();
	protected ArrayList<Collidable> toRemove = new ArrayList<>();
	protected ArrayList<Platform> platforms = new ArrayList<>();
	protected ArrayList<Bubble> bubbles = new ArrayList<>();
	protected ArrayList<Fireball> fireballs = new ArrayList<>();
	protected ArrayList<Fruit> fruits = new ArrayList<>();
	protected ArrayList<Monster> monsters = new ArrayList<>();
	protected Hero hero;

	// For use by game over menu
	private static Random rand = new Random();
	private int index = rand.nextInt(10);
	private Tuple quotes[];
	protected String quote = "";
	protected String author = "";
	protected Rectangle2D quoteBox = new Rectangle2D.Double(0, 350, 700, 50);
	protected Rectangle2D authorBox = new Rectangle2D.Double(250, 400, 400, 50);

	public Level() {

		// For use by game over menu
		Tuple t1 = new Tuple("I'd rather be skiing.", "-Stan Laurel");
		Tuple t2 = new Tuple("Oh Wow. Oh Wow. Oh Wow.", "-Steve Jobs");
		Tuple t3 = new Tuple("Now is not the time for making new enemies.", "-Voltaire");
		Tuple t4 = new Tuple("Hey, fellas, how about this for a headline tomorrow? 'French Fries'",
				"-James French, at the electric chair");
		Tuple t5 = new Tuple("Dream on.", "-Aerosmith");
		Tuple t6 = new Tuple("They couldn't hit an elephant at this dist . . . ",
				"-Gen. Sedgwick, being shot mid-sentence");
		Tuple t7 = new Tuple("I must go in, for the fog is rising.", "-Emily Dickinson");
		Tuple t8 = new Tuple("I found Rome of clay; I leave it to you of marble", "-Augustus Caesar");
		Tuple t9 = new Tuple("I will hear in heaven.", "Ludwig van Beethoven");
		Tuple t10 = new Tuple("What are you going to do, shoot me?", "-Quote from man shot");
		Tuple newQuotes[] = { t1, t2, t3, t4, t5, t6, t7, t8, t9, t10 };
		this.quotes = newQuotes;
	}

	// For use by game over screen, courtesy of Anand Desai
	class Tuple {

		String tQuote;
		String tAuthor;

		Tuple(String q, String a) {
			this.tQuote = q;
			this.tAuthor = a;
		}
	}

	/**
	 * 
	 * Adds the given collidable object to the level.
	 *
	 * @param c
	 */
	public synchronized void addCollidable(Collidable c) {
		this.toAdd.add(c);
		c.addToList();
	}

	/**
	 * 
	 * Removes the given collidable object from the level.
	 *
	 * @param c
	 */
	public synchronized void removeCollidable(Collidable c) {
		this.toRemove.add(c);
	}

	/**
	 * 
	 * Changes the pause state of all of the level's collidable objects.
	 *
	 */
	public void changePause() {
		for (Collidable c : this.collidables) {
			c.changePause();
		}
	}

	/**
	 * 
	 * Sets the "screen" of the level to the given image. For use by the main
	 * menu, game won screen, and the game over screen. Adds quotes to the
	 * screen if it is the game over screen.
	 * 
	 *
	 * @param image
	 * @param isGameScreen
	 */
	public void setLevelScreen(Image image, boolean isGameScreen) {
		this.screen = image;
		if (isGameScreen) {
			this.quote = this.quotes[this.index].tQuote;
			this.author = this.quotes[this.index].tAuthor;
		}
	}

	/**
	 * 
	 * Time passed is called on each clock tick of the LevelComponent's timer.
	 * Time passed updates the position of all of the current level's
	 * collidables. Calls collision detection method.
	 *
	 */
	public synchronized void timePassed() {

		// Update positions
		for (Monster m : this.monsters) {
			this.heroLocation.setLocation(this.hero.positionX, this.hero.positionY);
			m.updateHeroLocation(this.heroLocation);
		}
		for (Collidable c : this.collidables) {
			if (!c.isPaused) {
				c.updatePosition();
			}
		}

		// Update lists
		this.collidables.removeAll(this.toRemove);
		this.monsters.removeAll(this.toRemove);
		this.fireballs.removeAll(this.toRemove);
		this.bubbles.removeAll(this.toRemove);
		this.fruits.removeAll(this.toRemove);

		this.toRemove.clear();
		this.collidables.addAll(this.toAdd);
		this.toAdd.clear();

		// Handle collisions
		handleCheckCollision();
	}

	/**
	 * 
	 * Checks if the current level is a "playable" level. (i.e. not the main
	 * menu, game over, or game won screens). Only main menu, game over, and
	 * game won screens have a screen that is not null.
	 *
	 * @return true -> if level is playable. false -> level is not playable
	 */
	public boolean isPlayable() {
		return this.screen == null;
	}

	/**
	 * 
	 * Changes the state of the level to reflect game progress. If there are no
	 * monsters left in a level, the level is ready to change. If the hero is
	 * out of lives, gameOver is set to true.
	 *
	 */
	public void handleCheckGameState() {
		if (this.monsters.isEmpty()) {
			this.timer.schedule(new TimerTask() {
				@Override
				public void run() {
					Level.this.isReadyToChange = true;
				}
			}, 5000);
		}

		if (this.hero.lives == 0) {
			this.gameOver = true;
		}
	}

	// Collision detection
	public boolean overlapping(Collidable c1, Collidable c2) {
		return c1.getShape().intersects(c2.getShape());
	}

	/**
	 * 
	 * Iterates through each necessary set of collidables to check for
	 * collisions. There are 4 main types of collisions: platform, enemy,
	 * bubble, and fruit collisions.
	 *
	 */
	public void handleCheckCollision() {

		// platform collisions
		for (Platform p : this.platforms) {
			if (overlapping(p, this.hero)) {
				this.hero.collideWithPlatform(p);
			}
			for (Monster m : this.monsters) {
				if (overlapping(p, m)) {
					m.collideWithPlatform(p);
				}
			}
			for (Fruit f : this.fruits) {
				if (overlapping(p, f)) {
					f.collideWithPlatform(p);
				}
			}
		}

		// enemy collisions
		for (Monster m : this.monsters) {
			if (overlapping(m, this.hero)) {
				if (m.isBubbled) {
					this.addCollidable(
							new Fruit(this, m.positionX, m.positionY, Collidable.SIZE, Collidable.SIZE, m.scoreValue));
					m.die();
				} else {
					this.hero.collideWithEnemy();
				}
			}
		}
		for (Fireball f : this.fireballs) {
			if (overlapping(f, this.hero)) {
				this.hero.collideWithEnemy();
			}
		}

		// bubble collisions
		for (Bubble b : this.bubbles) {
			if (overlapping(b, this.hero)) {
				b.collideWithHero();
				if (this.isPlayable()) {
					b.setScore();
				}
			}
			for (Monster m : this.monsters) {
				// bubbles only collide with monsters if not moving upwards
				if (overlapping(b, m) && b.dx != 0) {
					b.die();
					m.collideWithBubble();
				}
			}
		}

		// fruit collisions
		for (Fruit f : this.fruits) {
			if (overlapping(f, this.hero)) {
				f.collideWithHero();
			}
		}
	}
}
