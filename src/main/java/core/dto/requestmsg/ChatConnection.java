package core.dto.requestmsg;

import java.io.Serializable;

public class ChatConnection implements Serializable {
    private final Integer userId;
    private final Integer chatId;

    public ChatConnection(Integer userId, Integer chatId) {
        this.userId = userId;
        this.chatId = chatId;
    }

    public Integer getUserId() {
        return userId;
    }

    public Integer getChatId() {
        return chatId;
    }
}
