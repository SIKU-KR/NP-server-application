package core.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ChatController manages context of chat socket
 * 1. Create threads when client connects to server Socket
 * 2. Manage Thread Groups for group chatting
 * 3. Read and Write to Database using ChatModel
 */
public class ChatController {

    private ServerSocket serverSocket;

    public ChatController() {
        run();
    }

    public void run(){
        try{
            serverSocket = new ServerSocket(10003);
            System.out.println("Chat protocol listening from port:10003");
            while(true){
                Socket socket = serverSocket.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
