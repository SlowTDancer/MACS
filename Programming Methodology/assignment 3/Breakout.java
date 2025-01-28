
/*
 * File: Breakout.java
 * -------------------
 * Name:irakli khutsishvili
 * Section Leader:giorgi megrelidze
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

	/** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = 400;
	public static final int APPLICATION_HEIGHT = 600;

	/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;

	/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

	/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

	/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

	/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

	/** Separation between bricks */
	private static final int BRICK_SEP = 4;

	/** Width of a brick */
	private static final int BRICK_WIDTH = (WIDTH - (NBRICKS_PER_ROW - 1) * BRICK_SEP) / NBRICKS_PER_ROW;

	/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

	/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

	/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

	/** Number of turns */
	private static final int NTURNS = 3;

	/** pause time */
	private static final int DELAY = 10;

	/** Diameter of the ball in pixels */
	private static final int BALL_DIAMETER = BALL_RADIUS * 2;

	/** randomiser */
	private RandomGenerator rgen = RandomGenerator.getInstance();

	/* Method: run() */
	/** Runs the Breakout program. */
	public void run() {
	}

	// * initiates everything */
	public void init() {
		setSize(WIDTH, HEIGHT);
		addMouseListeners();
		sortBricks();
		paddle = drawPaddle();
		moveBall();

	}

	/** velocity at X axis and at Y axis */
	private double vx, vy;

	/** balls name */
	private GOval ball;

	/** paddles name */
	private GRect paddle;

	/** bricks name */
	private GRect brick;

	/** how many times we can fail */
	private int life = NTURNS;

	/** starting value of wasted lives */
	private int livesWasted = 0;

	/** starting value of destroyed bricks */
	private int destroyedBricks = 0;

	/** boolean that checks if brick has been broken before or not */
	private boolean remove = true;

	/** sort bricks in the rows */
	private void sortBricks() {
		for (int i = 0; i < NBRICK_ROWS; i++) {
			int y = BRICK_Y_OFFSET + i * (BRICK_HEIGHT + BRICK_SEP);
			if (i % NBRICK_ROWS < NBRICK_ROWS / 5) {
				drawBricks(y, Color.RED);
			}
			if (NBRICK_ROWS / 5 <= i % NBRICK_ROWS && i % NBRICK_ROWS < 2 * NBRICK_ROWS / 5) {
				drawBricks(y, Color.ORANGE);
			}
			if (2 * NBRICK_ROWS / 5 <= i % NBRICK_ROWS && i % NBRICK_ROWS < 3 * NBRICK_ROWS / 5) {
				drawBricks(y, Color.YELLOW);
			}
			if (3 * NBRICK_ROWS / 5 <= i % NBRICK_ROWS && i % NBRICK_ROWS < 4 * NBRICK_ROWS / 5) {
				drawBricks(y, Color.GREEN);
			}
			if (4 * NBRICK_ROWS / 5 <= i % NBRICK_ROWS && i % NBRICK_ROWS < 5 * NBRICK_ROWS / 5) {
				drawBricks(y, Color.CYAN);
			}
		}
	}

	/** draws bricks in one row */
	private void drawBricks(double y, Color c) {
		for (int i = 0; i < NBRICKS_PER_ROW; i++) {
			int x0 = WIDTH / 2 - NBRICKS_PER_ROW * BRICK_WIDTH / 2 - (NBRICKS_PER_ROW - 1) * BRICK_SEP / 2;
			int x = x0 + i * (BRICK_WIDTH + BRICK_SEP);
			brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
			brick.setFilled(true);
			brick.setFillColor(c);
			brick.setColor(c);
			add(brick, x, y);
		}
	}

	/** draws paddle */
	private GRect drawPaddle() {
		int x = (WIDTH - PADDLE_WIDTH) / 2;
		int y = HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
		paddle.setColor(Color.BLACK);
		add(paddle, x, y);
		return paddle;
	}

	/** forces paddle to change location to mouse coordinates */
	public void mouseMoved(MouseEvent event) {
		int x = event.getX() - PADDLE_WIDTH / 2;
		int y = HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT;
		if (x >= 0 && x <= WIDTH - PADDLE_WIDTH) {
			paddle.setLocation(x, y);
		}
	}

	/** draws ball */
	private GOval drawBall() {
		int x = WIDTH / 2 - BALL_RADIUS;
		int y = HEIGHT / 2 - BALL_RADIUS;
		ball = new GOval(BALL_DIAMETER, BALL_DIAMETER);
		ball.setFilled(true);
		ball.setFillColor(Color.BLACK);
		ball.setColor(Color.BLACK);
		add(ball, x, y);
		return ball;
	}

	/** moves ball and destroyes bricks */
	private void moveBall() {
		ball = drawBall();
		vx = rgen.nextDouble(1.0, 3.0);
		vy = 3;
		if (rgen.nextBoolean(0.5)) {
			vx = -vx;
		}
		waitForClick();
		int totalBricks = NBRICKS_PER_ROW * NBRICK_ROWS;
		for (int i = 0; i < NTURNS; i++) {
			while (livesWasted < life) {
				double x = ball.getX();
				double y = ball.getY();
				double currentVx;
				double currentVy = vy;
				/**
				 * code from 192-199 moves ball and checks if it can move vx or vy, and if it
				 * can't it changes velocity according to distance
				 */
				if (vx > 0) {
					currentVx = Math.min(vx, WIDTH - x - BALL_DIAMETER);
				} else {
					currentVx = Math.max(vx, -x);
				}
				if (vy < 0) {
					currentVy = Math.max(vy, -y);
				}
				ball.move(currentVx, currentVy);
				x = ball.getX();
				y = ball.getY();
				/** code from 207-215 checks collisions on the wall */
				if (x <= 0 || x >= WIDTH - BALL_DIAMETER) {
					vx = -vx;
				}
				if (y <= 0) {
					vy = -vy;
				}
				if (y >= HEIGHT - BALL_DIAMETER) {
					break;
				}
				GObject collider = getCollidingObject();
				/**
				 * if collider is the paddle, when it reflects on y and ignores if collions
				 * happened from the x, because player will still lose,so it doesn't matter
				 */
				if (collider == paddle) {
					ifPaddleReflect(y);
				}
				/**
				 * if the collider is a brick, when it removes it and reflects it. if ball
				 * collided with the brick from y side it reflects on y and if from x, it
				 * reflects on x. it also count hom many bricks where destroyed
				 */
				if (collider == brick) {
					remove = true;
					collisionAtYAxis(x, y - 1, x + BALL_RADIUS, y - 1);
					collisionAtYAxis(x + BALL_DIAMETER, y - 1, x + BALL_RADIUS, y - 1);
					collisionAtYAxis(x, y + BALL_DIAMETER + 1, x + BALL_RADIUS, y + BALL_DIAMETER + 1);
					collisionAtYAxis(x + BALL_DIAMETER, y + BALL_DIAMETER + 1, x + BALL_RADIUS, y + BALL_DIAMETER + 1);
					collisionAtXAxis(x - 1, y, x - 1, y + BALL_RADIUS);
					collisionAtXAxis(x - 1, y + BALL_DIAMETER, x - 1, y + BALL_RADIUS);
					collisionAtXAxis(x + BALL_DIAMETER + 1, y, x + BALL_DIAMETER + 1, y + BALL_RADIUS);
					collisionAtXAxis(x + BALL_DIAMETER + 1, y + BALL_DIAMETER, x + BALL_DIAMETER + 1, y + BALL_RADIUS);
				}
				/** if every brick was destroyed it removes everything and print YOU WIN!!! */
				if (destroyedBricks == totalBricks) {
					removeAll();
					winner();
					break;
				}
				pause(DELAY);
			}
			remove(ball);
			if (destroyedBricks == totalBricks) {
				break;
			}
			livesWasted++;
			ball = drawBall();
		}
		/** counts wasted lives */
		if (livesWasted == life) {
			removeAll();
			loser();
		}
	}

	/**
	 * this method checks if anything is on the points and tells what that object is
	 */
	private GObject getCollidingObject() {
		double x = ball.getX();
		double y = ball.getY();
		if (getElementAt(x, y) == paddle) {
			return paddle;
		}
		if (getElementAt(x, y + BALL_DIAMETER) == paddle) {
			return paddle;
		}
		if (getElementAt(x + BALL_DIAMETER, y) == paddle) {
			return paddle;
		}
		if (getElementAt(x + BALL_DIAMETER, y + BALL_DIAMETER) == paddle) {
			return paddle;
		}
		if (getElementAt(x, y) != null) {
			return brick;
		}
		if (getElementAt(x, y + BALL_DIAMETER) != null) {
			return brick;
		}
		if (getElementAt(x + BALL_DIAMETER, y) != null) {
			return brick;
		}
		if (getElementAt(x + BALL_DIAMETER, y + BALL_DIAMETER) != null) {
			return brick;
		}
		return null;
	}

	/**
	 * it reflects on y and ignores if collions happened from the x, because player
	 * will still lose,so it doesn't matter
	 */
	private void ifPaddleReflect(double y) {
		if (y + BALL_DIAMETER < HEIGHT - PADDLE_Y_OFFSET - PADDLE_HEIGHT + 3) {
			vy = -vy;
		}
	}

	/**
	 * this method checks if ball collided with a brick, teleports it a little cause
	 * it might stuck and break other bricks too. also counts destroyed
	 * bricks,reflects ball in y axis and if the collision happened it tells other
	 * functions that it already happened
	 */
	private void collisionAtYAxis(double x, double y, double a, double b) {
		if (getElementAt(x, y) != null && remove && getElementAt(a, b) != null) {
			remove(getElementAt(a, b));
			vy = -vy;
			ball.move(vy, 0);
			remove = false;
			destroyedBricks++;
		}
	}

	/**
	 * this method checks if ball collided with a brick, teleports it a little cause
	 * it might stuck and break other bricks too. also counts destroyed
	 * bricks,reflects ball in x axis and if the collision happened it tells other
	 * functions that it already happened
	 */
	private void collisionAtXAxis(double x, double y, double a, double b) {
		if (getElementAt(x, y) != null && remove && getElementAt(a, b) != null) {
			remove(getElementAt(a, b));
			vx = -vx;
			ball.move(0, vx);
			remove = false;
			destroyedBricks++;
		}
	}

	/** writes something if you win */
	private void winner() {
		GLabel winner = new GLabel("YOU WIN!!!");
		double x = (getWidth() - winner.getWidth()) / 2;
		double y = getHeight() / 2 + winner.getAscent() / 2;
		add(winner, x, y);
	}

	/** writes something if you lose */
	private void loser() {
		GLabel loser = new GLabel("YOU LOSE :(");
		double x = (getWidth() - loser.getWidth()) / 2;
		double y = getHeight() / 2 + loser.getAscent() / 2;
		add(loser, x, y);
	}
}
