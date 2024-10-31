package core.controller;

import core.model.RoomModel;
import core.thread.ListRoomConnectionThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ListRoomController manages TCP/IP connection for Clients try to get List of ChatRooms
 * 1. When Clients connect to socket, runs a thread to response with information
 */
public class ListRoomController {

    private final RoomModel roomModel;

    public ListRoomController(RoomModel roomModel) {
        this.roomModel = roomModel;
        run();
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(10001);
            System.out.println("List room protocol listening from port:10001");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ListRoomConnectionThread(socket, roomModel)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
