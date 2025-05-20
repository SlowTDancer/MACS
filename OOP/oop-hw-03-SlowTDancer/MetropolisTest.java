import junit.framework.TestCase;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MetropolisTest extends TestCase {
    private final String[] first = new String[]{"m", "c", "1"};
    private final String[] second = new String[]{"a", "b", "2"};
    private final String[] third = new String[]{"x", "y", "3"};

    public void testInit(){
        Assertions.assertDoesNotThrow(()->{
            Metropolis temp1 = new Metropolis(first[0], first[1], first[2]);
            Metropolis temp2 = new Metropolis(second[0], second[1], second[2]);
            Metropolis temp3 = new Metropolis(third[0], third[1], third[2]);
        });
    }

    public void testGet(){
        Metropolis getter = new Metropolis(second[0], second[1], second[2]);
        assertEquals("a", getter.get(0));
        assertEquals("b", getter.get(1));
        assertEquals("2", getter.get(2));
        Assertions.assertThrows(RuntimeException.class, ()->{
            getter.get(3);
        });
    }

    public void testGetMetropolis(){
        Metropolis getMetr = new Metropolis(third[0], third[1], third[2]);
        assertEquals("x", getMetr.getMetropolis());
    }

    public void testGetContinent(){
        Metropolis getCont = new Metropolis(first[0], first[1], first[2]);
        assertEquals("c", getCont.getContinent());
    }

    public void testGetPopulation(){
        Metropolis getPop = new Metropolis(second[0], second[1], second[2]);
        assertEquals("2", getPop.getPopulation());
    }
}