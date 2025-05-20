import junit.framework.TestCase;
import org.apache.commons.dbcp2.BasicDataSource;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetropolisDatabase {
    private final Connection connection;
    private final String dbName;
    private final String tableName;
    public static final int EXACT_MATCH = 0;
    public static final int POPULATION_LARGER = 0;

    public MetropolisDatabase(String tableName, String dbName, String userName, String password) throws ClassNotFoundException, SQLException {
        this.tableName = tableName;
        this.dbName = dbName;
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/" + dbName);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);

        connection = dataSource.getConnection();
    }

    private boolean isName(String val){
        return !(val.contains(";") || val.contains("'") || val.contains("--") || val.contains("\\") || val.contains("\""));
    }

    private boolean isNumber(String val){
        if(val.equals("")) return false;
        if(val.charAt(0) == '0') return false;
        for(int i = 0; i < val.length(); i++){
            if(!Character.isDigit(val.charAt(i))) return false;
        }
        return true;
    }

    private boolean isValid(Metropolis val){
        return isName(val.getMetropolis()) && isName(val.getContinent()) && isNumber(val.getPopulation());
    }

    public boolean addMetropolis(Metropolis val) throws SQLException {
        if(!isValid(val) || val.getContinent().equals("")|| val.getMetropolis().equals("")) return false;
        Statement statement = connection.createStatement();

        statement.execute("USE " + dbName + ";\n");

        StringBuilder code = new StringBuilder();
        code.append("insert into " + tableName + " ");
        code.append("values(\"").append(val.getMetropolis()).append("\", ");
        code.append("\"").append(val.getContinent()).append("\", ");
        code.append(val.getPopulation()).append(")").append(";");
        statement.executeUpdate(code.toString());
        statement.close();
        return true;
    }

    private String getSearchCode(Metropolis val, int populationType, int matchType){
        StringBuilder res = new StringBuilder();
        res.append("select * from " + tableName + "\n");
        if(val.getMetropolis().equals("") && val.getContinent().equals("")) return res.toString();
        boolean check = val.getMetropolis().equals("") || val.getContinent().equals("");
        res.append("where ");

        if(matchType == EXACT_MATCH){
            if(!val.getMetropolis().equals("")) res.append("\""+ val.getMetropolis() + "\" = " + tableName +".metropolis");
            if(!check) res.append(" and ");
            if(!val.getContinent().equals("")) res.append(tableName + ".continent = " + "\""+ val.getContinent() + "\"");
        }else{
            if(!val.getMetropolis().equals("")) res.append(tableName + ".metropolis" + " like \"%"+ val.getMetropolis() + "%\"");
            if(!check) res.append(" and ");
            if(!val.getContinent().equals("")) res.append(tableName + ".continent" + " like \"%"+ val.getContinent() + "%\"");
        }

        if(val.getPopulation().equals("")) return res.toString() + ";";

        res.append(" and ");
        if(populationType == POPULATION_LARGER){
            res.append(val.getPopulation() + " < " + tableName + ".population;");
        }else{
            res.append(val.getPopulation() + " > " + tableName + ".population;");
        }
        return res.toString();
    }

    public ArrayList<Metropolis> search(Metropolis val, int populationType, int matchType) throws SQLException {
        ArrayList<Metropolis> res = new ArrayList<>();
        if(!isValid(val) && !val.getPopulation().equals("")) return res;
        Statement statement = connection.createStatement();

        statement.execute("USE " + dbName + ";\n");

        String code = getSearchCode(val, populationType, matchType);
        ResultSet resultSet = statement.executeQuery(code);
        while(resultSet.next()){
            String metropolis = resultSet.getString("Metropolis");
            String continent = resultSet.getString("Continent");
            String population = resultSet.getString("Population");
            res.add(new Metropolis(metropolis, continent, population));
        }
        statement.close();
        return res;
    }
}