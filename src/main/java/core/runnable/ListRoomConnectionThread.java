package core.runnable;

import core.common.AppLogger;
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
    private final Object requestMsg;

    public ListRoomConnectionThread(Socket socket, RoomModel roomModel, Object requestMsg) {
        this.socket = socket;
        this.roomModel = roomModel;
        this.requestMsg = requestMsg;
    }

    @Override
    public void run() {
        try {
            sendRoomList(getRoomList());
        } catch (IOException e) {
            AppLogger.error(e.getMessage());
        } finally {
            closeSocket();
        }
    }

    private List<ChatRoom> getRoomList() {
        return roomModel.readRoomList();
    }

    private void sendRoomList(List<ChatRoom> response) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        }
    }

    private void closeSocket() {
        try {
            if(!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            AppLogger.error(e.getMessage());
        }
    }
}
