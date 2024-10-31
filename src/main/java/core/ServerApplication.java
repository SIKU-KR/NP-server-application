package core;

/**
 * Start point of Server Application
 * Create ServerSocketController with ChatModel, RoomModel, UserModel (Dependency Injection)
 * Run ServerSocketController Instance to open server sockets
 */
public class ServerApplication {

    public static void main(String[] args) {
        ServerSocketManager serverSocketManager = new ServerSocketManager();
        serverSocketManager.run();
    }

}