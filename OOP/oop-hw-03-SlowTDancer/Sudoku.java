import java.util.*;

/*
 * Encapsulates a Sudoku grid to be solved.
 * CS108 Stanford.
 */
public class Sudoku {
	// Provided grid data for main/testing
	// The instance variable strategy is up to you.

	// Provided easy 1 6 grid
	// (can paste this text into the GUI too)
	public static final int[][] easyGrid = Sudoku.stringsToGrid(
			"1 6 4 0 0 0 0 0 2",
			"2 0 0 4 0 3 9 1 0",
			"0 0 5 0 8 0 4 0 7",
			"0 9 0 0 0 6 5 0 0",
			"5 0 0 1 0 2 0 0 8",
			"0 0 8 9 0 0 0 3 0",
			"8 0 9 0 4 0 2 0 0",
			"0 7 3 5 0 9 0 0 1",
			"4 0 0 0 0 0 6 7 9");


	// Provided medium 5 3 grid
	public static final int[][] mediumGrid = Sudoku.stringsToGrid(
			"530070000",
			"600195000",
			"098000060",
			"800060003",
			"400803001",
			"700020006",
			"060000280",
			"000419005",
			"000080079");

	// Provided hard 3 7 grid
	// 1 solution this way, 6 solutions if the 7 is changed to 0
	public static final int[][] hardGrid = Sudoku.stringsToGrid(
			"3 7 0 0 0 0 0 8 0",
			"0 0 1 0 9 3 0 0 0",
			"0 4 0 7 8 0 0 0 3",
			"0 9 3 8 0 0 0 1 2",
			"0 0 0 0 4 0 0 0 0",
			"5 2 0 0 0 6 7 9 0",
			"6 0 0 0 2 1 0 4 0",
			"0 0 0 5 3 0 9 0 0",
			"0 3 0 0 0 0 0 5 1");


	public static final int SIZE = 9;  // size of the whole 9x9 puzzle
	public static final int PART = 3;  // size of each 3x3 part
	public static final int MAX_SOLUTIONS = 100;

	// Provided various static utility methods to
	// convert data formats to int[][] grid.

	private static int[][] board;
	private List<Spot> spots;
	private String solution;
	private long solutionTime;
	private final String noSolution = "NO SOLUTION YET";

	/**
	 * Returns a 2-d grid parsed from strings, one string per row.
	 * The "..." is a Java 5 feature that essentially
	 * makes "rows" a String[] array.
	 * (provided utility)
	 * @param rows array of row strings
	 * @return grid
	 */
	public static int[][] stringsToGrid(String... rows) {
		int[][] result = new int[rows.length][];
		for (int row = 0; row<rows.length; row++) {
			result[row] = stringToInts(rows[row]);
		}
		return result;
	}


	/**
	 * Given a single string containing 81 numbers, returns a 9x9 grid.
	 * Skips all the non-numbers in the text.
	 * (provided utility)
	 * @param text string of 81 numbers
	 * @return grid
	 */
	public static int[][] textToGrid(String text) {
		int[] nums = stringToInts(text);
		if (nums.length != SIZE*SIZE) {
			throw new RuntimeException("Needed 81 numbers, but got:" + nums.length);
		}

		int[][] result = new int[SIZE][SIZE];
		int count = 0;
		for (int row = 0; row<SIZE; row++) {
			for (int col=0; col<SIZE; col++) {
				result[row][col] = nums[count];
				count++;
			}
		}
		return result;
	}


	/**
	 * Given a string containing digits, like "1 23 4",
	 * returns an int[] of those digits {1 2 3 4}.
	 * (provided utility)
	 * @param string string containing ints
	 * @return array of ints
	 */
	public static int[] stringToInts(String string) {
		int[] a = new int[string.length()];
		int found = 0;
		for (int i=0; i<string.length(); i++) {
			if (Character.isDigit(string.charAt(i))) {
				a[found] = Integer.parseInt(string.substring(i, i+1));
				found++;
			}
		}
		int[] result = new int[found];
		System.arraycopy(a, 0, result, 0, found);
		return result;
	}


