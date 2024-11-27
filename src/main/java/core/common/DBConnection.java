package core.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

/**
 * DBConnection is class to connect database(MySQL)
 * Must be inherited to use connection to database
 * File "/src/main/resources/application.properties" configures connection information.
 */

public abstract class DBConnection {
    private String url;
    private String user;
    private String password;

    public DBConnection() {
        loadDatabaseConfig();
        System.out.println("Connected to the database");
    }

    private void loadDatabaseConfig() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new IOException("application.properties not found in classpath");
            }
            properties.load(input);
            this.url = properties.getProperty("db.url");
            this.user = properties.getProperty("db.username");
            this.password = properties.getProperty("db.password");
        } catch (IOException e) {
            AppLogger.error("Failed to load database configuration: " + e.getMessage());
        }
    }

    public List<Map<String, Object>> executeQuery(String query) {
        List<Map<String, Object>> results = new ArrayList<>();
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            int columnCount = resultSet.getMetaData().getColumnCount();
            while (resultSet.next()) {
                Map<String, Object> row = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    row.put(resultSet.getMetaData().getColumnName(i), resultSet.getObject(i));
                }
                results.add(row);
            }
        } catch (Exception e) {
            AppLogger.error(e.getMessage());
        }
        return results;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }
}

