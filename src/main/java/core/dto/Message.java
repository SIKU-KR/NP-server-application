package core.dto;

import java.io.Serializable;

/**
 * Message transfer object for chatting mechanism
 */
public class Message {
    private Integer chatId;
    private String username;
    private String message;

    public Message() {
    }

    public Message(Integer chatId, String username, String message) {
        this.chatId = chatId;
        this.username = username;
        this.message = message;
    }

    public Integer getChatId() {
        return chatId;
    }

    public String getUsername() {
        return username;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "chatId=" + chatId +
                ", username='" + username + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}