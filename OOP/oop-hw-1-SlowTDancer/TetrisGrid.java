//
// TetrisGrid encapsulates a tetris board and has
// a clearRows() capability.

public class TetrisGrid {
	private boolean[][] board;
	/**
	 * Constructs a new instance with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public TetrisGrid(boolean[][] grid) {
		board = grid;
	}
	
	private boolean check(int j){
		boolean res = true;
		for(int i = 0; i < board.length; i++){
			res = res && board[i][j];
		}
		return res;
	}
	/**
	 * Does row-clearing on the grid (see handout).
	 */
	public void clearRows() {
		int counter = 0;
		if(board.length == 0) return;
		boolean[][] res = new boolean[board.length][board[0].length];
		for(int j = 0; j < board[0].length; j++){
			if(check(j)) continue;
			for(int i = 0; i < board.length; i++){
				res[i][counter] = board[i][j];
			}
			counter++;
		}
		for(int j = counter; j < board[0].length; j++){
			for(int i = 0; i < board.length; i++){
				res[i][j] = false;
			}
		}
		board = res;
	}
	
	/**
	 * Returns the internal 2d grid array.
	 * @return 2d grid array
	 */
	boolean[][] getGrid() {
		return board;
	}
}
