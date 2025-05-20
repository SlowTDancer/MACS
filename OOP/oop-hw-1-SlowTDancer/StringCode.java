import javax.swing.plaf.synth.SynthOptionPaneUI;
import java.beans.IntrospectionException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

// CS108 HW1 -- String static methods

public class StringCode {
	private final static int BS = 26;
	private final static int MOD = 97;
	/**
	 * Given a string, returns the length of the largest run.
	 * A a run is a series of adajcent chars that are the same.
	 * @param str
	 * @return max run length
	 */
	public static int maxRun(String str) {
		if(str.length() == 0) return 0;
		int res = 1;
		int count = 1;
		char ch = str.charAt(0);
		for(int i = 1; i < str.length(); i++){
			if(ch == str.charAt(i)){
				count++;
			}else{
				res = Math.max(res, count);
				count = 1;
				ch = str.charAt(i);
			}
		}
		res = Math.max(res, count);
		return res;
	}

	private static boolean isDigit(char ch){
		return ch >= '0' && ch <= '9';
	}
	/**
	 * Given a string, for each digit in the original string,
	 * replaces the digit with that many occurrences of the character
	 * following. So the string "a3tx2z" yields "attttxzzz".
	 * @param str
	 * @return blown up string
	 */
	public static String blowup(String str) {
		String res = "";
		int len = str.length();
		for(int i = 0; i < str.length(); i++){
			char ch = str.charAt(i);
			if(isDigit(ch)){
				if(i == str.length() - 1){
					continue;
				}
				int dig = ch - '0';
				for(int j = 0; j < dig; j++){
					res += str.charAt(i + 1);
				}
			}else{
				res += ch;
			}
		}
		return res;
	}


	private static int getCharIndex(char ch){
		return (int)ch % MOD;
	}

	private static int binpow(int x, int n){
		int answer = 1;
		while(n > 0){
			if((n & 1) != 0) answer = (answer * x) % MOD;
			x = (x * x) % MOD;
			n = n >> 1;
		}
		return answer;
	}

	private static HashSet<Integer> hashA(String a, int len, int exp){
		HashSet<Integer> hs = new HashSet<>();
		int hash = 0;
		for(int i = 0; i < len; i++){
			hash = (hash * BS) % MOD;
			hash = (hash + getCharIndex(a.charAt(i))) % MOD;
		}
		hs.add(hash);
		for(int i = len; i < a.length(); i++){
			hash = (hash - (exp * getCharIndex(a.charAt(i - len))) % MOD) % MOD;
			hash = (hash * BS) % MOD;
			hash = (hash + getCharIndex(a.charAt(i))) % MOD;
			if(hash < 0) hash += MOD;
			hs.add(hash);
		}
		return hs;
	}

	private static boolean check(int hash, HashSet<Integer> hs, String a, String b, int len, int x){
		if(hs.contains(hash)){
			String temp = "";
			for(int i = x; i < x + len; i++){
				temp += b.charAt(i);
			}
			if(a.contains(temp)) return true;
		}
		return false;
	}
	/*
	 * Given 2 strings, consider all the substrings within them
	 * of length len. Returns true if there are any such substrings
	 * which appear in both strings.
	 * Compute this in linear time using a HashSet. Len will be 1 or more.
	 */
	public static boolean stringIntersect(String a, String b, int len) {
		if(len > a.length() || len > b.length()) return false;
		int exp = binpow(BS, len - 1);
		HashSet<Integer> hs = hashA(a, len, exp);
		int hash = 0;
		for(int i = 0; i < len; i++){
			hash = (hash * BS) % MOD;
			hash = (hash + getCharIndex(b.charAt(i))) % MOD;
		}
		if(check(hash, hs, a, b, len, 0)) return true;
		for(int i = len; i < b.length(); i++){
			hash = (hash - (exp * getCharIndex(b.charAt(i - len))) % MOD) % MOD;
			hash = (hash * BS) % MOD;
			hash = (hash + getCharIndex(b.charAt(i))) % MOD;
			if(hash < 0) hash += MOD;
			if(check(hash, hs, a, b, len, i - len + 1)) return true;
		}
		return false;
	}
}