package core;

import core.controller.ChatController;
import core.controller.ListRoomController;
import core.controller.NewRoomController;
import core.model.RoomModel;

/**
 * ServerSocketController manages Context of Controllers
 */
public class ServerSocketManager {

    private ListRoomController listRoomController;
    private NewRoomController newRoomController;
    private ChatController chatController;

    public void run(){
        new Thread(() -> listRoomController = new ListRoomController(new RoomModel())).start();
        new Thread(() -> newRoomController = new NewRoomController()).start();
        new Thread(() -> chatController = new ChatController()).start();
    }

}
