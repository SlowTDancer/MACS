import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;


import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
  Unit test for Piece class -- starter shell.
 */
public class PieceTest extends TestCase {
	// You can create data to be used in the
	// test cases like this. For each run of a test method,
	// a new PieceTest object is created and setUp() is called
	// automatically by JUnit.
	// For example, the code below sets up some
	// pyramid and s pieces in instance variables
	// that can be used in tests.
	private Piece pyr1, pyr2, pyr3, pyr4;
	private Piece s, sRotated, s2, s2Rotated;
	private Piece square, squareRotated;
	private Piece stick, stickRotated;
	private Piece l1, l2, l1Rotated, l2Rotated;
	private Piece[] data;

	protected void setUp() throws Exception {
		super.setUp();

		pyr1 = new Piece(Piece.PYRAMID_STR);
		pyr2 = pyr1.computeNextRotation();
		pyr3 = pyr2.computeNextRotation();
		pyr4 = pyr3.computeNextRotation();

		s = new Piece(Piece.S1_STR);
		sRotated = s.computeNextRotation();

		s2 = new Piece(Piece.S2_STR);
		s2Rotated = new Piece("0 0  0 1  1 1  1 2");

		square = new Piece(Piece.SQUARE_STR);
		squareRotated = square.computeNextRotation();

		stick = new Piece(Piece.STICK_STR);
		stickRotated = stick.computeNextRotation();

		l1 = new Piece(Piece.L1_STR);
		l1Rotated = new Piece("0 0  1 0  2 0  2 1");

		l2 = new Piece(Piece.L2_STR);
		l2Rotated = new Piece("0 1  1 1  2 0  2 1");

		data = Piece.getPieces();
	}

	// Here are some sample tests to get you started

	public void testSampleSize() {
		// Check size of pyr piece
		assertEquals(3, pyr1.getWidth());
		assertEquals(2, pyr1.getHeight());

		// Now try after rotation
		// Effectively we're testing size and rotation code here
		assertEquals(2, pyr2.getWidth());
		assertEquals(3, pyr2.getHeight());

		// Now try with some other piece, made a different way
		Piece l = new Piece(Piece.STICK_STR);
		assertEquals(1, l.getWidth());
		assertEquals(4, l.getHeight());

		assertEquals(4, stickRotated.getWidth());
		assertEquals(1, stickRotated.getHeight());

		//square and rotated square
		assertEquals(2, square.getWidth());
		assertEquals(2, square.getHeight());

		assertEquals(2, squareRotated.getWidth());
		assertEquals(2, squareRotated.getHeight());

		//l1 and rotated l1
		assertEquals(2, l1.getWidth());
		assertEquals(3, l1.getHeight());

		assertEquals(3, l1.computeNextRotation().getWidth());
		assertEquals(2, l1.computeNextRotation().getHeight());

		//l2 and rotated l2
		assertEquals(2, l2.getWidth());
		assertEquals(3, l2.getHeight());

		assertEquals(3, l2.computeNextRotation().getWidth());
		assertEquals(2, l2.computeNextRotation().getHeight());

		//s1 and rotated s1
		assertEquals(3, s.getWidth());
		assertEquals(2, s.getHeight());

		assertEquals(2, sRotated.getWidth());
		assertEquals(3, sRotated.getHeight());

		//s2 and rotated s2
		assertEquals(3, s2.getWidth());
		assertEquals(2, s2.getHeight());

		assertEquals(2, s2Rotated.getWidth());
		assertEquals(3, s2Rotated.getHeight());

	}

	public void testGetBody(){
		TPoint[] sBody = new TPoint[4];
		sBody[0] = new TPoint(0, 0);
		sBody[1] = new TPoint(1, 0);
		sBody[2] = new TPoint(1 ,1);
		sBody[3] = new TPoint(new TPoint(2, 1));
		assertEquals(sBody[3].toString(), "(2,1)");
		assertTrue(Arrays.equals(s.getBody(), sBody));
	}

