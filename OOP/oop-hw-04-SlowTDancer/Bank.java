// Bank.java

/*
 Creates a bunch of accounts and uses threads
 to post transactions to the accounts concurrently.
*/

import java.io.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

public class Bank {
	public class Worker extends Thread{
		@Override
		public void run(){
			while(true){
				try{
					Transaction curr = bq.take();
					if(curr == nullTrans){
						bq.put(curr);
						latch.countDown();
						break;
					}
					doTransaction(curr);
				} catch (InterruptedException e) {throw new RuntimeException(e);}
			}
		}
	}

	public static final int ACCOUNTS = 20;     // number of accounts
	public static final int INIT_BALANCE = 1000;
	private final CountDownLatch latch;
	private final List<Account> accs;
	private final BlockingQueue<Transaction> bq;
	private final Transaction nullTrans = new Transaction(-1, 0, 0);


	public Bank(int numWorkers){
		bq = new ArrayBlockingQueue<>(Buffer.SIZE);
		latch = new CountDownLatch(numWorkers);
		accs = new ArrayList<>();
		for(int i = 0; i < ACCOUNTS; i++){
			Account account = new Account(this, i, INIT_BALANCE);
			accs.add(account);
		}
	}

	public List<Account> getAccounts(){
		return accs;
	}

	private void doTransaction(Transaction t){
		Account from = accs.get(t.from);
		Account to = accs.get(t.to);
		from.subtractAmount(t.amount);
		to.addAmount(t.amount);
	}

	/*
     Reads transaction data (from/to/amt) from a file for processing.
     (provided code)
     */
	public void readFile(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));

			// Use stream tokenizer to get successive words from file
			StreamTokenizer tokenizer = new StreamTokenizer(reader);

			while (true) {
				int read = tokenizer.nextToken();
				if (read == StreamTokenizer.TT_EOF) {
					bq.put(nullTrans);
					break;
				}
				int from = (int)tokenizer.nval;

				tokenizer.nextToken();
				int to = (int)tokenizer.nval;

				tokenizer.nextToken();
				int amount = (int)tokenizer.nval;

				bq.put(new Transaction(from, to, amount));
			}
		} catch (Exception e) {e.printStackTrace(); System.exit(1);}
	}

	private void startWorkers(int numWorkers){
		for(int i = 0; i < numWorkers; i++){
			Worker worker = this.new Worker();
			worker.start();
		}
	}

	/*
     Processes one file of transaction data
     -fork off workers
     -read file into the buffer
     -wait for the workers to finish
    */
	public void processFile(String file, int numWorkers) {
		startWorkers(numWorkers);
		readFile(file);
		try{
			latch.await();
		} catch (InterruptedException e) {throw new RuntimeException(e);}
		for (Account acc : accs) System.out.println(acc);
	}


	/*
     Looks at commandline args and calls Bank processing.
    */
	public static void main(String[] args) {
		// deal with command-lines args
		if (args.length == 0) {
			System.out.println("Args: transaction-file [num-workers [limit]]");
			return;
		}

		String file = args[0];
		int numWorkers = 1;
		if (args.length >= 2) {
			numWorkers = Integer.parseInt(args[1]);
		}
		Bank bank = new Bank(numWorkers);
		bank.processFile(file, numWorkers);
	}
}

