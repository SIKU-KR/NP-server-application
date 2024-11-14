package core.runnable;

import core.common.AppLogger;
import core.dto.requestmsg.UserLogin;
import core.dto.response.User;
import core.model.UserModel;
import core.view.OutStreamView;

import java.io.IOException;
import java.net.Socket;
import java.util.Optional;

public class UserConncectionThread implements Runnable {

    private final Socket socket;
    private final UserModel userModel;
    private final UserLogin requestMsg;

    public UserConncectionThread(Socket socket, UserModel userModel, Object requestMsg) {
        this.socket = socket;
        this.userModel = userModel;
        this.requestMsg = (UserLogin) requestMsg;
    }

    @Override
    public void run() {
        User user = null;
        try {
            Optional<User> userOptional = userModel.checkUserExists(requestMsg.getUsername());
            if (userOptional.isEmpty()) {
                String username = requestMsg.getUsername();
                userModel.createUser(username);
                userOptional = Optional.of(new User(username, 0));
            }
            user = userOptional.get();
            sendResponse(user);
        } catch (IOException e) {
            AppLogger.formalError(socket, e);
        } finally {
            closeSocket();
        }
    }

    private void sendResponse(User user) throws IOException {
        OutStreamView<User> out = new OutStreamView<>(socket);
        out.send(user);
        AppLogger.formalInfo(socket, "RESPONSE", "Login Successful: " + user.getUsername());
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