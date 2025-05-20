// TabooTest.java
// Taboo class tests -- nothing provided.

import java.util.*;

import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TabooTest extends TestCase {
    private <T> Set<T> fillSet(List<T> a){
        Set<T> res = new HashSet<>();
        res.addAll(a);
        return res;
    }
    public void testNoFollow1(){
        Taboo<String> tb = new Taboo<>(Arrays.asList("a","c","a","b"));
        Set<String> first = fillSet(Arrays.asList("c", "b"));
        Set<String> second = fillSet(Arrays.asList("a"));
        assertEquals(first, tb.noFollow("a"));
        assertEquals(second, tb.noFollow("c"));
        assertEquals(Collections.emptySet(), tb.noFollow("b"));
        assertEquals(Collections.emptySet(), tb.noFollow("x"));
    }

    public void testNoFollow2(){
        Taboo<Integer> tb = new Taboo<>(Arrays.asList(6, 3, 7, 3, 9, 4, 1, 3, 7, 5, 7, 6));
        Set<Integer> six = fillSet(Arrays.asList(3));
        assertEquals(six, tb.noFollow(6));
        Set<Integer> three = fillSet(Arrays.asList(7, 9));
        assertEquals(three, tb.noFollow(3));
        Set<Integer> seven = fillSet(Arrays.asList(3, 5, 6));
        assertEquals(seven, tb.noFollow(7));
        assertEquals(Collections.emptySet(), tb.noFollow(0));
    }

    //
    //taboo empty
    //
    public void testNoFollow3(){
        Taboo<Character> t = new Taboo<>(new ArrayList<Character>());
        assertEquals(Collections.emptySet(), t.noFollow('s'));

    }

    public void testReduce1(){
        Taboo<String> tb = new Taboo<>(Arrays.asList("a","c","a","b"));
        List<String> temp = new LinkedList<>(Arrays.asList("a", "c", "b", "x", "c", "a"));
        tb.reduce(temp);
        assertEquals(Arrays.asList("a","x","c"),temp);
    }

    public void testReduce2(){
        Taboo<String> tb = new Taboo<>(Arrays.asList("r", "u", "s", "k", "a"));
        List<String> list = new ArrayList<>(Arrays.asList("k", "r", "r", "u", "s", "k", "a", "s", "k", "a"));
        tb.reduce(list);
        assertEquals(Arrays.asList("k", "r", "r",  "s", "a", "s", "a"), list);
    }

    //
    //nulls
    //
    public void testReduce3(){
        Taboo<String> tb = new Taboo<>(Arrays.asList("n", "a", null, "r", null, "u", null, "r", "u", null, "t", null, "o"));
        List<String> temp1 = new ArrayList<>(Arrays.asList("n", "a", "r", "u", "r", "r"));
        List<String> temp2 = new ArrayList<>(Arrays.asList(null, null, "n", "a"));
        tb.reduce(temp1);
        assertEquals(Arrays.asList("n", "r", "r", "r"),temp1);
        tb.reduce(temp2);
        assertEquals(Arrays.asList(null, null, "n"), temp2);
    }

    //
    //taboo empty
    //
    public void testReduce4(){
        Taboo<Integer> tb = new Taboo<>(new ArrayList<>());
        List<Integer> temp = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        tb.reduce(temp);
        assertEquals(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9), temp);
    }

    //
    //both empty
    //
    public void testReduce5(){
        Taboo<Integer> tb = new Taboo<>(new ArrayList<>());
        List<Integer> temp = new ArrayList<>();
        tb.reduce(temp);
        assertEquals(temp, new ArrayList<>());
    }
}
