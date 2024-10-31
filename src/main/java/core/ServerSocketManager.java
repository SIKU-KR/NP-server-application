package core;

import core.controller.ChatController;
import core.controller.ListRoomController;
import core.controller.NewRoomController;

/**
 * ServerSocketController manages Context of Controllers
 * This can be removed from the project if it is not needed
 */
public class ServerSocketManager {

    private ListRoomController listRoomController;
    private NewRoomController newRoomController;
    private ChatController chatController;

    public void run(){
        new Thread(() -> listRoomController = new ListRoomController()).start();
        new Thread(() -> newRoomController = new NewRoomController()).start();
        new Thread(() -> chatController = new ChatController()).start();
    }

}
