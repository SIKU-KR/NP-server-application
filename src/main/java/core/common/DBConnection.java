package core.common;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * DBConnection with HikariCP for connection pooling.
 */
public class DBConnection {
    private static HikariDataSource dataSource;

    static {
        try {
            Properties properties = new Properties();
            try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("application.properties")) {
                if (input == null) {
                    throw new RuntimeException("application.properties not found in classpath");
                }
                properties.load(input);
            }

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password"));
            config.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.hikari.maximumPoolSize", "10")));
            config.setMinimumIdle(Integer.parseInt(properties.getProperty("db.hikari.minimumIdle", "2")));
            config.setIdleTimeout(Long.parseLong(properties.getProperty("db.hikari.idleTimeout", "30000")));
            config.setConnectionTimeout(Long.parseLong(properties.getProperty("db.hikari.connectionTimeout", "30000")));
            config.setMaxLifetime(Long.parseLong(properties.getProperty("db.hikari.maxLifetime", "1800000")));

            dataSource = new HikariDataSource(config);
            AppLogger.info("HikariCP DataSource initialized");
        } catch (Exception e) {
            AppLogger.error("Failed to initialize HikariCP DataSource: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            System.out.println("HikariCP DataSource closed");
        }
    }
}