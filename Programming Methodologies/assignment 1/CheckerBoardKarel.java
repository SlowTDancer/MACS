
import stanford.karel.*;

public class CheckerBoardKarel extends SuperKarel {
	public void run() {
		if (frontIsClear()) {
			while (frontIsClear()) {
				moveType1();
				moveType2();
			}
		} else {
			turnLeft();
			while (frontIsClear()) {
				moveLikeDominoes();
			}
		}
	}

	/*
	 * karel paints odd rows like dominoes and goes back after she does it and moves
	 * to a new row
	 */
	private void moveType1() {
		if (frontIsClear()) {
			moveLikeDominoes();
			goBack();
			goUp();
		}
	}

	/*
	 * karel paints even rows like reverse dominoes and goes back after she does it
	 * and moves to a new row
	 */
	private void moveType2() {
		if (frontIsClear()) {
			moveLikeReverseDominoes();
			goBack();
			goUp();
		}
	}

	/*
	 * karel puts beeper, then moves 2 times(if she can) and puts beepers until she
	 * is done
	 */
	private void moveLikeDominoes() {
		putBeeper();
		while (frontIsClear()) {
			ultraMove();
		}
	}

	/*
	 * karel goes back to the first column
	 */
	private void goBack() {
		turnAround();
		while (frontIsClear()) {
			move();
		}
		turnAround();
	}

	/*
	 * karel moves 2 times(if she can) and puts beepers until she is done
	 */
	private void ultraMove() {
		if (frontIsClear()) {
			move();
			if (frontIsClear()) {
				move();
				putBeeper();
			}
		}

	}

	/*
	 * karel moves to a new row
	 */
	private void goUp() {
		turnLeft();
		if (frontIsClear()) {
			move();
			turnRight();
		}
	}

	/*
	 * karel moves one time and does the same thing as moveLikeDominoes()
	 */
	private void moveLikeReverseDominoes() {
		if (frontIsClear()) {
			move();
		}
		moveLikeDominoes();
	}
}