
/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import java.util.Arrays;

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {

	public static void main(String[] args) {
		new Yahtzee().start(args);
	}

	public void run() {
		IODialog dialog = getDialog();
		enterNumOfPlayers(dialog);
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		createArrays();
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
	}

	private void playGame() {
		for (int i = 0; i < N_SCORING_CATEGORIES; i++) {
			for (int player = 1; player <= nPlayers; player++) {
				display.printMessage(
						playerNames[player - 1] + "'s turn! Click \"Roll Dice" + " button to roll the dice");
				display.waitForPlayerToClickRoll(player);
				rollDices();
				display.displayDice(rolledDices);
				retry(player);
				chooseCategory(player);
				int score = writeScore(chosenCategory);
				score(chosenCategory, player, score);
			}
		}
		winner();
	}

	/*
	 * enter how many players we have.
	 */
	private void enterNumOfPlayers(IODialog dialog) {
		while (true) {
			nPlayers = dialog.readInt("Enter number of players");
			if (nPlayers <= MAX_PLAYERS && nPlayers >= 1) {
				break;
			}
		}
	}

	/*
	 * creates all the arrays, which will help us to calculate.
	 */
	private void createArrays() {
		upperScore = new int[nPlayers];
		lowerScore = new int[nPlayers];
		totalScore = new int[nPlayers];
		usedCategory = new boolean[nPlayers][TOTAL];
		Bonus = new boolean[nPlayers];
	}

	/*
	 * rolls dices.
	 */
	private int[] rollDices() {
		for (int i = 0; i < N_DICE; i++) {
			rolledDices[i] = rgen.nextInt(1, 6);
		}
		return rolledDices;

	}

	/*
	 * helps player to select dices and if dice is selected rolls it.
	 */
	private void diceSelection() {
		display.waitForPlayerToSelectDice();
		for (int j = 0; j < N_DICE; j++) {
			if (display.isDieSelected(j)) {
				rolledDices[j] = rgen.nextInt(1, 6);
			}
		}
	}

	/*
	 * this method allows player to reroll dices two more times.
	 */
	private void retry(int player) {
		for (int j = 0; j < 2; j++) {
			display.printMessage(playerNames[player - 1] + " choose which dices you want to reroll");
			diceSelection();
			display.displayDice(rolledDices);
		}
	}

	/*
	 * this method allows player to choose free category, if player chooses taken
	 * one, when it tells him choose the right one.
	 */
	private void chooseCategory(int player) {
		display.printMessage(playerNames[player - 1] + " choose category");
		while (true) {
			chosenCategory = display.waitForPlayerToSelectCategory();
			if (!usedCategory[player - 1][chosenCategory]) {
				usedCategory[player - 1][chosenCategory] = true;
				break;
			}
			display.printMessage("choose valid category");
		}
	}

	/*
	 * this method calculates scores according to chosen category.
	 */
	private int writeScore(int chosenCategory) {
		int points = 0;
		if (chosenCategory >= ONES && chosenCategory <= SIXES) {
			points = evaluateNums(chosenCategory);
		}
		if (chosenCategory == THREE_OF_A_KIND) {
			points = evaluateThreeOfAKind();
		}
		if (chosenCategory == FOUR_OF_A_KIND) {
			points = evaluateFourOfAKind();
		}
		if (chosenCategory == FULL_HOUSE) {
			points = evaluateFullHouse();
		}
		if (chosenCategory == SMALL_STRAIGHT) {
			points = evaluateSmallStraight();
		}
		if (chosenCategory == LARGE_STRAIGHT) {
			points = evaluateLargeStraight();
		}
		if (chosenCategory == YAHTZEE) {
			points = evaluateYAHTZEE();
		}
		if (chosenCategory == CHANCE) {
			points = evaluateChance();
		}
		return points;
	}

	/*
	 * calculates num scores.
	 */
	private int evaluateNums(int chosenCategory) {
		int point = 0;
		for (int i = 0; i < N_DICE; i++) {
			if (rolledDices[i] == chosenCategory) {
				point = point + chosenCategory;
			}
		}
		return point;
	}

	/*
	 * calculates three of a kind score.
	 */
	private int evaluateThreeOfAKind() {
		int point = 0;
		boolean p = checkThreeOfAKind();
		if (p) {
			for (int i = 0; i < N_DICE; i++) {
				point = point + rolledDices[i];
			}
		}
		return point;
	}

	/*
	 * calculates four of a kind score.
	 */
	private int evaluateFourOfAKind() {
		int point = 0;
		boolean p = checkFourOfAKind();
		if (p) {
			for (int i = 0; i < N_DICE; i++) {
				point = point + rolledDices[i];
			}
		}
		return point;
	}

	/*
	 * calculates full house score.
	 */
	private int evaluateFullHouse() {
		int point = 0;
		boolean p = checkFullHouse();
		if (p) {
			point = FULL_HOUSE_SCORE;
		}
		return point;
	}

	/*
	 * calculates small straight score.
	 */
	private int evaluateSmallStraight() {
		int point = 0;
		boolean p = checkSmallStraight();
		if (p) {
			point = SMALL_STRAIGHT_SCORE;
		}
		return point;
	}

	/*
	 * calculates large straight score.
	 */
	private int evaluateLargeStraight() {
		int point = 0;
		boolean p = checkLargeStraight();
		if (p) {
			point = LARGE_STRAIGHT_SCORE;
		}
		return point;
	}

	/*
	 * calculates yahtzee score.
	 */
	private int evaluateYAHTZEE() {
		int point = 0;
		boolean p = checkYAHTZEE();
		if (p) {
			point = YAHTZEE_SCORE;
		}
		return point;
	}

	/*
	 * calculates chance score.
	 */
	private int evaluateChance() {
		int point = 0;
		for (int i = 0; i < N_DICE; i++) {
			point = point + rolledDices[i];
		}
		return point;
	}

	/*
	 * checks if combination is three of a kind.
	 */
	private boolean checkThreeOfAKind() {
		boolean x = checkEquality(0, 2);
		boolean y = checkEquality(1, 3);
		boolean z = checkEquality(2, 4);
		boolean p = x || y || z;
		return p;
	}

	/*
	 * checks if combination is four of a kind.
	 */
	private boolean checkFourOfAKind() {
		boolean x = checkEquality(0, 3);
		boolean y = checkEquality(1, 4);
		boolean p = x || y;
		return p;
	}

	/*
	 * checks if combination is full house.
	 */
	private boolean checkFullHouse() {
		boolean x = checkEquality(0, 1) && checkEquality(2, 4);
		boolean y = checkEquality(0, 2) && checkEquality(3, 4);
		boolean p = x || y;
		return p;
	}

	/*
	 * checks if combination is small straight.
	 */
	private boolean checkSmallStraight() {
		boolean p = false;
		for (int i = 0; i < N_DICE - 1; i++) {
			p = checkIfIncreasing(i);
			if (p) {
				break;
			}
		}
		return p;
	}

	/*
	 * checks if combination is large straight.
	 */
	private boolean checkLargeStraight() {
		boolean p = checkIfIncreasing(N_DICE);
		return p;
	}

	/*
	 * checks if combination is yahtzee.
	 */
	private boolean checkYAHTZEE() {
		boolean p = checkEquality(0, N_DICE - 1);
		return p;
	}

	/*
	 * checks if dices are equal.
	 */
	private boolean checkEquality(int i, int j) {
		Arrays.sort(rolledDices);
		boolean x = true;
		for (int k = i; k < j; k++) {
			if (rolledDices[k] == rolledDices[k + 1]) {
				x = true;
				continue;
			}
			x = false;
			break;
		}
		return x;
	}

	/*
	 * checks if dices are increasing and we can tell this method which dice to
	 * skip, if we dont want to skip any, we can take i more than dices
	 */
	private boolean checkIfIncreasing(int i) {
		Arrays.sort(rolledDices);
		boolean x = true;
		for (int k = 0; k < N_DICE - 1; k++) {
			if (k == i) {
				continue;
			}
			if (rolledDices[k] + 1 == rolledDices[k + 1]) {
				x = true;
				continue;
			}
			x = false;
			break;
		}
		return x;
	}

	/*
	 * adds upperBonus.
	 */
	private void upperBonus(int player) {
		if (upperScore[player - 1] >= 63 && !Bonus[player - 1]) {
			display.updateScorecard(UPPER_BONUS, player, UPPER_BONUS_SCORE);
			lowerScore[player - 1] = lowerScore[player - 1] + UPPER_BONUS_SCORE;
			totalScore[player - 1] = totalScore[player - 1] + UPPER_BONUS_SCORE;
			Bonus[player - 1] = true;
		}
	}

	/*
	 * counts upper score.
	 */
	private void upperScoreCounter(int chosenCategory, int player, int score) {
		if (chosenCategory >= ONES && chosenCategory <= SIXES) {
			upperScore[player - 1] = upperScore[player - 1] + score;
		}
	}

	/*
	 * counts lower score.
	 */
	private void lowerScoreCounter(int chosenCategory, int player, int score) {
		if (chosenCategory >= THREE_OF_A_KIND && chosenCategory <= CHANCE) {
			lowerScore[player - 1] = lowerScore[player - 1] + score;
		}
	}

	/*
	 * counts total score.
	 */
	private void totalScoreCounter(int player, int score) {
		totalScore[player - 1] = totalScore[player - 1] + score;
	}

	/*
	 * displays and calculates upper score.
	 */
	private void upperScore(int chosenCategory, int player, int score) {
		upperScoreCounter(chosenCategory, player, score);
		display.updateScorecard(UPPER_SCORE, player, upperScore[player - 1]);
	}

	/*
	 * displays and calculates lower score.
	 */
	private void lowerScore(int chosenCategory, int player, int score) {
		lowerScoreCounter(chosenCategory, player, score);
		display.updateScorecard(LOWER_SCORE, player, lowerScore[player - 1]);
	}

	/*
	 * displays and total upper score.
	 */
	private void totalScore(int player, int score) {
		totalScoreCounter(player, score);
		display.updateScorecard(TOTAL, player, totalScore[player - 1]);

	}

	/*
	 * displays and calculates score.
	 */
	private void score(int chosenCategory, int player, int score) {
		display.updateScorecard(chosenCategory, player, score);
		upperScore(chosenCategory, player, score);
		upperBonus(player);
		lowerScore(chosenCategory, player, score);
		totalScore(player, score);
	}

	/*
	 * declares winner.
	 */
	private void winner() {
		int winner = 0;
		for (int i = 0; i < nPlayers; i++) {
			if (totalScore[i] > totalScore[winner]) {
				winner = i;
			}
		}
		display.printMessage(playerNames[winner] + " won with " + totalScore[winner] + " points");
	}

	/* Private instance variables */
	private int[] rolledDices = new int[N_DICE];
	private int nPlayers;
	private String[] playerNames;
	private boolean Bonus[];
	private int[] upperScore;
	private int[] lowerScore;
	private int[] totalScore;
	private boolean[][] usedCategory;
	private int chosenCategory;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
}
