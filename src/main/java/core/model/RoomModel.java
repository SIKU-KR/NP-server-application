package core.model;

import core.common.AppLogger;
import core.common.DBConnection;
import core.dto.response.ChatRoom;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Access to Chat table in MySQL DBMS.
 */
public class RoomModel {

    public List<ChatRoom> readRoomList() {
        List<ChatRoom> roomList = new ArrayList<>();
        String sql = "SELECT * FROM Chat ORDER BY timestamp";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql);
             ResultSet resultSet = pstmt.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("title");
                String creator = resultSet.getString("creator");
                LocalDateTime time = resultSet.getTimestamp("timestamp").toLocalDateTime();
                roomList.add(new ChatRoom(id, name, creator, time));
            }
        } catch (SQLException e) {
            AppLogger.error("Error reading room list: " + e.getMessage());
        }
        return roomList;
    }

    public ChatRoom createNewRoom(String name, String creator) {
        String sql = "INSERT INTO Chat (title, creator, timestamp) VALUES (?, ?, ?)";
        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentTime);

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, name);
            pstmt.setString(2, creator);
            pstmt.setTimestamp(3, timestamp);

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new RuntimeException("Creating room failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    return new ChatRoom(id, name, creator, currentTime);
                } else {
                    throw new RuntimeException("Creating room failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            AppLogger.error("Error creating room: " + e.getMessage());
            throw new RuntimeException("Database error occurred while creating a new room.");
        }
    }

    public void removeRoomForTest(String roomName) {
        String sql = "DELETE FROM Chat WHERE title = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {

            pstmt.setString(1, roomName);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                AppLogger.warn("No room found with title: " + roomName);
            } else {
                AppLogger.info("Room with title '" + roomName + "' deleted successfully.");
            }
        } catch (SQLException e) {
            AppLogger.error("Error deleting room: " + e.getMessage());
            throw new RuntimeException("Database error occurred while deleting the room.");
        }
    }
}