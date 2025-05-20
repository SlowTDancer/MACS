// StringCodeTest
// Some test code is provided for the early HW1 problems,
// and much is left for you to add.

import junit.framework.TestCase;

public class StringCodeTest extends TestCase {
	//
	//blow up
	//
	public void testBlowup1() {
		// basic cases
		assertEquals("xxaaaabb", StringCode.blowup("xx3abb"));
		assertEquals("xxxZZZZ", StringCode.blowup("2x3Z"));
	}

	public void testBlowup2() {
		// things with digits

		// digit at end
		assertEquals("axxx", StringCode.blowup("a2x3"));

		// digits next to each other
		assertEquals("a33111", StringCode.blowup("a231"));

		// try a 0
		assertEquals("aabb", StringCode.blowup("aa0bb"));
	}

	public void testBlowup3() {
		// weird chars, empty string
		assertEquals("AB&&,- ab", StringCode.blowup("AB&&,- ab"));
		assertEquals("", StringCode.blowup(""));

		// string with only digits
		assertEquals("", StringCode.blowup("2"));
		assertEquals("33", StringCode.blowup("23"));
	}

	public void testBlowup4(){
		assertEquals("22", StringCode.blowup("22"));
		assertEquals("222112334443333222", StringCode.blowup("32123432"));
	}
	//
	// maxRun
	//
	public void testRun1() {
		assertEquals(2, StringCode.maxRun("hoopla"));
		assertEquals(3, StringCode.maxRun("hoopllla"));
	}

	public void testRun2() {
		assertEquals(3, StringCode.maxRun("abbcccddbbbxx"));
		assertEquals(0, StringCode.maxRun(""));
		assertEquals(3, StringCode.maxRun("hhhooppoo"));
	}

	public void testRun3() {
		// "evolve" technique -- make a series of test cases
		// where each is change from the one above.
		assertEquals(1, StringCode.maxRun("123"));
		assertEquals(2, StringCode.maxRun("1223"));
		assertEquals(2, StringCode.maxRun("112233"));
		assertEquals(3, StringCode.maxRun("1112233"));
	}
	public void testRun4(){
		assertEquals(9, StringCode.maxRun("naaaaaaaaarutooooo"));
		assertEquals(12, StringCode.maxRun("saaaasuuuuukeeeeeeeeeeee"));
		assertEquals(2, StringCode.maxRun("123321123321123321123312123"));
	}

	//
	// stringIntersect
	//
	public void testIntersect1(){
		assertTrue(StringCode.stringIntersect("ragindaabcrom", "meabcminda", 3));
		assertTrue(StringCode.stringIntersect("kkkkkkkkkkkkkkkk", "kkkkkkk", 7));
		assertFalse(StringCode.stringIntersect("sasukeuchiha", "sasuke", 7));
		assertFalse(StringCode.stringIntersect("aaaaaaaaaaaaaa", "bbbbbbbbbbbbbbbbb", 3));
	}

	public void testIntersect2(){
		assertTrue(StringCode.stringIntersect("aabbccc", "aabcc", 3));
		assertTrue(StringCode.stringIntersect("zzzrearakrrfeg", "sdnfiduabgfvidasnakrrfids", 3));
		assertFalse(StringCode.stringIntersect("zzzzzzzzzz", "rrrrrrrr", 5));
	}

	//
	// edge cases
	//
	public void testIntersect3(){
		assertTrue(StringCode.stringIntersect("", "kamehameha", 0));
		assertTrue(StringCode.stringIntersect("", "", 0));
		assertFalse(StringCode.stringIntersect("", "rasengan", 1));
		assertTrue(StringCode.stringIntersect("boruto", "naruto", 2));
		assertFalse(StringCode.stringIntersect("madara", "hashirama", 5));
		assertFalse(StringCode.stringIntersect("r", "b", 1));
	}
}