package core.Controller;

import java.net.ServerSocket;

/**
 * ListRoomController manages TCP/IP connection for Clients try to get List of ChatRooms
 * 1. When Clients connect to socket, runs a thread to create new chatroom.
 * 2. Thread writes information from database using ChatModel instance
 * 3. Redirect to ChatController for client to enter new chat room
 * 4. Disconnects connection after finishing task
 */
public class NewRoomController {

    private ServerSocket serverSocket;


    public void run(){

    }
}
