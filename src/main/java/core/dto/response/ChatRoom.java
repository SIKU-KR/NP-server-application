package core.dto.response;

import java.time.LocalDateTime;
import java.util.Objects;

public class ChatRoom {
    Integer id;
    String name;
    String creator;
    LocalDateTime time;

    public ChatRoom() {
    }

    public ChatRoom(Integer id, String name, String creator, LocalDateTime time) {
        this.id = id;
        this.name = name;
        this.creator = creator;
        this.time = time;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCreator() {
        return creator;
    }

    public LocalDateTime getTime() {
        return time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return Objects.equals(id, chatRoom.id) &&
                Objects.equals(name, chatRoom.name) &&
                Objects.equals(creator, chatRoom.creator) &&
                Objects.equals(time, chatRoom.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, creator, time);
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", creator='" + creator + '\'' +
                ", time=" + time +
                '}';
    }
}
