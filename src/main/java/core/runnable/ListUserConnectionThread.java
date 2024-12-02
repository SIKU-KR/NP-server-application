package core.runnable;

import core.common.AppLogger;
import core.controller.ChatThreadsController;
import core.dto.response.ListUser;
import core.view.OutStreamView;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ListUserConnectionThread implements Runnable {
    private final Socket socket;
    private final Integer reqMsg;
    private final ChatThreadsController chatThreadsController;

    public ListUserConnectionThread(Socket socket, Object requestMsg) {
        this.socket = socket;
        reqMsg = (Integer) requestMsg;
        this.chatThreadsController = ChatThreadsController.getInstance();
    }

    @Override
    public void run() {
        try {
            sendUserlist(chatThreadsController.getChatConnections(reqMsg));
        } catch (RuntimeException | IOException e) {
            AppLogger.formalError(socket, e);
        } finally {
            closeSocket();
        }
    }

    private void sendUserlist(List<String> chatConnections) throws IOException {
        ListUser users = new ListUser(chatConnections);
        OutStreamView<ListUser> out = new OutStreamView<>(socket);
        out.send(users);
        AppLogger.formalInfo(socket, "SENT", "User list sent to client successfully");
    }

    private void closeSocket() {
        try {
            if (!this.socket.isClosed()) {
                this.socket.close();
            }
        } catch (IOException e) {
            AppLogger.formalError(socket, e);
        }
    }
}
