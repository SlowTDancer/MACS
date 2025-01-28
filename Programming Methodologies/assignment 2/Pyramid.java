
/*
 * File: Pyramid.java
 * Name: irakli khutsishvili
 * Section Leader: giorgi megrelidze
 * ------------------
 * This file is the starter file for the Pyramid problem.
 * It includes definitions of the constants that match the
 * sample run in the assignment, but you should make sure
 * that changing these values causes the generated display
 * to change accordingly.
 */

import acm.graphics.*;
import acm.program.*;

public class Pyramid extends GraphicsProgram {

	/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

	/** Width of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

	/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 14;

	public void run() {
		buildPyramid();
	}

	/*
	 * this method starts putting bricks from the top of the pyramid and goes down.
	 * also if we are on the n row, it puts n bricks in that row
	 */
	private void buildPyramid() {
		for (int i = 0; i < BRICKS_IN_BASE; i++) {
			int y = getHeight() - BRICK_HEIGHT * BRICKS_IN_BASE + BRICK_HEIGHT * i;
			for (int j = 0; j <= i; j++) {
				GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
				int x = getWidth() / 2 - BRICK_WIDTH / 2 - BRICK_WIDTH * i / 2 + BRICK_WIDTH * j;
				add(brick, x, y);
			}
		}
	}
}
