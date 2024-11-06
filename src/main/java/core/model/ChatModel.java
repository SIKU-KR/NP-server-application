package core.model;

import core.common.AppLogger;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/**
 * Access to Message table in MySQL dbms.
 */
public class ChatModel extends DBConnection {

    public void createNewMsg(Integer chatId, Integer userId, String text){
        String sql = "INSERT INTO Message (text, chat_id, user_id, timestamp) VALUES (?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, text);
            pstmt.setInt(2, chatId);
            pstmt.setInt(3, userId);
            pstmt.setTimestamp(4, timestamp);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to insert message");
            }
        } catch(SQLException e) {
            AppLogger.error(e.getMessage());
        }
    }

}
