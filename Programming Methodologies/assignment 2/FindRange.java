
/*
 * File: FindRange.java
 * Name: irakli khutshivili
 * Section Leader: giorgi megrelidze
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {
	public void run() {
		writeText();
		int min = Integer.MAX_VALUE;// we take largest int to memorize minimum value of the number
		int max = Integer.MIN_VALUE;// we take smallest int to memorize maximum value of the number
		int counter = 0;// how many times did we write a number
		while (true) {
			int x = readInt("enter vaule: ");
			if (x == 0) {
				break;
			}
			counter++;// counts how many times we will do the process
			min = Math.min(min, x);// memorizes smallest number
			max = Math.max(max, x);// memorizes largest number
		}
		if (counter == 0) {
			println("error");
		} else {
			println("smallest:" + min);
			println("largest:" + max);
		}
	}

	private void writeText() {
		println("This program finds the largest and smallest numbers.");
	}

}
