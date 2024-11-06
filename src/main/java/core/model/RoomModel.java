package core.model;

import core.common.AppLogger;
import core.dto.requestmsg.ChatRoom;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Access to Chat table in MySQL dbms.
 */
public class RoomModel extends DBConnection{

    public List<ChatRoom> readRoomList() {
        List<ChatRoom> roomList = new ArrayList<>();
        String sql = "SELECT * FROM `Chat Order By timestamp";
        List<Map<String, Object>> result = executeQuery(sql);
        for (Map<String, Object> row : result) {
            Integer id = (Integer) row.get("id");
            String name = (String) row.get("title");
            String creator = (String) row.get("creator");
            Timestamp timestamp = (Timestamp) row.get("timestamp");
            LocalDateTime time = timestamp.toLocalDateTime();
            roomList.add(new ChatRoom(id, name, creator, time));
        }
        return roomList;
    }

    public ChatRoom createNewRoom(String name, String creator) {
        String sql = "INSERT INTO Chat (title, creator, timestamp) VALUES (?, ?, ?)";
        LocalDateTime currentTime = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(currentTime);

        try (PreparedStatement pstmt = getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
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
            AppLogger.error(e.getMessage());
            throw new RuntimeException("Database error occurred while creating a new room.");
        }
    }
}
