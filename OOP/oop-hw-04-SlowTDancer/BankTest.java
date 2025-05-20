import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.List;

public class BankTest extends TestCase {
    public void testSmall(){
        Bank singleThread = new Bank(1);
        singleThread.processFile("small.txt", 1);
        Bank small = new Bank(200);
        small.processFile("small.txt", 200);
        equalAccounts(small, singleThread);
    }

    public void test5k(){
        Bank single = new Bank(1);
        single.processFile("5k.txt", 1);
        Bank mid = new Bank(60);
        mid.processFile("5k.txt", 60);
        equalAccounts(single, mid);
    }

    public void test100k(){
        Bank single = new Bank(1);
        single.processFile("100k.txt", 1);
        Bank large = new Bank(600);
        large.processFile("100k.txt", 600);
        equalAccounts(single, large);
    }

    public void testMain(){
        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                Bank.main(new String[]{"100k.txt", "200"});
            }
        });
    }

    public void testMainEmpty() throws InterruptedException{
        OutputStream os = new ByteArrayOutputStream();
        System.setOut(new PrintStream(os));
        Bank.main(new String[]{});
        String out = os.toString();
        out = out.substring(0, out.length() - 2);
        assertEquals("Args: transaction-file [num-workers [limit]]", out);
    }

    private void equalAccounts(Bank bank1, Bank bank2) {
        List<Account> actual = bank1.getAccounts();
        List<Account> expected = bank2.getAccounts();
        assertEquals(Bank.ACCOUNTS, expected.size());
        assertEquals(expected.size(), actual.size());
        for(int i = 0; i < actual.size(); i++){
            assertEquals(expected.get(i).toString(), actual.get(i).toString());
        }
    }
}