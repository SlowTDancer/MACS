import javax.swing.table.AbstractTableModel;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MetropolisesTable extends AbstractTableModel {
    private List<Metropolis> currData;
    private final MetropolisDatabase mdb;
    private final String[] cols;

    public MetropolisesTable(String tableName, String dbName, String user, String password) throws SQLException, ClassNotFoundException {
        currData = new ArrayList<>();
        mdb = new MetropolisDatabase(tableName, dbName, user, password);
        cols = new String[]{"Metropolis", "Continent", "Population"};
    }

    @Override
    public int getRowCount() {
        return currData.size();
    }

    @Override
    public int getColumnCount() {
        return cols.length;
    }

    @Override
    public String getColumnName(int index){
        return cols[index];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return currData.get(rowIndex).get(columnIndex);
    }

    public String[] getCols(){
        return cols;
    }
    public void add(Metropolis val) throws SQLException {
        currData.clear();
        if(!mdb.addMetropolis(val)) return;
        currData.add(val);
        fireTableDataChanged();
    }

    public void search(Metropolis val, int populationType, int matchType) throws SQLException {
        currData = mdb.search(val, populationType, matchType);
        fireTableDataChanged();
    }
}
