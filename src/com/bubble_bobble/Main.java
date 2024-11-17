package com.bubble_bobble;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * Main opens a JFrame center screen and loads the main menu screen on the GUI.
 * All key listeners are attached to the frame. User input controls the flow of
 * the game from there. The frame closes the program upon exit. Game is won win
 * 10000 points have been achieved and level 9 is beaten.
 *
 * @author Joshua Eckels and Sabri Amer Created Nov 8, 2018.
 */

public class Main {
	public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int SCREEN_WIDTH = SCREEN_SIZE.width;
	public static final int SCREEN_HEIGHT = SCREEN_SIZE.height;
	public static final int TITLE_HEIGHT = 50;
	public static final int PANEL_SIZE = 700;
	public static final int PANEL_X = (SCREEN_WIDTH / 2) - (PANEL_SIZE / 2);
	public static final int PANEL_Y = (SCREEN_HEIGHT / 2) - (PANEL_SIZE / 2) - TITLE_HEIGHT;
	public static final int NUM_TILES = 20;
	public static final int TILE_SIZE = PANEL_SIZE / NUM_TILES;

	/**
	 * 
	 * Program execution begins here.
	 *
	 * @param None
	 */
	public static void main(String[] args) {
		JFrame mainFrame = new JFrame("BubbleBobble");
		mainFrame.setLocation(PANEL_X, PANEL_Y);
		mainFrame.setFocusable(true);

		final LevelComponent component = new LevelComponent();
		component.setPreferredSize(new Dimension(PANEL_SIZE, PANEL_SIZE));
		mainFrame.add(component, BorderLayout.CENTER);

		JPanel titlePanel = new JPanel();
		titlePanel.setPreferredSize(new Dimension(PANEL_SIZE, TITLE_HEIGHT));
		titlePanel.setBackground(new Color(93, 79, 56));

		JLabel title = new JLabel("BubbleBobble Arcade Game");
		title.setFont(new Font("Bookman Old Style", Font.BOLD, 30));
		title.setForeground(new Color(239, 163, 43));

		titlePanel.add(title);
		mainFrame.add(titlePanel, BorderLayout.NORTH);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setPreferredSize(new Dimension(PANEL_SIZE, TITLE_HEIGHT));
		buttonPanel.setBackground(new Color(93, 79, 56));

		mainFrame.add(buttonPanel, BorderLayout.SOUTH);

		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(1);
			}

		});
		buttonPanel.add(exitButton, BorderLayout.CENTER);

		/**
		 * 
		 * BubbleKeyListener listens to all key events, then calls the
		 * appropriate handle functions in the level component.
		 *
		 * @author Joshua Eckels and Sabri Amer Created Nov 8, 2018.
		 */
		class BubbleKeyListener implements KeyListener {

			private LevelComponent comp;

			public BubbleKeyListener(LevelComponent comp) {
				this.comp = comp;
			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					this.comp.setKeyState("Right", true);
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					this.comp.setKeyState("Left", true);
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					this.comp.setKeyState("Up", true);
					this.comp.handleMoveHero();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				char key = e.getKeyChar();
				if (key == 'u' && !this.comp.checkGameOver()) {
					this.comp.changeLevel(1);
				}
				if (key == 'd' && !this.comp.checkGameOver()) {
					this.comp.changeLevel(-1);
				}
				if (key == 'p') {
					this.comp.currentLevel.changePause();
				}
				if (key == 'm') {
					this.comp.changeLevel(0);
				}
				if (e.getKeyCode() == KeyEvent.VK_UP) {
					this.comp.setKeyState("Up", false);
					this.comp.handleMoveHero();
				}
				if (e.getKeyCode() == KeyEvent.VK_LEFT) {
					this.comp.setKeyState("Left", false);
				}
				if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
					this.comp.setKeyState("Right", false);
				}
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					this.comp.handleBubble();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {
				// Not used
			}
		}

		component.loadLevel();
		mainFrame.addKeyListener(new BubbleKeyListener(component));
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.pack();
		mainFrame.setVisible(true);
	}
}
