package core.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ChatRoom implements Serializable {
    private String name;
    private LocalDateTime time;

    public ChatRoom(String name, LocalDateTime time) {
        this.name = name;
        this.time = time;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }
}
