package core.runnable;

import core.common.AppLogger;
import core.dto.requestmsg.ChatRoom;
import core.model.RoomModel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

/**
 * Handler thread for connection to get a list of chat rooms.
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
            AppLogger.error("When sending room list: " + e.getMessage());
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
            AppLogger.error("When closing socket: " + e.getMessage());
        }
    }
}
