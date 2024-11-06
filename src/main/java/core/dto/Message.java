package core.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

public class Message implements Serializable {
    private Integer chatId;
    private Integer userId;
    private String message;

    public String getMessage() {
        return message;
    }
}