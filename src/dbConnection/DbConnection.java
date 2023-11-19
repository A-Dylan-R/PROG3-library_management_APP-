package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.FileInputStream;
import java.io.IOException;

public class DbConnection {
    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();
        FileInputStream fis = null;
        Connection connection = null;

        try {
            fis = new FileInputStream("C:\\Users\\lanx4\\IdeaProjects\\JDBC_prog3\\src\\db.properties");
            props.load(fis);

            String url = props.getProperty("url");
            String user = props.getProperty("user");
            String password = props.getProperty("password");

            connection = DriverManager.getConnection(url, user, password);

            System.out.println("Connection successful!");

        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (IOException ex) {
                System.err.println("Error closing FileInputStream: " + ex.getMessage());
            }
        }

        return connection;
    }
}
