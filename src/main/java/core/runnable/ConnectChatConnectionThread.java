package core.runnable;

import core.common.AppLogger;
import core.common.MsgQueue;
import core.controller.ChatThreadsController;
import core.dto.requestmsg.ChatConnection;
import core.dto.Message;
import core.model.ChatModel;

import java.io.*;
import java.net.Socket;

/**
 * Thread of chat sessions
 * 1. read msg object from input stream (client -> server)
 * 2. write msg object using output stream (server -> client)
 */
public class ConnectChatConnectionThread implements Runnable {

    private final Socket socket;
    private final ChatModel chatModel;
    private final Integer chatId;
    private final Integer userId;

    private InputStream inputStream;
    private OutputStream outputStream;
    private volatile boolean running = true;

    public ConnectChatConnectionThread(Socket socket, ChatModel chatModel, Object requestMsg) {
        this.socket = socket;
        this.chatModel = chatModel;
        ChatConnection chatConnection = (ChatConnection) requestMsg;
        this.chatId = chatConnection.getChatId();
        this.userId = chatConnection.getUserId();
        initializeStreams();
    }

    private void initializeStreams() {
        try {
            this.inputStream = this.socket.getInputStream();
            this.outputStream = this.socket.getOutputStream();
        } catch (IOException e) {
            AppLogger.error("When initializing streams: " + e.getMessage());
        }
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(inputStream)) {
            while (running) {
                readMessage(objectInputStream);
            }
        } catch (IOException e) {
            AppLogger.error("When creating ObjectInputStream: " + e.getMessage());
        }
    }

    public Integer getUserId() {
        return userId;
    }

    private void readMessage(ObjectInputStream objectInputStream) {
        try {
            Message receivedMessage = (Message) objectInputStream.readObject();
            processMessage(receivedMessage);
        } catch (ClassNotFoundException | IOException e) {
            AppLogger.error("When reading message: " + e.getMessage());
            stop();
        }
    }

    private void processMessage(Message message) {
        if (message != null) {
            chatModel.createNewMsg(chatId, userId, message.getMessage());
            MsgQueue.getInstance().enqueue(message);
        }
    }

    public synchronized void sendMessage(Message message) {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream)) {
            objectOutputStream.writeObject(message);
            objectOutputStream.flush();
        } catch (IOException e) {
            AppLogger.error("When sending message: " + e.getMessage());
        }
    }

    public void stop() {
        running = false;
        ChatThreadsController.getInstance().removeThread(chatId, this);
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            AppLogger.error("When closing socket: " + e.getMessage());
        }
    }
}


