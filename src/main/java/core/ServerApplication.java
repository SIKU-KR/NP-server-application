package core;

import core.controller.ServerSocketController;
import core.controller.ThreadGroupController;

/**
 * Start point of Server Application
 * Create ServerSocketController with ChatModel, RoomModel, UserModel (Dependency Injection)
 * Run ServerSocketController Instance to open server sockets
 */
public class ServerApplication {

    private static int PORT = 10001;

    public static void main(String[] args) {
        ServerSocketController.getInstance(PORT).run();
    }

}