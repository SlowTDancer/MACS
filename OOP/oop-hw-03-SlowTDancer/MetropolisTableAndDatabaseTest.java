import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class MetropolisTableAndDatabaseTest extends TestCase {
    private static final String TABLE_NAME = "testTable";
    private static final String DB_NAME = "";
    private static final String USER_NAME = "";
    private static final String PASSWORD = "";
    public static final int EXACT_MATCH = 0;
    public static final int PARTIAL_MATCH = 1;
    public static final int POPULATION_LARGER = 0;
    public static final int POPULATION_SMALLER = 1;

    private final Metropolis[] entries = {new Metropolis("New York", "North America", "21295000"),
            new Metropolis("Mumbai", "Asia", "20400000"),
            new Metropolis("San Francisco", "North America", "5780000"),
            new Metropolis("London", "Europe", "8580000"),
            new Metropolis("Rome", "Europe", "2715000"),
            new Metropolis("Melbourne", "Australia", "3900000"),
            new Metropolis("Tokyo", "Asia", "1200000"),
            new Metropolis("giorgi", "Dzadzamia", "10")
            };

    private MetropolisesTable tb;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/" + DB_NAME);
        dataSource.setUsername(USER_NAME);
        dataSource.setPassword(PASSWORD);
        Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        statement.execute("USE " + DB_NAME + ";\n");
        statement.execute("DROP TABLE IF EXISTS " + TABLE_NAME);
        statement.execute("CREATE TABLE " + TABLE_NAME + " (metropolis CHAR(64),\n" +
                " continent CHAR(64),\n" +
                " population BIGINT);");
        statement.close();
        tb = new MetropolisesTable(TABLE_NAME, DB_NAME, USER_NAME, PASSWORD);
    }

    private Metropolis getMetropolis(int index, MetropolisesTable tb) {
        String[] temp = new String[tb.getCols().length];
        for(int j = 0; j < tb.getCols().length; j++){
            temp[j] = (String)tb.getValueAt(index, j);
        }
        return new Metropolis(temp[0], temp[1], temp[2]);
    }

    public void testEmpty() {
        assertEquals(0, tb.getRowCount());
        assertEquals(tb.getCols().length, tb.getColumnCount());
        for(int i = 0; i < tb.getCols().length; i++){
            assertEquals(tb.getCols()[i], tb.getColumnName(i));
        }
    }

    public void testAdd1() throws SQLException {
        tb.add(new Metropolis("New York", "North America", "21295000"));
        assertEquals(1, tb.getRowCount());
        assertEquals(tb.getCols().length, tb.getColumnCount());
        assertEquals("New York", tb.getValueAt(0, 0));
        assertEquals("North America", tb.getValueAt(0, 1));
        assertEquals("21295000", tb.getValueAt(0, 2));
        tb.add(new Metropolis("Mumbai", "Asia", "20400000"));
        assertEquals(1, tb.getRowCount());
        assertEquals(tb.getCols().length, tb.getColumnCount());
        assertEquals("Mumbai", tb.getValueAt(0, 0));
        assertEquals("Asia", tb.getValueAt(0, 1));
        assertEquals("20400000", tb.getValueAt(0, 2));
    }

    public void testAdd2() throws SQLException {
        for (int i = 0; i < 4; i++) {
            tb.add(entries[i]);
            for (int j = 0; j < tb.getCols().length; j++) {
                assertEquals(entries[i].get(j), tb.getValueAt(0, j));
            }
        }
    }

    public void testSearchWithoutOptions() throws SQLException {
        List<Metropolis> addedList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            tb.add(entries[i]);
            addedList.add(entries[i]);
        }
        tb.search(new Metropolis("", "", ""), POPULATION_LARGER, EXACT_MATCH);
        assertEquals(4,tb.getRowCount());
        for (int i = 0; i < 4; i++) {
            Metropolis searchedEntry = getMetropolis(i, tb);
            assertTrue(addedList.contains(searchedEntry));
        }
        tb.add(entries[4]);
        assertEquals(1, tb.getRowCount());
        Metropolis firstEntry = getMetropolis(0,tb);
        assertEquals(entries[4], firstEntry);
    }

    public void testSearch() throws SQLException {
        for(int i = 0; i < 8; i++) tb.add(entries[i]);
        tb.search(new Metropolis("a", "", "400"), POPULATION_LARGER, PARTIAL_MATCH);
        assertEquals(2, tb.getRowCount());
        tb.search(new Metropolis("", "Asia", "10000000"), POPULATION_SMALLER, EXACT_MATCH);
        assertEquals(1, tb.getRowCount());
        assertEquals(entries[6], getMetropolis(0, tb));
        tb.search(new Metropolis("T", "", "20000"), POPULATION_LARGER, PARTIAL_MATCH);
        assertEquals(1, tb.getRowCount());
        assertEquals(entries[6], getMetropolis(0, tb));
    }

    public void testInvalid(){
        assertThrows(RuntimeException.class, () -> tb.add(entries[12]));
    }
}