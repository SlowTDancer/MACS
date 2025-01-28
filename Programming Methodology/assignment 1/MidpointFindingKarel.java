
import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {
	public void run() {
		moveLikeSmartKnight();
		goDown();
		putBeeper();
	}

	/*
	 * karel moves(if she can move) like knight from chess,because its the quadratic
	 * world if she moves 2 times in y axis and 1 time in x axis she will arrive at
	 * the midpoint of upper side
	 */
	private void moveLikeSmartKnight() {
		while (frontIsClear()) {
			smartMove();
			turnLeft();
			doubleSmartMove();
			if (frontIsClear()) {
				turnRight();
			}
		}
	}

	/*
	 * karel checks if she can move and if she can she moves
	 */
	private void smartMove() {
		if (frontIsClear()) {
			move();
		}
	}

	/*
	 * karel does smartMove() 2 times
	 */
	private void doubleSmartMove() {
		smartMove();
		smartMove();
	}

	/*
	 * karel goes down vertically until she reaches first row and then stops
	 */
	private void goDown() {
		turnAround();
		while (frontIsClear()) {
			move();
		}
	}
}