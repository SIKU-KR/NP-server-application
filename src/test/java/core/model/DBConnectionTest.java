package core.model;

import core.common.DBConnection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {
    private DBConnection dbConnection = new DBConnection();

    @Test
    void testConnectionPool() {
        try (Connection connection = dbConnection.getConnection()) {
            assertNotNull(connection, "Connection should not be null");
            assertFalse(connection.isClosed(), "Connection should be open");
        } catch (SQLException e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
}