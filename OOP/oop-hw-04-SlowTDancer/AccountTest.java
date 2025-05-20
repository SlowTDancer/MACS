import junit.framework.TestCase;

import static org.junit.jupiter.api.Assertions.*;

public class AccountTest extends TestCase {
    public void test1(){
        Account acc = new Account(new Bank(0), 0,1000);
        acc.subtractAmount(1000);
        assertEquals("acct:0 bal:0 trans:1", acc.toString());
    }

    public void test2(){
        Account acc2 = new Account(new Bank(0), 1, 100);
        acc2.addAmount(100);
        assertEquals("acct:1 bal:200 trans:1", acc2.toString());
    }

    public void test3(){
        Account acc3 = new Account(new Bank(0),2, 400);
        assertEquals("acct:2 bal:400 trans:0", acc3.toString());
    }
}