package core.controller;

import core.model.RoomModel;
import core.thread.ListRoomConnectionThread;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 대화방 목록을 가져오려는 클라이언트를 위해 ListRoomController가 TCP/IP 연결을 관리합니다
 * 1. 클라이언트가 소켓에 연결하면 스레드를 실행하여 정보를 제공합니다.
 * 2. 채팅모델 인스턴스를 사용하여 데이터베이스에서 정보를 읽는 스레드
 * 3. 클라이언트가 생성된 응답을 받으면 연결을 끊습니다
 */
public class ListRoomController {

    private final RoomModel roomModel;

    public ListRoomController(RoomModel roomModel) {
        this.roomModel = roomModel;
        run();
    }

    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(10001);
            System.out.println("List room protocol listening from port:10001");
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new ListRoomConnectionThread(socket, roomModel)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
