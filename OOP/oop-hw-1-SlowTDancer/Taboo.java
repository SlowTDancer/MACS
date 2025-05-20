
/*
 HW1 Taboo problem class.
 Taboo encapsulates some rules about what objects
 may not follow other objects.
 (See handout).
*/

import java.util.*;

public class Taboo<T> {
	private HashMap<T, HashSet<T>> data;
	/**
	 * Constructs a new Taboo using the given rules (see handout.)
	 * @param rules rules for new Taboo
	 */
	public Taboo(List<T> rules) {
		data = new HashMap<>();
		for(int i = 0; i < rules.size() - 1; i++){
			if(rules.get(i + 1) == null) continue;
			HashSet<T> temp;
			if(data.containsKey(rules.get(i))){
				temp = data.get(rules.get(i));
			}else{
				temp = new HashSet<>();
			}
			temp.add(rules.get(i + 1));
			data.put(rules.get(i), temp);
		}
	}
	
	/**
	 * Returns the set of elements which should not follow
	 * the given element.
	 * @param elem
	 * @return elements which should not follow the given element
	 */
	public Set<T> noFollow(T elem) {
		Set<T> res = new HashSet<>();
		if(data.containsKey(elem)) res = data.get(elem);
		return res; // YOUR CODE HERE
	}
	
	/**
	 * Removes elements from the given list that
	 * violate the rules (see handout).
	 * @param list collection to reduce
	 */
	public void reduce(List<T> list) {
		int counter = list.size() - 1;
		int index = 0;
		while(index < counter){
			if(noFollow(list.get(index)).contains(list.get(index + 1))){
				list.remove(index + 1);
				counter--;
			}else{
				index++;
			}
		}
	}
}
