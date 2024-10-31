package core.Controller;

import core.Model.ChatModel;
import core.Model.RoomModel;
import core.Model.UserModel;

/**
 * ServerSocketController manages Context of Controllers
 * This can be removed from the project if it is not needed
 */
public class ServerSocketController {

    private ChatModel chatModel;
    private RoomModel serverSocket;
    private UserModel userModel;

    private ListRoomController listRoomController;
    private NewRoomController newRoomController;
    private ChatController chatController;

    public ServerSocketController(ChatModel chatModel, RoomModel serverSocket, UserModel userModel) {
        this.chatModel = chatModel;
        this.serverSocket = serverSocket;
        this.userModel = userModel;
    }

    public void run(){
        listRoomController = new ListRoomController();
        listRoomController.run();

        newRoomController = new NewRoomController();
        newRoomController.run();

        chatController = new ChatController();
        chatController.run();
    }

}
