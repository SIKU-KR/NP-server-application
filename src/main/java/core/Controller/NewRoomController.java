package core.Controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ListRoomController manages TCP/IP connection for Clients try to get List of ChatRooms
 * 1. When Clients connect to socket, runs a thread to create new chatroom.
 * 2. Thread writes information from database using RoomModel instance
 * 3. Redirect to ChatController for client to enter new chat room
 * 4. Disconnects connection after finishing task
 */
public class NewRoomController {

    private ServerSocket serverSocket;

    public NewRoomController() {
        run();
    }

    public void run(){
        try{
            serverSocket = new ServerSocket(10002);
            System.out.println("New room protocol listening from port:10002");
            while(true){
                Socket socket = serverSocket.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
