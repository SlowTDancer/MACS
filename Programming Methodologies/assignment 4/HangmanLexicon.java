
/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

import acm.util.*;

public class HangmanLexicon {
	/*
	 * arraylist for the words from reader.
	 */
	private ArrayList<String> word = new ArrayList<String>();

	/*
	 * reads text and fills arraylist.
	 */
	public HangmanLexicon() {
		try {
			BufferedReader rd = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			while (true) {
				String line = rd.readLine();
				if (line == null) {
					break;
				}
				word.add(line);
			}
			rd.close();
		} catch (Exception ex) {
			throw new ErrorException("lol");
		}
	}

	/** Returns the number of words in the lexicon. */
	public int getWordCount() {
		return word.size();
	}

	/** Returns the word at the specified index. */
	public String getWord(int index) {
		return word.get(index);
	};
}