	// Test the skirt returned by a few pieces
	public void testSampleSkirt() {
		// Note must use assertTrue(Arrays.equals(... as plain .equals does not work
		// right for arrays.
		assertTrue(Arrays.equals(new int[] {0, 0, 0}, pyr1.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0, 1}, pyr3.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 0, 1}, s.getSkirt()));
		assertTrue(Arrays.equals(new int[] {1, 0}, sRotated.getSkirt()));

		assertTrue(Arrays.equals(new int[]{0, 0}, l1.getSkirt()));
		assertTrue(Arrays.equals(new int[]{0, 0, 0}, l1Rotated.getSkirt()));

		assertTrue(Arrays.equals(new int[]{0, 0}, l2.getSkirt()));
		assertTrue(Arrays.equals(new int[]{1, 1, 0}, l2Rotated.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0, 0}, square.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0}, squareRotated.getSkirt()));

		assertTrue(Arrays.equals(new int[] {0}, stick.getSkirt()));
		assertTrue(Arrays.equals(new int[] {0, 0, 0, 0}, stickRotated.getSkirt()));
	}

	//Test equals
	public void testEquals(){
		assertEquals(pyr1, new Piece("0 0  1 0  1 1  2 0"));
		assertEquals(pyr1, new Piece("1 0  0 0  2 0  1 1"));
		assertEquals(pyr1, pyr4.computeNextRotation());

		assertEquals(stick, new Piece("0 0  0 1  0 2  0 3"));
		assertEquals(stickRotated, new Piece("0 0  1 0  2 0  3 0"));
		assertEquals(stick, data[Piece.STICK]);

		assertEquals(square, new Piece("0 0  1 1  1 0  0 1"));
		assertEquals(squareRotated, new Piece("0 0  0 1  1 0  1 1"));

		assertEquals(l1, new Piece("0 0  1 0  0 1  0 2"));
		assertEquals(l1, new Piece(Piece.L1_STR));

		assertEquals(s, new Piece(Piece.S1_STR));
		assertEquals(sRotated, new Piece("0 2  1 1  1 0  0 1"));
	}

	//Test rotations
	public void testRotations(){
		Piece r1 = l1.computeNextRotation();
		assertEquals(r1, l1Rotated);

		Piece r2 = l2.computeNextRotation();
		assertEquals(r2, l2Rotated);

		assertEquals(square, squareRotated);

		assertEquals(stickRotated, new Piece("0 0  2 0  1 0  3 0"));
		assertEquals(stickRotated.computeNextRotation(), stick);

		Piece l1r4 = l1;
		for(int i = 0; i < 4; i++){
			l1r4 = l1r4.computeNextRotation();
		}
		assertEquals(l1r4, l1);

		assertEquals(s2.computeNextRotation(), new Piece("0 0  0 1  1 1  1 2"));
		assertEquals(s2.computeNextRotation().computeNextRotation(), new Piece("0 1  1 0  1 1  2 0"));
	}

	//Test fast rotations
	public void testFastRotations(){
		assertEquals(data[Piece.SQUARE], data[Piece.SQUARE].fastRotation());
		assertEquals(data[Piece.SQUARE], data[Piece.SQUARE].fastRotation().fastRotation());

		assertEquals( new Piece("0 0  0 1  1 1  1 2"), data[Piece.S2].fastRotation());
		assertEquals(data[Piece.S2].fastRotation().fastRotation(), new Piece("0 1  1 0  1 1  2 0"));

		assertEquals(l1Rotated, data[Piece.L1].fastRotation());
		assertEquals(l2Rotated, data[Piece.L2].fastRotation());

		assertEquals(stickRotated, data[Piece.STICK].fastRotation());


		assertEquals(pyr2, data[Piece.PYRAMID].fastRotation());
		assertEquals(pyr3, data[Piece.PYRAMID].fastRotation().fastRotation());
		assertEquals(pyr4, data[Piece.PYRAMID].fastRotation().fastRotation().fastRotation());
		assertEquals(pyr1, data[Piece.PYRAMID].fastRotation().fastRotation().fastRotation().fastRotation());
	}

	//test invalid parse
	public void testParse(){
		assertThrows(RuntimeException.class, new Executable() {
			@Override
			public void execute() throws Throwable {
				Piece pt = new Piece("aaaaaa");
			}
		});
	}

}
