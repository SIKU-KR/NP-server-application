package core.model;

import core.common.AppLogger;

import java.util.concurrent.ConcurrentHashMap;

public class InMemoryLastSenders {
    private static InMemoryLastSenders instance;

    private ConcurrentHashMap<Integer, String> lastSenderMap;

    private InMemoryLastSenders() {
        lastSenderMap = new ConcurrentHashMap<>();
    }

    public static InMemoryLastSenders getInstance() {
        if(instance == null){
            instance = new InMemoryLastSenders();
            AppLogger.info("LastSenderMap created");
        }
        return instance;
    }

    public void storeValue(Integer id, String value) {
        this.lastSenderMap.put(id, value);
    }

    public String getValue(Integer id) {
        return this.lastSenderMap.get(id);
    }

    public void clearkey(Integer id) {
        this.lastSenderMap.remove(id);
    }
}
