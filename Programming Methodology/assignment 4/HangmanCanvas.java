
/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {

	/** Resets the display so that only the scaffold appears */
	public void reset() {
		hiddenWord = new GLabel("");
		wrongGuesses = "";
		count = 0;
		removeAll();
		int x = getWidth() / 2 - (BEAM_LENGTH + UPPER_ARM_LENGTH) / 2;
		int y = getHeight() / 2 - SCAFFOLD_HEIGHT / 2;
		drawScaffold(x, y);
		drawBeam(x, y);
		drawRope(x, y);
	}

	/**
	 * Updates the word on the screen to correspond to the current state of the
	 * game. The argument string shows what letters have been guessed so far;
	 * unguessed letters are indicated by hyphens.
	 */
	public void displayWord(String word) {
		int x = getWidth() / 2 - (BEAM_LENGTH + UPPER_ARM_LENGTH) / 2 + BEAM_LENGTH;
		int y = getHeight() / 2 + SCAFFOLD_HEIGHT / 2;
		remove(hiddenWord);
		hiddenWord = new GLabel(word);
		hiddenWord.setFont("-20");
		add(hiddenWord, x - hiddenWord.getWidth() / 2, y + hiddenWord.getHeight());
	}

	/**
	 * Updates the display to correspond to an incorrect guess by the user. Calling
	 * this method causes the next body part to appear on the scaffold and adds the
	 * letter to the list of incorrect guesses that appears at the bottom of the
	 * window.
	 */
	public void noteIncorrectGuess(char letter) {
		count++;
		wrongGuesses = wrongGuesses + letter;
		int x = getWidth() / 2 - (BEAM_LENGTH + UPPER_ARM_LENGTH) / 2 + BEAM_LENGTH;
		int y = getHeight() / 2 - SCAFFOLD_HEIGHT / 2 + ROPE_LENGTH;
		if (count == 1) {
			drawHead(x, y);
		}
		if (count == 2) {
			drawBody(x, y);
		}
		if (count == 3) {
			drawLeftHand(x, y);
		}
		if (count == 4) {
			drawRightHand(x, y);
		}
		if (count == 5) {
			drawLeftLeg(x, y);
		}
		if (count == 6) {
			drawRightLeg(x, y);
		}
		if (count == 7) {
			drawLeftToe(x, y);
		}
		if (count == 8) {
			drawRightToe(x, y);
		}
		wrongGuesses(wrongGuesses);
	}

	/*
	 * draws scaffold.
	 */
	private void drawScaffold(int x, int y) {
		GLine scaffold = new GLine(x, y, x, y + SCAFFOLD_HEIGHT);
		add(scaffold);
	}
	/*
	 * draws beam.
	 */

	private void drawBeam(int x, int y) {
		GLine beam = new GLine(x, y, x + BEAM_LENGTH, y);
		add(beam);
	}
	/*
	 * draws rope.
	 */

	private void drawRope(int x, int y) {
		GLine rope = new GLine(x + BEAM_LENGTH, y, x + BEAM_LENGTH, y + ROPE_LENGTH);
		add(rope);
	}

	/*
	 * draws head.
	 */
	private void drawHead(int x, int y) {
		GOval head = new GOval(HEAD_DIAMETER, HEAD_DIAMETER);
		add(head, x - HEAD_RADIUS, y);
	}
	/*
	 * draws body.
	 */

	private void drawBody(int x, int y) {
		GLine body = new GLine(x, y + HEAD_DIAMETER, x, y + HEAD_DIAMETER + BODY_LENGTH);
		add(body);
	}

	/*
	 * draws left hand.
	 */
	private void drawLeftHand(int x, int y) {
		int y0 = y + HEAD_DIAMETER + ARM_OFFSET_FROM_HEAD;
		GLine upperLeftHand = new GLine(x, y0, x - UPPER_ARM_LENGTH, y0);
		GLine lowerLeftHand = new GLine(x - UPPER_ARM_LENGTH, y0, x - UPPER_ARM_LENGTH, y0 + LOWER_ARM_LENGTH);
		add(upperLeftHand);
		add(lowerLeftHand);
	}

	/*
	 * draws right hand.
	 */

	private void drawRightHand(int x, int y) {
		int y0 = y + HEAD_DIAMETER + ARM_OFFSET_FROM_HEAD;
		GLine upperRightHand = new GLine(x, y0, x + UPPER_ARM_LENGTH, y0);
		GLine lowerRightHand = new GLine(x + UPPER_ARM_LENGTH, y0, x + UPPER_ARM_LENGTH, y0 + LOWER_ARM_LENGTH);
		add(upperRightHand);
		add(lowerRightHand);
	}
	/*
	 * draws left leg.
	 */

	private void drawLeftLeg(int x, int y) {
		int y0 = y + HEAD_DIAMETER + BODY_LENGTH;
		GLine leftHip = new GLine(x, y0, x - HIP_WIDTH, y0);
		GLine leftLeg = new GLine(x - HIP_WIDTH, y0, x - HIP_WIDTH, y0 + LEG_LENGTH);
		add(leftHip);
		add(leftLeg);
	}

	/*
	 * draws right leg.
	 */
	private void drawRightLeg(int x, int y) {
		int y0 = y + HEAD_DIAMETER + BODY_LENGTH;
		GLine rightHip = new GLine(x, y0, x + HIP_WIDTH, y0);
		GLine rightLeg = new GLine(x + HIP_WIDTH, y0, x + HIP_WIDTH, y0 + LEG_LENGTH);
		add(rightHip);
		add(rightLeg);
	}

	/*
	 * draws left toe.
	 */
	private void drawLeftToe(int x, int y) {
		int y0 = y + HEAD_DIAMETER + BODY_LENGTH + LEG_LENGTH;
		int x0 = x - HIP_WIDTH;
		GLine leftToe = new GLine(x0, y0, x0 - FOOT_LENGTH, y0);
		add(leftToe);
	}

	/*
	 * draws left toe.
	 */
	private void drawRightToe(int x, int y) {
		int y0 = y + HEAD_DIAMETER + BODY_LENGTH + LEG_LENGTH;
		int x0 = x + HIP_WIDTH;
		GLine rightToe = new GLine(x0, y0, x0 + FOOT_LENGTH, y0);
		add(rightToe);
	}
	/*
	 * draws players wrong guesses.
	 */

	private void wrongGuesses(String x) {
		remove(wrongsList);
		wrongsList = new GLabel(x);
		add(wrongsList, 0, getHeight() - wrongsList.getAscent());

	}

	/*
	 * some instance variables.
	 */
	private GLabel hiddenWord;
	private String wrongGuesses;
	private GLabel wrongsList = new GLabel("");
	private int count;
	/* Constants for the simple version of the picture (in pixels) */

	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int HEAD_DIAMETER = 2 * HEAD_RADIUS;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;
}