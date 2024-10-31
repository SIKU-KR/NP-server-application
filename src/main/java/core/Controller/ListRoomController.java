package core.Controller;

import java.net.ServerSocket;

/**
 * ListRoomController manages TCP/IP connection for Clients try to get List of ChatRooms
 * 1. When Clients connect to socket, runs a thread to response with information.
 * 2. Thread reads information from database using ChatModel Instance
 * 3. Disconnects when client receives created response
 */
public class ListRoomController {

    private ServerSocket serverSocket;

    public void run(){

    }
}
