package core.runnable;

import core.common.AppLogger;
import core.dto.ChatRoom;
import core.dto.NewRoom;
import core.model.RoomModel;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class NewRoomConnectionThread implements Runnable {

    private final Socket socket;
    private final RoomModel roomModel;
    private final NewRoom requestMsg;

    public NewRoomConnectionThread(Socket socket, RoomModel roomModel, Object requestMsg) {
        this.socket = socket;
        this.roomModel = roomModel;
        this.requestMsg = (NewRoom) requestMsg;
    }

    @Override
    public void run() {
        try {
            ChatRoom createdRoom = getCreatedRoom();
            sendResponse(createdRoom);
        } catch (RuntimeException e) {
            AppLogger.error(e.getMessage());
        } catch (IOException e) {
            AppLogger.error(e.getMessage());
        } finally {
            closeSocket();
        }
    }

    private ChatRoom getCreatedRoom() {
        return roomModel.createNewRoom(requestMsg.getTitle(), requestMsg.getCreator());
    }

    private void sendResponse(ChatRoom response) throws IOException {
        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())) {
            objectOutputStream.writeObject(response);
            objectOutputStream.flush();
        }
    }

    private void closeSocket() {
        try {
            if (!this.socket.isClosed()) {
                this.socket.close();
            }
        } catch (IOException e) {
            AppLogger.error(e.getMessage());
        }
    }
}
