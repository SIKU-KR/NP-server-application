package core.controller;

import core.runnable.ConnectChatConnectionThread;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Manages chatting threads by Map Collection
 * 1. Key-Value : {ChatId}:{List of Threads}
 * 2. addThreads() : add Thread to Map, so program have control on it
 * 3. getThreads() : get Threads connect of given chatId
 * 4. removeThread() : removes specific thread of Map.
 */
public class ChatThreadsController {
    private static ChatThreadsController instance;

    private final Map<Integer, List<ConnectChatConnectionThread>> chatConnections;

    private ChatThreadsController() {
        this.chatConnections = new ConcurrentHashMap<>();
    }

    public static ChatThreadsController getInstance() {
        if (instance == null) {
            instance = new ChatThreadsController();
        }
        return instance;
    }

    public void addThread(Integer chatId, ConnectChatConnectionThread connectionThread) {
        chatConnections.computeIfAbsent(chatId, k -> new CopyOnWriteArrayList<>()).add(connectionThread);
    }

    public List<ConnectChatConnectionThread> getThreads(Integer chatId) {
        return this.chatConnections.getOrDefault(chatId, List.of());
    }

    public void removeThread(Integer chatId, ConnectChatConnectionThread connectionThread) {
        List<ConnectChatConnectionThread> connections = chatConnections.get(chatId);
        if (connections != null) {
            connections.remove(connectionThread);
        }
    }

    public List<String> getChatConnections(int id) {
        if (id == 0) {
            // if id == 0, get all usernames of all thread
            return this.chatConnections.values().stream()
                    .flatMap(List::stream).map(ConnectChatConnectionThread::getUsername)
                    .toList();
        }
        if (this.chatConnections.containsKey(id)) {
            return this.chatConnections.get(id).stream()
                    .map(ConnectChatConnectionThread::getUsername).toList();
        }
        return List.of();
    }
}
