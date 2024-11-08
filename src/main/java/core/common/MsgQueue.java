package core.common;

import core.dto.Message;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Class : MsgQueue
 * A Queue data structure which works asynchronously.
 * Supports enque and deque.
 * 1. Implemented based on Producer-Consumer Pattern.
 * 1-1. ConnectChatConnectionThread.java (Producer) - MsgQueueConsumer.java (Consumer)
 * 2. Also implemented based on Singleton Pattern.
 * 3. BlockingQueue : Specific Queue Collection(java.util.concurrent) for multi-threading.
 */
public class MsgQueue {
    private static volatile MsgQueue instance;

    private final BlockingQueue<Message> queue;

    private MsgQueue() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public static MsgQueue getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (MsgQueue.class) {
            if (instance == null) {
                instance = new MsgQueue();
            }
        }
        return instance;
    }

    public void enqueue(Message message) {
        queue.add(message);
        AppLogger.info("Task appended to queue: " + message.toString());
    }

    public Message dequeue() throws InterruptedException {
        return queue.take();
    }

    public void clear() {
        instance = null;
    }
}