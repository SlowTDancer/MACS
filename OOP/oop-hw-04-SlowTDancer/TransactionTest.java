import junit.framework.TestCase;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTest extends TestCase {

    public void testInit(){
        Assertions.assertDoesNotThrow(new Executable() {
            @Override
            public void execute() throws Throwable {
                Transaction trans = new Transaction(0, 6, 1000);
            }
        });
    }
    public void testTransaction(){
        Transaction trans = new Transaction(23, 13, 300);
        assertEquals(23, trans.from);
        assertEquals(13, trans.to);
        assertEquals(300, trans.amount);
    }
    public void testToString() {
        Transaction trans = new Transaction(23, 13, 300);
        assertEquals("from:23 to:13 amt:300", trans.toString());
    }
}