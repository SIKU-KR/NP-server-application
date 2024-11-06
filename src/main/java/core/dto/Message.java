package core.dto;

import java.io.Serializable;

/**
 * Message transfer object for chatting mechanism
 */
public class Message implements Serializable {
    private Integer chatId;
    private Integer userId;
    private String message;

    public Integer getChatId() {
        return chatId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }
}