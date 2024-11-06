package core.dto;

import java.io.Serializable;

public class Message implements Serializable {
    private Integer chatId;
    private Integer userId;
    private String message;

    public String getMessage() {
        return message;
    }
}