// Cracker.java
/*
 Generates SHA hashes of short strings in parallel.
*/

import java.security.*;
import java.util.Arrays;
import java.util.concurrent.CountDownLatch;

public class Cracker {
	public class Worker extends Thread{
		private final int left;
		private final int right;
		private final byte[] target;
		private final MessageDigest md;

		public Worker(int startIndex, int lastIndex, byte[] target){
			this.left = startIndex;
			this.right = lastIndex;
			this.target = target;
			try {
				md  = MessageDigest.getInstance("SHA");
			} catch (NoSuchAlgorithmException e) {throw new RuntimeException(e);}
		}

		public void run(){
			checkEveryPassword();
			latch.countDown();
		}

		private void checkEveryPassword(){
			for (int i = left; i < right; i++) {
				StringBuilder soFar = new StringBuilder(String.valueOf(CHARS[i]));
				generatePassword(soFar);
			}
		}

		private void generatePassword(StringBuilder soFar){
			if (soFar.length() > maxLength) return;

			md.update(soFar.toString().getBytes());
			if (Arrays.equals(md.digest(), target)) System.out.println(soFar);

			for (char ch : CHARS) {
				soFar.append(ch);
				generatePassword(soFar);
				soFar.deleteCharAt(soFar.length() - 1);
			}
		}
	}

	// Array of chars used to produce strings
	public static final char[] CHARS = "abcdefghijklmnopqrstuvwxyz0123456789.,-!".toCharArray();
	private final CountDownLatch latch;
	private final byte[] target;
	private final int maxLength;
	private final int numWorkers;


	public Cracker(int num, int len, String target){
		latch = new CountDownLatch(num);
		numWorkers = num;
		maxLength  = len;
		this.target = hexToArray(target);
	}

	/*
     Given a byte[] array, produces a hex String,
     such as "234a6f". with 2 chars for each byte in the array.
     (provided code)
    */
	public static String hexToString(byte[] bytes) {
		StringBuffer buff = new StringBuffer();
		for (int i=0; i<bytes.length; i++) {
			int val = bytes[i];
			val = val & 0xff;  // remove higher bits, sign
			if (val<16) buff.append('0'); // leading 0
			buff.append(Integer.toString(val, 16));
		}
		return buff.toString();
	}

	/*
     Given a string of hex byte values such as "24a26f", creates
     a byte[] array of those values, one byte value -128..127
     for each 2 chars.
     (provided code)
    */
	public static byte[] hexToArray(String hex) {
		byte[] result = new byte[hex.length()/2];
		for (int i=0; i<hex.length(); i+=2) {
			result[i/2] = (byte) Integer.parseInt(hex.substring(i, i+2), 16);
		}
		return result;
	}

	private void startWorkers(){
		int part = CHARS.length / numWorkers;
		for(int i = 0; i < CHARS.length; i += part){
			int lastIndex = Math.min(i + part, CHARS.length);
			Worker worker = new Worker(i, lastIndex, target);
			worker.start();
		}
	}

	private void crack(){
		startWorkers();
		try{
			latch.await();
		} catch (InterruptedException e) {throw new RuntimeException(e);}
		System.out.println("All Done");
	}


	public static void main(String[] args){
		if (args.length < 1) {
			System.out.println("Args: target length [workers]");
			return;
		}
		// args: targ len [num]
		String targ = args[0];
		int len = Integer.parseInt(args[1]);
		int num = 1;
		if (args.length>2) {
			num = Integer.parseInt(args[2]);
		}
		// a! 34800e15707fae815d7c90d49de44aca97e2d759
		// xyz 66b27417d37e024c46526c2f6d358a754fc552f3
		Cracker cracker = new Cracker(num, len, targ);
		cracker.crack();
	}
}