package core;

import core.Controller.ChatController;
import core.Controller.ListRoomController;
import core.Controller.NewRoomController;
import core.Model.ChatModel;
import core.Model.RoomModel;
import core.Model.UserModel;

/**
 * ServerSocketController manages Context of Controllers
 * This can be removed from the project if it is not needed
 */
public class ServerSocketManager {


    private ListRoomController listRoomController;
    private NewRoomController newRoomController;
    private ChatController chatController;

    public void run(){
        listRoomController = new ListRoomController();
        listRoomController.run();

        newRoomController = new NewRoomController();
        newRoomController.run();

        chatController = new ChatController();
        chatController.run();
    }

}
