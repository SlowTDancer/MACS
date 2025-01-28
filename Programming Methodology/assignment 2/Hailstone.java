
/*
 * File: Hailstone.java
 * Name: irakli khutsishvili
 * Section Leader: giorgi megrelidze
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	public void run() {
		int x = readInt("enter a number: ");
		int counted = goToOne(x);
		println("The process took " + counted + " to reach 1");
	}

	/*
	 * user gives this method an integer and if the number is even then it takes
	 * half,else makes it 3n+1 and when it reaches one it stops and counts how many
	 * processes it took to go to one
	 */
	private int goToOne(int x) {
		int counter = 0;
		while (true) {
			if (x % 2 == 1) {
				println(x + " is odd, so i make 3n+1: " + (3 * x + 1));
				x = 3 * x + 1;
				counter++;
			}
			if (x % 2 == 0) {
				println(x + " is even, so i take half: " + (x / 2));
				x = x / 2;
				counter++;
			}
			if (x == 1) {
				break;
			}
		}
		return counter;
	}
}
