package core.model;

import core.common.AppLogger;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryChatCounts {
    private static InMemoryChatCounts instance;

    private ConcurrentHashMap<Integer, Integer> chatCounts;

    private InMemoryChatCounts() {
        this.chatCounts = new ConcurrentHashMap<>();
    }

    public static InMemoryChatCounts getInstance() {
        if (instance == null) {
            instance = new InMemoryChatCounts();
            AppLogger.info("Chat Counter created");
        }
        return instance;
    }

    public void addValue(Integer id) {
        if (chatCounts.containsKey(id)) {
            chatCounts.put(id, chatCounts.get(id) + 1);
        } else {
            chatCounts.put(id, 1);
        }
    }

    public int getValue(Integer id) {
        if (chatCounts.containsKey(id)) {
            return chatCounts.get(id);
        }
        return 0;
    }

    public void clearKey(Integer id) {
        this.chatCounts.remove(id);
    }
}
