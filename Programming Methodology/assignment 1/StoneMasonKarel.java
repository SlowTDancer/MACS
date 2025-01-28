import stanford.karel.SuperKarel;

public class StoneMasonKarel extends SuperKarel {
	public void run() {
		fixColumn();
		doAction();
	}

	/*
	 * karel repairs columns and moves until she is done
	 */
	private void doAction() {
		while (frontIsClear()) {
			moveToNewColumn();
			fixColumn();
		}
	}

	/*
	 * karel repairs one column and returns to starting position
	 */
	private void fixColumn() {
		fillWithBricks();
		goToStreet1();
	}
	/*
	 * karel moves to a new column
	 */

	private void moveToNewColumn() {
		for (int i = 0; i < 4; i++) {
			move();
		}
	}

	/*
	 * karel repairs one column
	 */
	private void fillWithBricks() {
		turnLeft();
		if (noBeepersPresent()) {
			putBeeper();
		}
		while (frontIsClear()) {
			move();
			if (noBeepersPresent()) {
				putBeeper();
			}
		}
	}

	/*
	 * karel goes back to street 1
	 */
	private void goToStreet1() {
		turnAround();
		while (frontIsClear()) {
			move();
		}
		turnLeft();
	}
}
