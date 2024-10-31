package core.thread;

import core.dto.ChatRoom;
import core.model.RoomModel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * ListRoomConnectionThread implements Runnable, which means Thread.
 * This class' instance is created and run by ListRoomController
 * 1. Thread reads information from database using RoomModel Instance
 * 2. Disconnects when client receives created response
 */
public class ListRoomConnectionThread implements Runnable {

    private final Socket socket;
    private final RoomModel roomModel;

    public ListRoomConnectionThread(Socket socket, RoomModel roomModel) {
        this.socket = socket;
        this.roomModel = roomModel;
    }

    @Override
    public void run() {
        try {
            sendRoomList();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeSocket();
        }
    }

    private void sendRoomList() throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            List<ChatRoom> response = roomModel.readRoomList();
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        }
    }

    private void closeSocket() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
