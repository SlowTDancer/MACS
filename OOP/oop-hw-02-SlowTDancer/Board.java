// Board.java

import java.awt.image.AreaAveragingScaleFilter;
import java.util.Arrays;

/**
 CS108 Tetris Board.
 Represents a Tetris board -- essentially a 2-d grid
 of booleans. Supports tetris pieces and row clearing.
 Has an "undo" feature that allows clients to add and remove pieces efficiently.
 Does not do any drawing or have any idea of pixels. Instead,
 just represents the abstract 2-d board.
*/
public class Board {
	// Some ivars are stubbed out for you:
	private int width;
	private int height;
	private boolean[][] grid;
	private boolean DEBUG = true;
	private boolean committed;
	private int[] heights;
	private int[] widths;
	private int maxHeight;
	//for undo function
	private boolean[][] xGrid;
	private int xMaxHeight;
	private int[] xHeights;
	private int[] xWidths;


	// Here a few trivial methods are provided:

	/**
	 Creates an empty board of the given width and height
	 measured in blocks.
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		grid = new boolean[width][height];
		for(int i = 0; i < grid.length; i++){
			Arrays.fill(grid[i], false);
		}
		committed = true;
		heights = new int[width];
		Arrays.fill(heights, 0);
		widths = new int [height];
		Arrays.fill(widths, 0);
		maxHeight = 0;
		xMaxHeight = 0;
		xGrid = new boolean[width][height];
		for(int i = 0; i < xGrid.length; i++){
			Arrays.fill(xGrid[i], false);
		}
		xHeights = new int[width];
		Arrays.fill(xHeights, 0);
		xWidths = new int [height];
		Arrays.fill(xWidths, 0);
	}


	/**
	 Returns the width of the board in blocks.
	 */
	public int getWidth() {
		return width;
	}


	/**
	 Returns the height of the board in blocks.
	 */
	public int getHeight() {
		return height;
	}


	/**
	 Returns the max column height present in the board.
	 For an empty board this is 0.
	 */
	public int getMaxHeight() {
		return maxHeight;
	}


	/**
	 Checks the board for internal consistency -- used
	 for debugging.
	 */
	public void sanityCheck() {
		if (DEBUG) {
			heightChecker();
			widthChecker();
		}
	}

	private void heightChecker(){
		int maxH = 0;
		int Hcounter;
		for(int i = 0; i < width; i++){
			Hcounter = 0;
			for(int j = 0; j < height; j++){
				if(!grid[i][j]) continue;
				Hcounter = j + 1;
			}
			if(Hcounter != heights[i]){throw new RuntimeException("height of the " + i +  "th column was expected to be: " + heights[i] + " but was: " + Hcounter);}
			maxH = Math.max(maxH, Hcounter);
		}
		if(maxH != maxHeight){throw new RuntimeException("max height was expected to be: " + maxHeight + " but was: " + maxH);}
	}

	private void widthChecker(){
		int Wcounter;
		for(int j = 0; j < height; j++){
			Wcounter = 0;
			for(int i = 0; i < width; i++){
				if(!grid[i][j]) continue;
				Wcounter++;
			}
			if(Wcounter != widths[j]){throw new RuntimeException("width of the " + j +  "th column was expected to be: " + widths[j] + " but was: " + Wcounter);}
		}
	}

	/**
	 Given a piece and an x, returns the y
	 value where the piece would come to rest
	 if it were dropped straight down at that x.

	 <p>
	 Implementation: use the skirt and the col heights
	 to compute this fast -- O(skirt length).
	 */
	public int dropHeight(Piece piece, int x) {
		int res = 0;
		int[] sk = piece.getSkirt();
		for(int i = 0; i < piece.getWidth(); i++){
			int curr = heights[x + i] - sk[i];
			res = Math.max(curr, res);
		}
		return res;
	}


	/**
	 Returns the height of the given column --
	 i.e. the y value of the highest block + 1.
	 The height is 0 if the column contains no blocks.
	 */
	public int getColumnHeight(int x) {
		return heights[x];
	}


	/**
	 Returns the number of filled blocks in
	 the given row.
	 */
	public int getRowWidth(int y) {
		return widths[y];
	}


	private boolean inBounds(int i, int j){
		return Math.min(i, j) >= 0 && i < width && j < height;
	}

	/**
	 Returns true if the given block is filled in the board.
	 Blocks outside of the valid width/height area
	 always return true.
	 */
	public boolean getGrid(int x, int y) {
		return !inBounds(x, y) || grid[x][y];
	}


	public static final int PLACE_OK = 0;
	public static final int PLACE_ROW_FILLED = 1;
	public static final int PLACE_OUT_BOUNDS = 2;
	public static final int PLACE_BAD = 3;

