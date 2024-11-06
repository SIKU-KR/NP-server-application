package core.controller;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ThreadGroupController {
    private static ThreadGroupController instance;

    private final Map<Integer, ThreadGroup> threadGroups;

    private ThreadGroupController() {
        this.threadGroups = new ConcurrentHashMap<>();
    }

    public static ThreadGroupController getInstance() {
        if (instance == null) {
            instance = new ThreadGroupController();
        }
        return instance;
    }

    public ThreadGroup getOrCreateThreadGroup(Integer chatId) {
        return threadGroups.computeIfAbsent(chatId, id -> new ThreadGroup(id.toString()));
    }

    public ThreadGroup getThreadGroup(Integer chatId) {
        return threadGroups.get(chatId);
    }
}
