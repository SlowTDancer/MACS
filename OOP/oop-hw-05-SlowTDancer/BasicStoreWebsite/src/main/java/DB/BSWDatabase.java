package DB;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.*;
import java.util.ArrayList;

public class BSWDatabase {
    private Connection connection;
    private final static String USERNAME = "root";
    private final static String PASSWORD = "Ikako2525";
    private final static String DBNAME = "SITHET";
    private ArrayList<Product> products;

    public BSWDatabase() throws SQLException, ClassNotFoundException {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/" + DBNAME);
        dataSource.setUsername(USERNAME);
        dataSource.setPassword(PASSWORD);
        Class.forName("com.mysql.cj.jdbc.Driver");
        connection = dataSource.getConnection();
    }
    public ArrayList<Product> getProductList() throws SQLException {
        Statement searchStatement = connection.createStatement();
        Statement statement = connection.createStatement();
        statement.execute("USE " + DBNAME + ";\n");
        String query = "SELECT * FROM products";
        ResultSet info = searchStatement.executeQuery(query);
        products = new ArrayList<>();
        while (info.next()) {
            String productId = info.getString(1);
            String productName = info.getString(2);
            String imageName = info.getString(3);
            double price = Double.parseDouble(info.getString(4));
            Product p = new Product(productId, productName, imageName, price);
            products.add(p);
        }
        return products;
    }

    public Product getProduct(String productId) throws SQLException {
        if(products == null) products = getProductList();
        for(Product p : products){
            if(p.getProductId().equals(productId)) {
                return new Product(p.getProductId(), p.getProductName(), p.getImageFileName(), p.getProductPrice());
            }
        }
        return null;
    }
}
