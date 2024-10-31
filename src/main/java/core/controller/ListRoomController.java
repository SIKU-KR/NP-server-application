package core.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ListRoomController manages TCP/IP connection for Clients try to get List of ChatRooms
 * 1. When Clients connect to socket, runs a thread to response with information.
 * 2. Thread reads information from database using ChatModel Instance
 * 3. Disconnects when client receives created response
 */
public class ListRoomController {

    private ServerSocket serverSocket;

    public ListRoomController() {
        run();
    }

    public void run(){
        try{
            serverSocket = new ServerSocket(10001);
            System.out.println("List room protocol listening from port:10001");
            while(true){
                Socket socket = serverSocket.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
