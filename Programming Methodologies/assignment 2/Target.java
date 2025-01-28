
/*
 * File: Target.java
 * Name: irakli khutsishvili
 * Section Leader: giorgi megrelidze
 * -----------------
 * This file is the starter file for the Target problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {
	// given radius of the largest circle in pixels
	private static final double RADIUS = 72;
	// radius of the first circle in pixels
	private static final double RADIUS1 = RADIUS;
	// radius of the second circle in pixels
	private static final double RADIUS2 = RADIUS * (1.65 / 2.54);
	// radius of the third circle in pixels
	private static final double RADIUS3 = RADIUS * (0.76 / 2.54);

	public void run() {
		drawCircle(RADIUS1, Color.RED);
		drawCircle(RADIUS2, Color.WHITE);
		drawCircle(RADIUS3, Color.RED);
	}

	/*
	 * we give radius and a color to this method and it draws a circle and paints it
	 */
	private void drawCircle(double a, Color c) {
		GOval Circle = new GOval(a, a);
		double x = (getWidth() - a) / 2;
		double y = (getHeight() - a) / 2;
		Circle.setFilled(true);
		Circle.setFillColor(c);
		Circle.setColor(c);
		add(Circle, x, y);
	}

}