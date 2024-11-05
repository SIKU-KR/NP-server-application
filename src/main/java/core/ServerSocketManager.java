package core;

import core.common.AppLogger;
import core.common.RequestType;
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
public class ServerSocketManager {

    private ServerSocket serverSocket;
    private int port;

    private RoomModel roomModel;
    private ChatModel chatModel;

    public ServerSocketManager(int port) {
        this.roomModel = new RoomModel();
        this.chatModel = new ChatModel();
        this.port = port;
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
                case ROOMLIST ->
                        new Thread(new ListRoomConnectionThread(socket, roomModel, dto.getRequestMsg())).start();
                case NEWROOM -> new Thread(new NewRoomConnectionThread(socket, roomModel, dto.getRequestMsg())).start();
                case CONNECTCHAT -> new Thread(new ConnectChatConnectionThread(socket, chatModel)).start();
                default ->
                        AppLogger.error("Unsupported request type " + requestType + "From " + socket.getInetAddress().getHostAddress());
            }

        } catch (ClassNotFoundException e) {
            AppLogger.error(e.getMessage());
        } finally {
            socket.close();
        }
    }
}
