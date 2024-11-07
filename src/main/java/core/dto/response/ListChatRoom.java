package core.dto.response;

import java.util.List;

public class ListChatRoom {
    List<ChatRoom> chatRooms;

    public ListChatRoom() {
    }

    public ListChatRoom(List<ChatRoom> chatRooms) {
        this.chatRooms = chatRooms;
    }

    public List<ChatRoom> getChatRooms() {
        return chatRooms;
    }

    @Override
    public String toString() {
        return "ListChatRoom{" +
                "chatRooms=" + chatRooms +
                '}';
    }
}
