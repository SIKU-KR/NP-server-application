package core.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private ChatRoom chat;
    private User user;
    private String message;
    private LocalDateTime time;
}
