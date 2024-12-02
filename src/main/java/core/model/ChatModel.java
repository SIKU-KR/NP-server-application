package core.model;

import core.common.AppLogger;
import core.common.DBConnection;
import core.dto.Message;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Access to Message table in MySQL DBMS.
 */
public class ChatModel {

    public void createNewMsg(Integer chatId, String username, String text) {
        String sql = "INSERT INTO Message (text, chat_id, user_name, timestamp) VALUES (?, ?, ?, ?)";
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, text);
            pstmt.setInt(2, chatId);
            pstmt.setString(3, username);
            pstmt.setTimestamp(4, timestamp);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Failed to insert message.");
            }
        } catch (SQLException e) {
            AppLogger.error("Error inserting message: " + e.getMessage());
            throw new RuntimeException("Database error occurred while inserting message.");
        }
    }

    public List<Message> getOldMsgs(Integer chatId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE chat_id = ? ORDER BY timestamp";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setInt(1, chatId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    String username = resultSet.getString("user_name");
                    String text = resultSet.getString("text");
                    LocalDateTime time = resultSet.getTimestamp("timestamp").toLocalDateTime();
                    messages.add(new Message(chatId, username, text));
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Error fetching old messages: " + e.getMessage());
        }
        return messages;
    }

    public void removeMsgsForTest(Integer chatId) {
        String sql = "DELETE FROM Message WHERE chat_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, chatId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            AppLogger.error("Error fetching old messages: " + e.getMessage());
        }
    }
}