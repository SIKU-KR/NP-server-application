package core.model;

import core.common.AppLogger;
import core.common.DBConnection;
import core.dto.Message;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Access to Message table in MySQL dbms.
 */
public class ChatModel extends DBConnection {

    public void createNewMsg(Integer chatId, String username, String text){
        String sql = "INSERT INTO Message (text, chat_id, user_name, timestamp) VALUES (?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, text);
            pstmt.setInt(2, chatId);
            pstmt.setString(3, username);
            pstmt.setTimestamp(4, timestamp);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to insert message");
            }
        } catch(SQLException e) {
            AppLogger.error(e.getMessage());
        }
    }

    public List<Message> getOldMsgs(Integer chatId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE chat_id = ?";
        List<Map<String, Object>> result = executeQuery(sql);
        for (Map<String, Object> row : result) {
            String username = (String) row.get("user_name");
            String text = (String) row.get("text");
            Message message = new Message(chatId, username, text);
            messages.add(message);
        }
        return messages;
    }
}