	// Provided -- the deliverable main().
	// You can edit to do easier cases, but turn in
	// solving hardGrid.
	public static void main(String[] args) {
		Sudoku sudoku;
		sudoku = new Sudoku(hardGrid);

		System.out.println(sudoku); // print the raw problem
		int count = sudoku.solve();
		System.out.println("solutions:" + count);
		System.out.println("elapsed:" + sudoku.getElapsed() + "ms");
		System.out.println(sudoku.getSolutionText());
	}

	public static class Spot implements Comparable{
		private final int row;
		private final int col;

		public Spot(int row, int col){
			this.row = row;
			this.col = col;
		}

		public void set(int val){
			board[row][col] = val;
		}

		public List<Integer> getAssignableNums(){
			List<Integer> res = new ArrayList<Integer>() ;
			boolean[] canUse = new boolean[SIZE];
			Arrays.fill(canUse, true);
			for(int i = 0; i < SIZE; i++){
				if(board[row][i] != 0) canUse[board[row][i] - 1] = false;
			}
			for(int i = 0; i < SIZE; i++){
				if(board[i][col] != 0) canUse[board[i][col] - 1] = false;
			}
			int x = (row / PART) * PART;
			int y = (col / PART) * PART;
			for(int i = x; i < x + PART; i++){
				for(int j = y; j < y + PART; j++){
					if(board[i][j] != 0) canUse[board[i][j] - 1] = false;
				}
			}
			for(int i = 0; i < SIZE; i++){
				if(canUse[i]) res.add(i + 1);
			}
			return res;
		}

		@Override
		public int compareTo(Object o) {
			if(!(o instanceof Spot other)) throw new RuntimeException("Can't compare Spot to " + o.getClass());
			return this.getAssignableNums().size() - other.getAssignableNums().size();
		}
	}

	private class SolutionCounter{
		private int val;
		public SolutionCounter(){
			val = 0;
		}
		public void increase(){
			val++;
		}
		public int get(){
			return val;
		}
		public void setValue(int val){
			this.val = val;
		}
	}

	private void getSpots(){
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				if(board[i][j] != 0) continue;
				Spot spot = new Spot(i, j);
				spots.add(spot);
			}
		}
		Collections.sort(spots);
	}

	/**
	 * Sets up based on the given ints.
	 */
	public Sudoku(int[][] ints) {
		board = ints;
		spots = new ArrayList<>();
		solution = noSolution;
		getSpots();
	}

	public Sudoku(String s){
		this(textToGrid(s));
	}

	@Override
	public String toString(){
		StringBuilder res = new StringBuilder();
		for(int i = 0; i < SIZE; i++){
			for(int j = 0; j < SIZE; j++){
				if(j != 0) res.append(" ");
				res.append(board[i][j]);
			}
			res.append("\n");
		}
		return res.toString();
	}

	private void solveSudoku(SolutionCounter sCounter, int index){
		if(index == spots.size()){
			sCounter.increase();
			if(solution.equals(noSolution)) solution = this.toString();
			return;
		}
		Spot currSpot = spots.get(index);
		List<Integer> canAssign = currSpot.getAssignableNums();
		for (Integer integer : canAssign) {
			currSpot.set(integer);
			solveSudoku(sCounter, index + 1);
			currSpot.set(0);
			if (sCounter.get() >= MAX_SOLUTIONS) {
				sCounter.setValue(MAX_SOLUTIONS);
				return;
			}
		}
	}

	/**
	 * Solves the puzzle, invoking the underlying recursive search.
	 */
	public int solve() {
		SolutionCounter sCounter = new SolutionCounter();
		long solvingStartTime = System.currentTimeMillis();
		solveSudoku(sCounter, 0);
		solutionTime = System.currentTimeMillis() - solvingStartTime;
		return sCounter.get(); // YOUR CODE HERE
	}

	public String getSolutionText() {
		if(solution.equals(noSolution)) return "";
		return solution; // YOUR CODE HERE
	}

	public long getElapsed() {
		return solutionTime; // YOUR CODE HERE
	}

}
