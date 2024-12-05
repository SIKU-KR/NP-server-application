package core.runnable;

import core.common.AppLogger;
import core.model.InMemoryChatCounts;
import core.model.InMemoryLastSenders;
import core.controller.MsgQueueController;
import core.controller.ChatThreadsController;
import core.dto.Message;

import java.util.List;

/**
 * Consumer thread of MsgQueue
 * 1. wait(blocked) until queue gets a new msg.
 * 2. msgHandler() : sends to chat room members based on ChatThreadsController(Map)
 */
public class MsgQueueConsumer implements Runnable {
    private final MsgQueueController msgQueueController;
    private final InMemoryChatCounts inMemoryChatCounts;
    private final InMemoryLastSenders inMemoryLastSenders;
    private final ChatThreadsController chatThreadsController;
    private boolean isRunning = true;

    public MsgQueueConsumer() {
        this.msgQueueController = MsgQueueController.getInstance();
        this.inMemoryChatCounts = InMemoryChatCounts.getInstance();
        this.inMemoryLastSenders = InMemoryLastSenders.getInstance();
        this.chatThreadsController = ChatThreadsController.getInstance();
        AppLogger.info("MsgQueueConsumer started");
    }

    @Override
    public void run() {
        while (isRunning) {
            try {
                Message msg = this.msgQueueController.dequeue();
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
        String message = msg.getMessage();
        if (message.startsWith("/")) {
            if (message.startsWith("/문제")) {
                registerNewProb(msg);
            }
            if (message.startsWith("/네") || message.startsWith("/아니오")) {
                sendAnswerStatus(msg);
            }
            if (message.startsWith("/정답")) {
                sendThatCorrect(msg);
            }
        } else {
            processGeneralMsg(msg);
        }
    }

    private void sendToThreads(Message msg) {
        String sendedBy = msg.getUsername();
        int chatId = msg.getChatId();
        List<ConnectChatConnectionThread> threads = this.chatThreadsController.getThreads(chatId);
        for (ConnectChatConnectionThread thread : threads) {
            if (!thread.getUsername().equals(sendedBy)) {
                thread.sendMessage(msg);
                AppLogger.formalInfo(thread.getSocket(), "MESSAGE SENT", "Message sent to user " + thread.getUsername() + " in chat " + chatId);
            }
        }
    }

    private void processGeneralMsg(Message msg) {
        Integer chatId = msg.getChatId();
        if (inMemoryChatCounts.getValue(chatId) > 20) {
            sendToThreads(new Message(chatId, "admin", "이미 20번의 질문기회를 사용하셨습니다."));
        }
        inMemoryChatCounts.addValue(chatId);
        inMemoryLastSenders.storeValue(chatId, msg.getUsername());
        sendToThreads(msg);
    }

    private void registerNewProb(Message msg) {
        Integer chatId = msg.getChatId();
        String sendedBy = msg.getUsername();
        inMemoryChatCounts.clearKey(chatId);
        inMemoryLastSenders.clearkey(chatId);
        sendToThreads(new Message(chatId, "admin", sendedBy + "님이 게임을 새로 시작했습니다."));
    }

    private void sendAnswerStatus(Message msg) {
        Integer chatId = msg.getChatId();
        String content = inMemoryChatCounts.getValue(chatId) + "번에 대한 답변: " + msg.getMessage().substring(1);
        sendToThreads(new Message(msg.getChatId(), msg.getUsername(), content));
    }

    private void sendThatCorrect(Message msg) {
        String content = inMemoryLastSenders.getValue(msg.getChatId()) + "님이 정답을 맞췄습니다. '/문제'를 입력하여 새로운 게임을 시작하세요.";
        sendToThreads(new Message(msg.getChatId(), "admin", content));
    }
}
