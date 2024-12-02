package core.controller;

import core.common.AppLogger;
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
public class MsgQueueController {
    private static volatile MsgQueueController instance;

    private final BlockingQueue<Message> queue;

    private MsgQueueController() {
        this.queue = new LinkedBlockingQueue<>();
    }

    public static MsgQueueController getInstance() {
        if (instance != null) {
            return instance;
        }

        synchronized (MsgQueueController.class) {
            if (instance == null) {
                instance = new MsgQueueController();
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