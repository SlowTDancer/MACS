import stanford.karel.*;

public class CollectNewspaperKarel extends SuperKarel {
	public void run() {
		goToNewspapersPosition();
		pickNewspaper();
		goToStartingPosition();
	}

	/*
	 * karel goes to newspapers 
	 * prePosition:3x4 east 
	 * postPosition:6x3 east
	 */
	private void goToNewspapersPosition() {
		// prePosition:3x4 east
		// postPosition:3x4 south
		turnRight();
		// prePosition:3x4 south
		// postPosition:3x3 south
		move();
		// prePosition:3x3 south
		// postPosition:3x3 east
		turnLeft();
		// prePosition:3x3 east
		// postPosition:newspapersPositon(6x3) east
		while (noBeepersPresent()) {
			move();
		}
	}

	/*
	 * karel picks newspaper
	 */
	private void pickNewspaper() {
		pickBeeper();
	}

	/*
	 * karel goes back to starting position 
	 * prePosition:newspapersPosition(6x3) east
	 * postPosition:3x4 east
	 */
	private void goToStartingPosition() {
		// prePosition:newspapersPosition(6x3) east
		// postPosition:newspapersPositon(6x3) west
		turnAround();
		// prePosition:newspapersPosition(6x3) west
		// postPosition:3x3 west
		while (frontIsClear()) {
			move();
		}
		// prePosition:3x3 west
		// postPositon:3x3 north
		turnRight();
		// prePosition:3x3 north
		// postPosition:3x4 north
		move();
		// prePosition:3x4 north
		// postPosition:3x4 east
		turnRight();
	}
}