	/**
	 Attempts to add the body of a piece to the board.
	 Copies the piece blocks into the board grid.
	 Returns PLACE_OK for a regular placement, or PLACE_ROW_FILLED
	 for a regular placement that causes at least one row to be filled.

	 <p>Error cases:
	 A placement may fail in two ways. First, if part of the piece may falls out
	 of bounds of the board, PLACE_OUT_BOUNDS is returned.
	 Or the placement may collide with existing blocks in the grid
	 in which case PLACE_BAD is returned.
	 In both error cases, the board may be left in an invalid
	 state. The client can use undo(), to recover the valid, pre-place state.
	 */
	public int place(Piece piece, int x, int y) {
		if(!committed) throw new RuntimeException("place commit");
		backup();
		int result = PLACE_OK;
		committed = false;

		TPoint[] body = piece.getBody();
		for(int i = 0; i < body.length; i++){
			int currX = x + body[i].x;
			int currY = y + body[i].y;
			//if place was taken
			if(!inBounds(currX, currY)) continue;
			if(grid[currX][currY]) {
				result = PLACE_BAD;
				continue;
			}
			//update data
			grid[currX][currY] = true;
			heights[currX] = Math.max(heights[currX], currY + 1);
			widths[currY]++;
			maxHeight = Math.max(heights[currX], maxHeight);
			//if row got filled
			if(widths[currY] == width && !(result == PLACE_BAD || result == PLACE_OUT_BOUNDS)) result = PLACE_ROW_FILLED;
		}

		//check if piece can fit on board
		if(!inBounds(x, y) || !inBounds(x + piece.getWidth() - 1, y + piece.getHeight() - 1)) result = PLACE_OUT_BOUNDS;
		sanityCheck();
		return result;
	}

	private void backup(){
		for(int i = 0; i < width; i++){
			System.arraycopy(grid[i], 0, xGrid[i],0, height);
		}
		System.arraycopy(widths, 0, xWidths, 0, widths.length);
		System.arraycopy(heights, 0, xHeights, 0, heights.length);
		xMaxHeight = maxHeight;
	}


	/**
	 Deletes rows that are filled all the way across, moving
	 things above down. Returns the number of rows cleared.
	 */
	public int clearRows() {
		if(committed) backup();
		committed = false;
		int rowsCleared = 0;
		boolean[] cleared = new boolean[height];
		Arrays.fill(cleared, false);
		for(int i = 0; i < maxHeight; i++){
			if(getRowWidth(i) == width){
				cleared[i] = true;
				rowsCleared++;
			}
		}
		int curr = clear(cleared);
		for(int j = curr; j < maxHeight; j++){
			for(int i = 0; i < width; i++){
				grid[i][j] = false;
			}
		}
		updateData(rowsCleared, curr);
		sanityCheck();
		return rowsCleared;
	}

	private int clear(boolean[] cleared){
		int curr = 0;
		int index = 0;
		while(index < maxHeight) {
			if (cleared[index]) {
				index++;
				continue;
			}
			for (int i = 0; i < width; i++) {
				grid[i][curr] = grid[i][index];
			}
			widths[curr] = widths[index];
			index++;
			curr++;
		}
		return curr;
	}

	private void updateData(int rowsCleared, int from){
		for(int j = from; j < maxHeight; j++){
			widths[j] = 0;
		}
		int temp = 0;
		for(int i = 0; i < width; i++){
			int Hcounter = 0;
			for(int j = 0; j < maxHeight; j++){
				if(grid[i][j]) Hcounter = j + 1;
			}
			heights[i] = Hcounter;
			temp = Math.max(temp, Hcounter);
		}
		maxHeight = temp;
	}

	/**
	 Reverts the board to its state before up to one place
	 and one clearRows();
	 If the conditions for undo() are not met, such as
	 calling undo() twice in a row, then the second undo() does nothing.
	 See the overview docs.
	 */
	public void undo() {
		if(!committed){
			boolean[][] temp1 = grid;
			grid = xGrid;
			xGrid = temp1;

			int[] temp2 = widths;
			widths = xWidths;
			xWidths = temp2;

			temp2 = heights;
			heights = xHeights;
			xHeights = temp2;

			int temp3 = maxHeight;
			maxHeight = xMaxHeight;
			xMaxHeight = temp3;
			committed = true;
		}
		sanityCheck();
	}


	/**
	 Puts the board in the committed state.
	 */
	public void commit() {
		committed = true;
	}


	/*
     Renders the board state as a big String, suitable for printing.
     This is the sort of print-obj-state utility that can help see complex
     state change over time.
     (provided debugging utility)
     */
	public String toString() {
		StringBuilder buff = new StringBuilder();
		for (int y = height-1; y>=0; y--) {
			buff.append('|');
			for (int x=0; x<width; x++) {
				if (getGrid(x,y)) buff.append('+');
				else buff.append(' ');
			}
			buff.append("|\n");
		}
		for (int x=0; x<width+2; x++) buff.append('-');
		return(buff.toString());
	}
}


