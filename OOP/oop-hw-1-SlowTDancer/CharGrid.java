// HW1 2-d array Problems
// CharGrid encapsulates a 2-d grid of chars and supports
// a few operations on the grid.

public class CharGrid {
	private char[][] grid;

	/**
	 * Constructs a new CharGrid with the given grid.
	 * Does not make a copy.
	 * @param grid
	 */
	public CharGrid(char[][] grid) {
		this.grid = grid;
	}
	
	/**
	 * Returns the area for the given char in the grid. (see handout).
	 * @param ch char to look for
	 * @return area for given char
	 */
	public int charArea(char ch) {
		int mnrow = Integer.MAX_VALUE;
		int mxrow = Integer.MIN_VALUE;
		int mncol = Integer.MAX_VALUE;
		int mxcol = Integer.MIN_VALUE;
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				if(grid[i][j] == ch){
					mnrow = Math.min(mnrow, i);
					mxrow = Math.max(mxrow, i);
					mncol = Math.min(mncol, j);
					mxcol = Math.max(mxcol, j);
				}
			}
		}
		if(mnrow == Integer.MAX_VALUE) return 0;
		return (mxrow - mnrow + 1) * (mxcol - mncol + 1);
	}

	private boolean inBounds(int i, int j){
		return Math.min(i, j) >= 0 && i < grid.length && j < grid[i].length;
	}

	private int repeated(int i, int j, int side){
		char ch = grid[i][j];
		int cnt = 0;
		boolean check = false;
		while(true){
			check = false;
			if(!inBounds(i, j)) break;
			switch (side) {
				case 0 -> {
					if (ch != grid[i][j]) break;
					check = true;
					cnt++;
					i--;
					break;
				}
				case 1 -> {
					if (ch != grid[i][j]) break;
					check = true;
					cnt++;
					i++;
					break;
				}
				case 2 -> {
					if (ch != grid[i][j]) break;
					check = true;
					cnt++;
					j--;
					break;
				}
				case 3 -> {
					if (ch != grid[i][j]) break;
					check = true;
					cnt++;
					j++;
					break;
				}
			}
			if(!check) break;
		}
		return cnt;
	}

	private boolean isCross(int i, int j){
		int counter = repeated(i, j, 0);
		if(counter <= 1) return false;
		for(int k = 1; k < 4; k++){
			if(counter != repeated(i, j, k)){
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the count of '+' figures in the grid (see handout).
	 * @return number of + in grid
	 */
	public int countPlus() {
		int res = 0;
		for(int i = 0; i < grid.length; i++){
			for(int j = 0; j < grid[i].length; j++){
				if(isCross(i, j)){
					res++;
				}
			}
		}
		return res;
	}
	
}
