package core;

import core.Controller.ServerSocketController;
import core.Model.ChatModel;
import core.Model.RoomModel;
import core.Model.UserModel;


/**
 * Start point of Server Application
 * Create ServerSocketController with ChatModel, RoomModel, UserModel (Dependency Injection)
 * Run ServerSocketController Instance to open server sockets
 */
public class ServerApplication {

    public static void main(String[] args) {
        ServerSocketController serverSocketController = new ServerSocketController(
                new ChatModel(), new RoomModel(), new UserModel()
        );
        serverSocketController.run();
    }

}