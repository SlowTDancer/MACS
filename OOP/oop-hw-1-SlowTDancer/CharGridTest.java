
// Test cases for CharGrid -- a few basic tests are provided.

import junit.framework.TestCase;

public class CharGridTest extends TestCase {
	public void testCharArea1() {
		char[][] grid = new char[][] {
				{'a', 'y', ' '},
				{'x', 'a', 'z'},
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(4, cg.charArea('a'));
		assertEquals(1, cg.charArea('z'));
	}
	public void testCharArea2() {
		char[][] grid = new char[][] {
				{'c', 'a', ' '},
				{'b', ' ', 'b'},
				{' ', ' ', 'a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(6, cg.charArea('a'));
		assertEquals(3, cg.charArea('b'));
		assertEquals(1, cg.charArea('c'));
	}

	public void testCharArea3(){
		char[][] grid = new char[][]{
				{'a','b','a'},
				{'b','a','a'},
				{'a','b','a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.charArea('r'));
		assertEquals(6, cg.charArea('b'));
		assertEquals(9, cg.charArea('a'));
	}

	public void testCharArea4(){
		char[][] grid = new char[][]{
				{' ','a','b','b','b'},
				{'a','o','i','b','c'},
				{'c','i','b','a','b'},
				{' ',' ', 'a','b','i'},
				{'b','c','k','i','e'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(16, cg.charArea('a'));
		assertEquals(16, cg.charArea('i'));
		assertEquals(8, cg.charArea(' '));
		assertEquals(25, cg.charArea('b'));
		assertEquals(20, cg.charArea('c'));
	}

	//
	// Empty grid test
	//
	public void testCharArea5(){
		char[][] grid = new char[][]{};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.charArea('R'));
	}

	//
	//not rectangle
	//
	public void testCharArea6(){
		char[][] grid = new char[][]{
				{'a', 'x', 'd'},
				{'a', 'b', 'x', 'a'},
				{'r', 'r', 'r'},
				{'x'},
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(8, cg.charArea('a'));
		assertEquals(12, cg.charArea('x'));
		assertEquals(3, cg.charArea('r'));
	}
	public void testCountPlus1(){
		char[][] grid = new char[][]{
				{' ', ' ', 'p'},
				{' ', ' ', 'p', ' ', ' ', ' ', ' ', 'x'},
				{'p', 'p', 'p', 'p', 'p', ' ', 'x', 'x', 'x'},
				{' ', ' ', 'p', ' ', ' ', 'y', ' ', 'x'},
				{' ', ' ', 'p', ' ', 'y', 'y', 'y'},
				{'z', 'z', 'z', 'z', 'z', 'y', 'z', 'z', 'z'},
				{' ', ' ', 'x', 'x', ' ', 'y'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(2, cg.countPlus());
	}

	public void testCountPlus2(){
		char[][] grid = new char[][]{
				{'b','a','b'},
				{'a','a','a'},
				{'b','a','a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());
	}

	public void testCountPlus3(){
		char[][] grid = new char[][]{
				{'t','a','b','a','w','z','a'},
				{'a','s','b','q','e','s','a'},
				{'b','b','b','b','b','d','a'},
				{'a','r','b','a','a','e','a'},
				{'a','b','b','a','a','q','a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());
	}

	public void testCountPlus4(){
		char[][] grid = new char[][]{
				{'a','a','p','a','a','a','a','a','a'},
				{'a','a','p','a','a','a','a','x','a'},
				{'p','p','p','p','p','a','x','x','x'},
				{'a','a','p','a','a','y','a','x','a'},
				{'a','a','p','a','y','y','y','a','a'},
				{'z','z','z','z','z','y','z','z','z'},
				{'a','a','x','x','a','y','a','a','a'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(2, cg.countPlus());
	}

	public void testCountPlus5(){
		char[][] grid = new char[][]{
				{'r','t','z','x','m','n','a','e'},
				{'x','a','x','x','x','a','x','x'},
				{'a','a','a','x','a','a','a','x'},
				{'x','a','x','a','x','a','x','a'},
				{'k','r','s','t','v','w','z','k'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(3, cg.countPlus());
	}

	public void testCountPlus6(){
		char[][] grid = new char[][]{
				{'x','x','x','x','x','x','x','x'},
				{'x','x','x','x','x','x','x','x'},
				{'x','x','x','x','x','x','x','x'},
				{'x','x','x','x','x','x','x','x'},
				{'x','x','x','x','x','x','x','x'},
				{'x','x','x','x','x','x','x','x'}
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.countPlus());
	}

	public void testCountPlus7(){
		char[][] grid = new char[][]{
				{'i','i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i','i'},
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(1, cg.countPlus());
	}

	public void testCountPlus8(){
		char[][] grid = new char[][]{
				{'i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i'},
				{'i','i','i','i','i','i','i','i'},
		};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.countPlus());
	}
	public void testCountPlus9(){
		char[][] grid = new char[][]{};
		CharGrid cg = new CharGrid(grid);
		assertEquals(0, cg.countPlus());
	}
}
