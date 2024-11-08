package core.dto;

import java.io.Serializable;

/**
 * Message transfer object for chatting mechanism
 */
public class Message {
    private Integer chatId;
    private Integer userId;
    private String message;

    public Message() {
    }

    public Message(Integer chatId, Integer userId, String message) {
        this.chatId = chatId;
        this.userId = userId;
        this.message = message;
    }

    public Integer getChatId() {
        return chatId;
    }

    public Integer getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "chatId=" + chatId +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                '}';
    }
}