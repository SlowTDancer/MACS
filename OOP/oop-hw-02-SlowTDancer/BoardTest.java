import junit.framework.TestCase;


public class BoardTest extends TestCase {
	Board b;
	Piece pyr1, pyr2, pyr3, pyr4, s, sRotated, s2, s2Rotated;
	private Piece square, squareRotated;
	private Piece stick;
	private Piece l2, l2Rotated;

	// This shows how to build things in setUp() to re-use
	// across tests.

	// In this case, setUp() makes shapes,
	// and also a 3X6 board, with pyr placed at the bottom,
	// ready to be used by tests.
	protected void setUp() throws Exception {
		b = new Board(3, 6);

		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		l2 = new Piece(Piece.L2_STR);
		l2Rotated = l2.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		s2 = new Piece(Piece.S2_STR);
		s2Rotated = s2.computeNextRotation();

		square = new Piece(Piece.SQUARE_STR);
		squareRotated = square.computeNextRotation();

		stick = new Piece(Piece.STICK_STR);

		b.place(pyr1, 0, 0);
	}

	// Check the basic width/height/max after the one placement
	public void testSample1() {
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(1));
		assertEquals(2, b.getMaxHeight());
		assertEquals(3, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(0, b.getRowWidth(2));
	}

	// Place sRotated into the board, then check some measures
	public void testSample2() {
		b.commit();
		int result = b.place(sRotated, 1, 1);
		assertEquals(Board.PLACE_OK, result);
		assertEquals(1, b.getColumnHeight(0));
		assertEquals(4, b.getColumnHeight(1));
		assertEquals(3, b.getColumnHeight(2));
		assertEquals(4, b.getMaxHeight());
	}

	// Makre  more tests, by putting together longer series of
	// place, clearRows, undo, place ... checking a few col/row/max
	// numbers that the board looks right after the operations.

	//test place
	public void testPlace1(){
		b.commit();
		int res = b.place(l2Rotated, 0, 1);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		b.commit();
		res = b.place(square, 1, 1);
		assertEquals(Board.PLACE_BAD, res);
	}

	public void testPlace2(){
		b.commit();
		int res = b.place(stick, 0, 1);
		assertEquals(Board.PLACE_OK, res);
		b.commit();
		res = b.place(stick, 2, 1);
		assertEquals(Board.PLACE_ROW_FILLED, res);
	}

	public void testPlace3(){
		b.commit();
		int res = b.place(square, 2, 1);
		assertEquals(Board.PLACE_OUT_BOUNDS, res);
		b.commit();
		res = b.place(s, 0, 2);
		assertEquals(Board.PLACE_ROW_FILLED, res);
	}

	//test clear row
	public void testClearRows1(){
		b.commit();
		int res = b.place(l2Rotated, 0, 2);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		res = b.clearRows();
		assertEquals(2, res);
	}

	public void testClearRows2(){
		b.commit();
		int res = b.place(sRotated, 1, 1);
		b.commit();
		assertEquals(Board.PLACE_OK, res);
		res = b.place(stick, 0, 1);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		b.commit();
		res = b.clearRows();
		b.commit();
		assertEquals(3, res);
	}

	public void testClearRows3(){
		Board board = new Board(3, 6);
		board.place(stick, 0, 0);
		board.commit();
		board.place(stick, 1, 0);
		board.commit();
		board.place(stick, 2, 0);
		board.commit();
		int res = board.clearRows();
		board.commit();
		assertEquals(4, res);
		board.place(stick, 0, 0);
		board.commit();
		res = board.place(squareRotated, 1, 0);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		res = board.clearRows();
		assertEquals(2, res);
	}

	public void testClearRows4() {
		int res = b.clearRows();
		assertEquals(1, res);
		b.commit();
		//rotated s2 added
		b.place(s2Rotated, 0, 0);
		assertEquals(1, b.getRowWidth(2));
		assertEquals(3, b.getColumnHeight(1));
		assertEquals(3, b.getMaxHeight());
		b.commit();
		//stick added
		res = b.place(stick, 2, 0);
		b.commit();
		assertEquals(Board.PLACE_ROW_FILLED, res);
		//clears 2 rows
		res = b.clearRows();
		assertEquals(2, res);
		b.commit();
		//checks if everything went well
		assertEquals(0, b.getColumnHeight(0));
		assertEquals(2, b.getColumnHeight(2));
		assertEquals(1, b.getColumnHeight(1));
		assertEquals(2, b.getRowWidth(0));
		assertEquals(1, b.getRowWidth(1));
		assertEquals(2, b.getMaxHeight());
	}

	public void testClearRows5() {
		Board board = new Board(4, 8);
		int res = board.place(square, 0, 0);
		board.commit();
		assertEquals(Board.PLACE_OK, res);
		res = board.place(square, 0, 2);
		board.commit();
		assertEquals(Board.PLACE_OK, res);
		res = board.place(stick, 2, 0);
		board.commit();
		assertEquals(Board.PLACE_OK, res);
		res = board.place(stick, 3, 0);
		board.commit();
		assertEquals(Board.PLACE_ROW_FILLED, res);
		res = board.clearRows();
		assertEquals(4, res);
		for (int i = 0; i < board.getHeight(); i++) {
			assertEquals(0, board.getRowWidth(i));
		}
		for (int i = 0; i < board.getWidth(); i++) {
			assertEquals(0, board.getColumnHeight(i));
		}
	}

	public void testDropHeight(){
		Board board = new Board(4,8);
		assertEquals(0, board.dropHeight(stick.computeNextRotation(), 0));
		board.place(pyr1, 0, 0);
		board.commit();
		assertEquals(2, board.dropHeight(stick.computeNextRotation(), 0));
		assertEquals(2, board.dropHeight(s2Rotated, 1));
		assertEquals(1, board.dropHeight(s2Rotated, 0));
		assertEquals(1, board.dropHeight(square, 2));
	}

	public void testUndo1(){
		b.undo();
		assertFalse(b.getGrid(0,0));
		assertFalse(b.getGrid(1,1));
		assertFalse(b.getGrid(0,2));
	}

	public void testUndo2(){
		Board board = new Board(3,9);
		board.place(stick,0,0);
		board.commit();
		board.place(stick,1,0);
		board.commit();
		board.place(stick,2,0);
		assertEquals(4, board.clearRows());
		board.undo();
		board.undo();
		for(int i = 0; i < board.getMaxHeight(); i++){
			for(int j = 0; j < 2; j++){
				assertTrue(board.getGrid(j, i));
			}
		}
		for(int i = 0; i < board.getMaxHeight(); i++){
			assertFalse(board.getGrid(2, i));
		}
		assertTrue(board.getGrid(1, -1));
	}

	public void testCUndo3(){
		Board board = new Board(4,4);
		board.place(stick.computeNextRotation(), 0,0);
		board.commit();
		int res = board.clearRows();
		board.commit();
		assertEquals(1, res);
		board.undo();
		assertEquals(0, board.getMaxHeight());
		board.clearRows();
		assertEquals(0, board.getMaxHeight());
	}

	public void testToString(){
		Board board = new Board(4, 4);
		int res = board.place(square, 0, 0);
		board.commit();
		assertEquals(Board.PLACE_OK, res);
		res = board.place(square, 2, 0);
		assertEquals(Board.PLACE_ROW_FILLED, res);
		String ans = "|    |\n|    |\n|++++|\n|++++|\n------";
		assertEquals(ans, board.toString());
	}
}