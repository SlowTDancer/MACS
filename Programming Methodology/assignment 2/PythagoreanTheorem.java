
/*
 * File: PythagoreanTheorem.java
 * Name: irakli khutsishvili
 * Section Leader: giorgi megrelidze
 * -----------------------------
 * This file is the starter file for the PythagoreanTheorem problem.
 */

import acm.program.*;

public class PythagoreanTheorem extends ConsoleProgram {
	public void run() {
		println("Enter values to compute Pythagorean theorem.");
		int a = readInt("a: ");
		int b = readInt("b: ");
		double c = pythagoreanCounter(a, b);
		println("c = " + c);
	}

//this method squares a,b and adds them up. after this it takes square root out of it.
	private double pythagoreanCounter(int a, int b) {
		int x = a * a + b * b;
		double answer = Math.sqrt(x);
		return answer;
	}
}
