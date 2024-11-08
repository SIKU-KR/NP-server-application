package core.runnable;

import core.common.AppLogger;
import core.dto.response.ChatRoom;
import core.dto.response.ListChatRoom;
import core.model.RoomModel;
import core.view.OutStreamView;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Handler thread for connection to get a list of chat rooms.
 */
public class ListRoomConnectionThread implements Runnable {

    private final Socket socket;
    private final RoomModel roomModel;

    public ListRoomConnectionThread(Socket socket, RoomModel roomModel, Object requestMsg) {
        this.socket = socket;
        this.roomModel = roomModel;
    }

    @Override
    public void run() {

        try {
            sendRoomList(getRoomList());
        } catch (IOException e) {
            AppLogger.formalError(socket, e);
        } finally {
            closeSocket();
        }
    }

    private ListChatRoom getRoomList() {
        List<ChatRoom> response = roomModel.readRoomList();
        return new ListChatRoom(response);
    }

    private void sendRoomList(ListChatRoom response) throws IOException {
        try {
            OutStreamView<ListChatRoom> out = new OutStreamView<>(socket);
            out.send(response);
            AppLogger.formalInfo(socket, "RESPONSE", "Room list sent successfully");
        } catch (IOException e) {
            AppLogger.formalError(socket, e);
        }
    }

    private void closeSocket() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
        } catch (IOException e) {
            AppLogger.formalError(socket, e);
        }
    }
}
