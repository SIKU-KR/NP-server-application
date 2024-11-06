package core.controller;

import core.common.AppLogger;
import core.common.RequestType;
import core.dto.requestmsg.ChatConnection;
import core.dto.DTO;
import core.model.ChatModel;
import core.model.RoomModel;
import core.runnable.ConnectChatConnectionThread;
import core.runnable.ListRoomConnectionThread;
import core.runnable.NewRoomConnectionThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ServerSocketController manages Context of Controllers
 */
public class ServerSocketController {

    private ServerSocket serverSocket;

    private ThreadGroupController threadGroupController;
    private int port;

    private RoomModel roomModel;
    private ChatModel chatModel;


    public ServerSocketController(int port, ThreadGroupController threadGroupController) {
        this.port = port;
        this.threadGroupController = threadGroupController;

        this.roomModel = new RoomModel();
        this.chatModel = new ChatModel();
    }

    public void run() {
        try {
            this.serverSocket = new ServerSocket(port);
            System.out.println("Server started on port " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                AppLogger.info("Connected from " + socket.getInetAddress().getHostAddress());
                connectionHandler(socket);
            }
        } catch (IOException e) {
            AppLogger.error(e.getMessage());
        }
    }

    private void connectionHandler(Socket socket) throws IOException {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())) {
            DTO dto = (DTO) objectInputStream.readObject();
            RequestType requestType = dto.getRequestType();
            AppLogger.info(socket.getInetAddress().getHostAddress() + " requested " + requestType);

            switch (requestType) {
                case ROOMLIST -> newListRoomThread(socket, dto);
                case NEWROOM -> newRoomThread(socket, dto);
                case CONNECTCHAT -> newChatThread(socket, dto);
                default -> AppLogger.error("Unsupported request type " + requestType);
            }

        } catch (ClassNotFoundException e) {
            AppLogger.error(e.getMessage());
        } finally {
            socket.close();
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
        ThreadGroup chatGroup = threadGroupController.getOrCreateThreadGroup(chatId);
        new Thread(chatGroup, new ConnectChatConnectionThread(socket, chatModel, dto.getRequestMsg())).start();
    }

}
