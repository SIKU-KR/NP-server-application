package core.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DBConnectionTest {
    private DBConnection dbConnection;

    @BeforeEach
    void setUp() {
        dbConnection = new RoomModel();
    }

    @Test
    void testExecuteQuerySuccess() {
        String query = "SELECT 1 AS test_col"; // 테스트용 간단한 쿼리
        List<Map<String, Object>> result = dbConnection.executeQuery(query);

        assertNotNull(result, "Result should not be null");
        assertFalse(result.isEmpty(), "Result should contain at least one row");
        assertEquals(1, result.get(0).get("test_col"), "The value of test_col should be 1");
    }

    @Test
    void testExecuteQueryFailure() {
        String invalidQuery = "SELECT * FROM non_existent_table";
        List<Map<String, Object>> result = dbConnection.executeQuery(invalidQuery);
        assertTrue(result.isEmpty(), "Result should be empty for invalid queries");
    }
}