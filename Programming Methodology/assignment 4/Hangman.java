
/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.program.*;
import acm.util.*;

public class Hangman extends ConsoleProgram {
	/*
	 * words meaning before game starts for the first time.
	 */
	private String word = "";
	/*
	 * words length before game starts for the first time.
	 */
	private int wordLength = 0;
	/*
	 * hiddenWords meaning before game starts for the first time.
	 */
	private String hiddenWord = "";
	/*
	 * amount of lives player has.
	 */
	private int livesLeft = 8;
	/*
	 * helping classes.
	 */
	HangmanLexicon Lexicon = new HangmanLexicon();
	RandomGenerator rgen = RandomGenerator.getInstance();
	private HangmanCanvas canvas;

	public void run() {
		while (true) {
			word = wordChooser();
			wordLength = word.length();
			hiddenWord = encryption(wordLength);
			startGame();
			gameProcess();
			gameResult();
			String x = readLine("if you want to play again, type (i want to play one more time): ");
			if (x.equals("i want to play one more time")) {
				livesLeft = 8;
				continue;
			}
			break;
		}
	}

	/*
	 * adds canvas
	 */
	public void init() {
		canvas = new HangmanCanvas();
		add(canvas);
	}

	/*
	 * chooses word from HangmanLexicon randomly.
	 */
	private String wordChooser() {
		int y = Lexicon.getWordCount();
		int wordIndex = rgen.nextInt(y);
		String word = Lexicon.getWord(wordIndex);
		return word;
	}

	/*
	 * encrypts chosen word.
	 */
	private String encryption(int x) {
		String encyption = "";
		for (int i = 0; i < x; i++) {
			encyption = encyption + "-";
		}
		return encyption;
	}

	/*
	 * prepares things for game to start.
	 */
	private void startGame() {
		println("Welcome To Hangman!");
		canvas.reset();
	}

	/*
	 * processes the game.
	 */
	private void gameProcess() {
		while (true) {
			wordNow();
			guessesLeft();
			String x = readChar();
			charChecker(x);
			if (livesLeft == 0) {
				break;
			}
			if (hiddenWord.contains("-")) {
				continue;
			}
			println("You guessed the word: " + word);
			break;
		}
	}

	/*
	 * tells us how much we guessed a word.
	 */
	private void wordNow() {
		String wordNow = "The word now looks like this: " + hiddenWord;
		canvas.displayWord(hiddenWord);
		println(wordNow);
	}

	/*
	 * tells us how many lives we have left.
	 */
	private void guessesLeft() {
		String guessesLeft = "You have " + livesLeft + " guesses left.";
		println(guessesLeft);
	}
	/*
	 * reads a character.
	 */

	private String readChar() {
		String a = "";
		while (true) {
			a = readLine("your guess: ");
			if (a.length() != 1) {
				println("incorrect input.Please enter a character.");
				continue;
			}
			if ((a.charAt(0) >= 'a' && a.charAt(0) <= 'z') || (a.charAt(0) >= 'A' && a.charAt(0) <= 'Z')) {
				break;
			}
			println("incorrect input.Please enter a character.");
		}
		return a.toUpperCase();
	}

	/*
	 * checks if a char is in the word and decyphers hiddenWord if there was one,
	 * also if char isn't in the word it takes our life and and draws some things.
	 */
	private void charChecker(String x) {
		if (word.contains(x)) {
			println("That guess is corret.");
			for (int i = 0; i < wordLength; i++) {
				String a = "" + word.charAt(i);
				if (x.equals(a)) {
					hiddenWord = hiddenWord.substring(0, i) + x + hiddenWord.substring(i + 1, wordLength);
				}
			}
		} else {
			println("There are no " + x + "'s in the word.");
			char a = x.charAt(0);
			canvas.noteIncorrectGuess(a);
			livesLeft--;
		}
	}

	/*
	 * announces games result.
	 */
	private void gameResult() {
		if (livesLeft == 0) {
			println("the word was: " + word);
			println("You Lose.");
		} else {
			println("You Win.");
			canvas.displayWord(hiddenWord);
		}
	}
}