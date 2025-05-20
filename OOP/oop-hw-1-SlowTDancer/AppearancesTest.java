import junit.framework.TestCase;
import org.junit.jupiter.api.Test;

import java.util.*;

public class AppearancesTest extends TestCase {
	// utility -- converts a string to a list with one
	// elem for each char.
	private List<String> stringToList(String s) {
		List<String> list = new ArrayList<String>();
		for (int i=0; i<s.length(); i++) {
			list.add(String.valueOf(s.charAt(i)));
			// note: String.valueOf() converts lots of things to string form
		}
		return list;
	}

	private Set<Integer> fillSet(int offset, int elems){
		Set<Integer> res = new HashSet<>();
		for(int i = offset; i < offset + elems; i++){
			res.add(i);
		}
		return res;
	}

	public void testSameCount1() {
		List<String> a = stringToList("abbccc");
		List<String> b = stringToList("cccbba");
		assertEquals(3, Appearances.sameCount(a, b));
	}

	public void testSameCount2() {
		// basic List<Integer> cases
		List<Integer> a = Arrays.asList(1, 2, 3, 1, 2, 3, 5);
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 9, 9, 1)));
		assertEquals(2, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1)));
		assertEquals(1, Appearances.sameCount(a, Arrays.asList(1, 3, 3, 1, 1)));
	}

	//
	//Test empty list
	//
	public void testSameCount3(){
		List<String> temp = new ArrayList<>();
		assertEquals(0, Appearances.sameCount(Arrays.asList("6", "6"), temp));
		assertEquals(0, Appearances.sameCount(temp, temp));
		assertEquals(0, Appearances.sameCount(temp, Arrays.asList("Ruska","Ninna", "Nikura")));
	}

	//
	//test set
	//
	public void testSameCount4(){
		int elems = 10;
		int offset = 5;
		Set<Integer> s1 = fillSet(0, elems);
		Set<Integer> s2 = fillSet(offset, elems);
		assertEquals(elems - offset, Appearances.sameCount(s1,s2));
	}

	public void testSameCount5(){
		int elems = 1000;
		int offset = 50;
		Set<Integer> s1 = fillSet(0, elems);
		Set<Integer> s2 = fillSet(offset, elems);
		assertEquals(elems - offset, Appearances.sameCount(s1,s2));
	}
}
