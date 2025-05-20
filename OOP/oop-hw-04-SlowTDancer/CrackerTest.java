import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CrackerTest extends TestCase {
    private ByteArrayOutputStream stream;

    private String getLine(OutputStream stream){
        String str = stream.toString();
        return str.substring(0, str.length() - 2);
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        stream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(stream));
    }

    public void testEmpty(){
        Cracker.main(new String[]{});
        assertEquals("Args: target length [workers]", getLine(stream));
    }

    public void testCrack1() {
        Cracker.main(new String[]{"34800e15707fae815d7c90d49de44aca97e2d759", "2","4"});
        assertEquals("a!", getLine(stream).substring(0, getLine(stream).toString().indexOf('\n') - 1));
    }

    public void testCrack2(){
        Cracker.main(new String[]{"34800e15707fae815d7c90d49de44aca97e2d759", "4", "1"});
        assertEquals("a!", getLine(stream).substring(0, getLine(stream).toString().indexOf('\n') - 1));
    }
    public void testCrack3(){
        Cracker.main(new String[]{"66b27417d37e024c46526c2f6d358a754fc552f3", "3", "4"});
        assertEquals("xyz", getLine(stream).substring(0, getLine(stream).toString().indexOf('\n') - 1));
    }

    public void testHexToString(){
        byte[] temp = Cracker.hexToArray("34800e15707fae815d7c90d49de44aca97e2d759");
        assertEquals("34800e15707fae815d7c90d49de44aca97e2d759", Cracker.hexToString(temp));
    }
}