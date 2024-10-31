package core.model;

import core.dto.ChatRoom;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * RoomModel inherits DBConnection(Abstract Class)
 * 1. readRoomList method excutes a query to read all chat rooms sorted by timestamp
 */
public class RoomModel extends DBConnection{

    public List<ChatRoom> readRoomList() {
        List<ChatRoom> roomList = new ArrayList<>();
        String sql = "SELECT * FROM `Chat Order By timestamp";
        List<Map<String, Object>> result = executeQuery(sql);
        for (Map<String, Object> row : result) {
            String name = (String) row.get("title");
            Timestamp timestamp = (Timestamp) row.get("timestamp");
            LocalDateTime time = timestamp.toLocalDateTime();
            roomList.add(new ChatRoom(name, time));
        }
        return roomList;
    }

}
