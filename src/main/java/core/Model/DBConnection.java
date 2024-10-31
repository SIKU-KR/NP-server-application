package core.Model;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * DBConnection is Abstract class to connect database(MySQL)
 * Must be inherited to use connection to database
 * File "/src/main/resources/application.properties" configures connection information.
 */

public abstract class DBConnection {
    private String url;
    private String user;
    private String password;

    public DBConnection() {
        loadDatabaseConfig();
    }

    private void loadDatabaseConfig() {
        Properties properties = new Properties();
        try (FileInputStream input = new FileInputStream("src/main/resources/application.properties")) {
            properties.load(input);
            this.url = properties.getProperty("db.url");
            this.user = properties.getProperty("db.username");
            this.password = properties.getProperty("db.password");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) {
        try(Connection connection = DriverManager.getConnection(url, user, password)){
            Statement statement = connection.createStatement();
            return statement.executeQuery(query);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
