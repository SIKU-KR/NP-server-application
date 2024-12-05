package core.runnable;

import core.common.AppLogger;
import core.controller.MsgQueueController;
import core.controller.ChatThreadsController;
import core.dto.requestmsg.ChatConnection;
import core.dto.Message;
import core.model.ChatModel;
import core.view.InStreamView;
import core.view.OutStreamView;

import java.io.*;
import java.net.Socket;
import java.util.List;

/**
 * Thread of chat sessions
 * 1. read msg object from input stream (client -> server)
 * 2. write msg object using output stream (server -> client)
 */
public class ConnectChatConnectionThread implements Runnable {

    private final Socket socket;
    private final ChatModel chatModel;
    private final Integer chatId;
    private final String username;

    private InStreamView<Message> in;
    private OutStreamView<Message> out;
    private volatile boolean running = true;

    public ConnectChatConnectionThread(Socket socket, ChatModel chatModel, Object requestMsg) {
        this.socket = socket;
        this.chatModel = chatModel;
        ChatConnection chatConnection = (ChatConnection) requestMsg;
        this.chatId = chatConnection.getChatId();
        this.username = chatConnection.getUsername();
        initializeStreams();
    }

    private void initializeStreams() {
        try {
            this.in = new InStreamView<>(socket, Message.class);
            this.out = new OutStreamView<>(socket);
        } catch (Exception e) {
            AppLogger.formalError(socket, e);
        }
    }

    @Override
    public void run() {
        AppLogger.formalInfo(socket, "STARTED", "started chatThread on chatId:" + chatId + ", user:" + username);
        try {
            List<Message> msgs = this.chatModel.getOldMsgs(chatId);
            for (Message msg : msgs) {
                sendMessage(msg);
            }
            while (running) {
                Message msg = in.read();
                processMessage(msg);
                if(msg == null){
                    stop();
                    break;
                }
            }
        } catch (Exception e) {
            AppLogger.formalError(socket, e);
            stop();
        }
    }

    public String getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }

    private void processMessage(Message message) {
        if (message != null) {
            AppLogger.formalInfo(socket, "RECEIVED", "message: '" + message.getMessage() + "'");
            chatModel.createNewMsg(chatId, username, message.getMessage());
            MsgQueueController.getInstance().enqueue(message);
        }
    }

    public synchronized void sendMessage(Message message) {
        AppLogger.formalInfo(socket, "SENT", "message: '" + message.getMessage() + "'");
        out.send(message);
    }

    public void stop() {
        running = false;
        ChatThreadsController.getInstance().removeThread(chatId, this);
        AppLogger.formalInfo(socket, "STOPPED", "socket connection stopped");
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            AppLogger.error("When closing socket: " + e.getMessage());
        }
    }
}


