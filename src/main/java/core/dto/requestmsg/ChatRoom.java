package core.dto.requestmsg;

import java.io.Serializable;
import java.time.LocalDateTime;

public class ChatRoom implements Serializable {
    private Integer id;
    private String name;
    private String creator;
    private LocalDateTime time;

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
}
