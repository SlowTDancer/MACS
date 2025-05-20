import java.util.*;

public class Appearances {
	private static <T> HashMap<T, Integer> fillData(Collection<T> c){
		HashMap<T, Integer> res = new HashMap<>();
		for(T elem: c){
			if(!res.containsKey(elem)){
				res.put(elem, 1);
			}else{
				Integer val = res.get(elem) + 1;
				res.replace(elem, val);
			}
		}
		return res;
	}
	/**
	 * Returns the number of elements that appear the same number
	 * of times in both collections. Static method. (see handout).
	 * @return number of same-appearance elements
	 */
	public static <T> int sameCount(Collection<T> a, Collection<T> b) {
		int res = 0;
		HashMap<T, Integer> Amap = fillData(a);
		HashMap<T, Integer> Bmap = fillData(b);
		for(HashMap.Entry<T, Integer> p: Amap.entrySet()){
			if(Bmap.containsKey(p.getKey()) && p.getValue().equals(Bmap.get(p.getKey()))){
				res++;
			}
		}
		return res;
	}
	
}
