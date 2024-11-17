package com.bubble_bobble;

import java.awt.Color;

import javax.swing.ImageIcon;

/**
 * 
 * Fireball is shot by an Incendo monster and travels a certain distance before
 * disappearing. Fireballs collide with the hero in the same way monsters do.
 *
 * @author Joshua Eckels and Sabri Amer Created Nov 9, 2018.
 */
public class Fireball extends Projectile {

	public Fireball(Level level, int x, int y, int dir) {
		super(level, x, y, dir);
		this.color = Color.RED;
		if (this.direction == -1) {
			this.sprite = (new ImageIcon(getClass().getClassLoader().getResource("sprites/fireball_left.png"))).getImage();
		} else {
			this.sprite = (new ImageIcon(getClass().getClassLoader().getResource("sprites/fireball_right.png"))).getImage();
		}
	}

	@Override
	public void updatePosition() {
		super.updatePosition();
		if (this.dx == 0) {
			this.die();
		}
	}

	@Override
	public void addToList() {
		this.level.fireballs.add(this);
	}
}
