package core.runnable;

import core.common.AppLogger;
import core.common.MsgQueue;
import core.controller.ChatThreadsController;
import core.dto.Message;

import java.util.List;

/**
 * Consumer thread of MsgQueue
 * 1. wait(blocked) until queue gets a new msg.
 * 2. msgHandler() : sends to chat room members based on ChatThreadsController(Map)
 */
public class MsgQueueConsumer implements Runnable {

    private MsgQueue msgQueue;
    private ChatThreadsController chatThreadsController;
    private boolean isRunning = true;

    public MsgQueueConsumer() {
        this.msgQueue = MsgQueue.getInstance();
        this.chatThreadsController = ChatThreadsController.getInstance();
        AppLogger.info("MsgQueueConsumer started");
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Message msg = this.msgQueue.dequeue();
                AppLogger.info("Task taken from queue :" + msg.toString());
                msgHandler(msg);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                AppLogger.error("Consumer Thread Interrupted");
                break;
            }
        }
    }

    private void msgHandler(Message msg) {
        Integer chatId = msg.getChatId();
        Integer sendedBy = msg.getUserId();
        List<ConnectChatConnectionThread> threads = this.chatThreadsController.getThreads(chatId);

        for (ConnectChatConnectionThread thread : threads) {
            if (!thread.getUserId().equals(sendedBy)) {
                thread.sendMessage(msg);
                AppLogger.formalInfo(thread.getSocket(), "MESSAGE SENT", "Message sent to user " + thread.getUserId() + " in chat " + chatId);
            }
        }
    }

}
