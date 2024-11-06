package core.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadGroupController {
    private Map<Integer, ThreadGroup> threadGroups;

    public ThreadGroupController() {
        this.threadGroups = new ConcurrentHashMap<>();
    }

    public ThreadGroup getOrCreateThreadGroup(Integer chatId) {
        return threadGroups.computeIfAbsent(chatId, id -> new ThreadGroup(id.toString()));
    }

    public ThreadGroup getThreadGroup(Integer chatId) {
        return threadGroups.get(chatId);
    }
}
