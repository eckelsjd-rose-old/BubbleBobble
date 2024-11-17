package com.bubble_bobble;

import java.awt.Color;
import java.awt.Image;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;

/**
 * 
 * Fruit objects can be picked up by the hero and increases the current score by
 * its unique score value. There are 4 types of fruit:
 * 
 * banana -> spawned by bubbles (10 points) 
 * eggplant -> spawned by BubbleBuster upon death (500 points) 
 * watermelon -> spawned by Incendo upon death (3000 points) 
 * Rascal -> character spawned upon completing the game
 *
 * @author Joshua Eckels and Sabri Amer Created Nov 9, 2018.
 */
public class Fruit extends Collidable {

	boolean heroCanTouch;

	public Fruit(Level level, int x, int y, int width, int height, int scoreValue) {
		super(level, x, y, width, height);
		this.scoreValue = scoreValue;
		this.color = Color.GREEN;
		this.heroCanTouch = false;

		this.sprites = new Image[4];
		this.sprites[0] = (new ImageIcon(getClass().getClassLoader().getResource("sprites/banana.png"))).getImage();
		this.sprites[1] = (new ImageIcon(getClass().getClassLoader().getResource("sprites/eggplant.png"))).getImage();
		this.sprites[2] = (new ImageIcon(getClass().getClassLoader().getResource("sprites/watermelon.png"))).getImage();
		this.sprites[3] = (new ImageIcon(getClass().getClassLoader().getResource("sprites/ur_boi_rascal.gif"))).getImage();
		if (scoreValue == 10) {
			this.sprite = this.sprites[0];
		} else if (scoreValue == 500) {
			this.sprite = this.sprites[1];
		} else if (scoreValue == 3000) {
			this.sprite = this.sprites[2];
		} else {
			this.sprite = this.sprites[3];
		}

		Timer timer = new Timer();
		// allow grace period for fruit to fall before colliding with hero
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				Fruit.this.heroCanTouch = true;
			}
		}, 500);
	}

	@Override
	public void updatePosition() {
		this.isFalling = (this.vy < 1) ? false : true;
		this.vy = this.vy + GRAVITY * DT;
		this.positionY += this.vy * DT;
	}

	@Override
	public void addToList() {
		this.level.fruits.add(this);
	}

	public void collideWithHero() {
		if (this.heroCanTouch) {
			// only increase the score if the level is playable
			if (this.level.isPlayable()) {
				this.setScore();
			}
			this.die();
		}
	}
}
