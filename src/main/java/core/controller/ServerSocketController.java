package core.controller;

import com.google.gson.JsonSyntaxException;
import core.common.AppLogger;
import core.common.RequestType;
import core.dto.requestmsg.ChatConnection;
import core.dto.DTO;
import core.model.ChatModel;
import core.model.RoomModel;
import core.model.UserModel;
import core.runnable.*;
import core.view.InStreamView;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ServerSocketController manages context of ServerSocket
 * 1. Implements singleton pattern to guarantee single instance
 * 2. "PORT" is specified by ServerApplication.java
 * 3. connectionHandler() : maps handler threads(methods) based on RequestType(Enum class)
 */
public class ServerSocketController {

    private static ServerSocketController instance;

    private final int port;
    private ServerSocket serverSocket;

    private final RoomModel roomModel;
    private final ChatModel chatModel;
    private final UserModel userModel;


    private ServerSocketController(int port) {
        this.port = port;
        this.roomModel = new RoomModel();
        this.chatModel = new ChatModel();
        this.userModel = new UserModel();
    }

    public static ServerSocketController getInstance(int port) {
        if (instance == null) {
            instance = new ServerSocketController(port);
        }
        return instance;
    }

    // method for test
    public static void stopServer() {
        try {
            if (instance.serverSocket != null) {
                instance.serverSocket.close();
                instance = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            this.serverSocket = new ServerSocket(port);
            AppLogger.formalInfo(serverSocket, "SERVER-STARTED", "server started");
            while (true) {
                Socket socket = serverSocket.accept();
                AppLogger.formalInfo(socket, "CONNECTED", "client connected to server");
                connectionHandler(socket);
            }
        } catch (IOException e) {
            AppLogger.formalError(serverSocket.getInetAddress().getHostAddress(), serverSocket.getLocalPort(), "SERVER-CLOSED", e.getMessage());
        }
    }

    private void connectionHandler(Socket socket) throws IOException {
        try {
            InStreamView<DTO> in = new InStreamView<>(socket, DTO.class);
            DTO dto = in.readDTO();
            RequestType requestType = dto.getRequestType();
            AppLogger.formalInfo(socket, "REQUEST", "Requested " + requestType);

            switch (requestType) {
                case ROOMLIST -> newListRoomThread(socket, dto);
                case NEWROOM -> newRoomThread(socket, dto);
                case CONNECTCHAT -> newChatThread(socket, dto);
                case LOGIN -> loginThread(socket, dto);
                case USERLIST -> userListThread(socket, dto);
                default -> AppLogger.formalError(socket.getInetAddress().getHostAddress(), socket.getPort(), "UNSUPPORTED-REQUEST", requestType.name());
            }

        } catch (IOException | JsonSyntaxException e) {
            AppLogger.formalError(socket, e);
        }
    }

    private void newListRoomThread(Socket socket, DTO dto) {
        new Thread(new ListRoomConnectionThread(socket, roomModel, dto.getRequestMsg())).start();
    }

    private void newRoomThread(Socket socket, DTO dto) {
        new Thread(new NewRoomConnectionThread(socket, roomModel, dto.getRequestMsg())).start();
    }

    private void newChatThread(Socket socket, DTO dto) {
        Integer chatId = ((ChatConnection) dto.getRequestMsg()).getChatId();
        ConnectChatConnectionThread connectChatConnectionThread = new ConnectChatConnectionThread(socket, chatModel, dto.getRequestMsg());
        ChatThreadsController.getInstance().addThread(chatId, connectChatConnectionThread);
        new Thread(connectChatConnectionThread).start();
    }

    private void loginThread(Socket socket, DTO dto) {
        new Thread(new UserConncectionThread(socket, userModel, dto.getRequestMsg())).start();
    }

    private void userListThread(Socket socket, DTO dto) {
        new Thread(new ListUserConnectionThread(socket, dto.getRequestMsg())).start();
    }
}
