
/*
 * File: ProgramHierarchy.java
 * Name: irakli khutsishvili
 * Section Leader: giorgi megrelidze
 * ---------------------------
 * This file is the starter file for the ProgramHierarchy problem.
 */

import acm.graphics.*;
import acm.program.*;

public class ProgramHierarchy extends GraphicsProgram {
	// value of the boxes width in pixels
	private static final int RECTS_WIDTH = 150;
	// value of the boxes height in pixels
	private static final int RECTS_HEIGHT = 50;
	// value of the distance between boxes in pixels
	private static final int GAP = 20;

	public void run() {
		drawDiagram();
		writeSomething("Program", 0, -RECTS_HEIGHT);
		writeSomething("GrapicsProgram", -(RECTS_WIDTH + GAP), RECTS_HEIGHT);
		writeSomething("ConsoleProgram", 0, RECTS_HEIGHT);
		writeSomething("DialogProgram", RECTS_WIDTH + GAP, RECTS_HEIGHT);

	}

	/*
	 * this method draws a diagram and has 3 steps
	 */
	private void drawDiagram() {
		drawTitlesBox();
		drawConnections();
		drawContentsBoxes();

	}

	/*
	 * first step is to draw the title box and this method does it
	 */
	private void drawTitlesBox() {
		GRect title = new GRect(RECTS_WIDTH, RECTS_HEIGHT);
		int x = (getWidth() - RECTS_WIDTH) / 2;
		int y = (getHeight() - RECTS_HEIGHT) / 2 - RECTS_HEIGHT;
		add(title, x, y);
	}

	/*
	 * second step draws lines that connects title box to other boxes
	 */
	private void drawConnections() {
		for (int i = 0; i < 3; i++) {
			int x1 = getWidth() / 2;
			int y1 = (getHeight() - RECTS_HEIGHT) / 2;
			int x2 = getWidth() / 2 - (RECTS_WIDTH + GAP) + (RECTS_WIDTH + GAP) * i;
			int y2 = (getHeight() + RECTS_HEIGHT) / 2;
			GLine line = new GLine(x1, y1, x2, y2);
			add(line);
		}

	}

	/*
	 * third step draws content boxes
	 */
	private void drawContentsBoxes() {
		for (int i = 0; i < 3; i++) {
			int y = (getHeight() + RECTS_HEIGHT) / 2;
			int x = (getWidth() - RECTS_WIDTH) / 2 - (RECTS_WIDTH + GAP) + (RECTS_WIDTH + GAP) * i;
			GRect contentsBox = new GRect(RECTS_WIDTH, RECTS_HEIGHT);
			add(contentsBox, x, y);

		}
	}

	/*
	 * we give this method text and the coordinates of the texts center from the
	 * center of the console and it writes it in the middle of the box
	 */
	private void writeSomething(String a, double b, double c) {
		GLabel text = new GLabel(a);
		double x = (getWidth() - text.getWidth()) / 2 + b;
		double y = getHeight() / 2 + text.getAscent() / 2 + c;
		add(text, x, y);

	}
}
