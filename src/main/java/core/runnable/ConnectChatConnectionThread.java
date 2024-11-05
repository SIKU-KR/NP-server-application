package core.runnable;

import core.model.ChatModel;

import java.net.Socket;

public class ConnectChatConnectionThread implements Runnable {

    private Socket socket;
    private ChatModel chatModel;

    public ConnectChatConnectionThread(Socket socket, ChatModel chatModel) {
        this.socket = socket;
        this.chatModel = chatModel;
    }

    @Override
    public void run() {

    }
}
