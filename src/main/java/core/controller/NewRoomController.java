package core.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 대화방 목록을 가져오려는 클라이언트를 위해 ListRoomController가 TCP/IP 연결을 관리합니다
 *1. 클라이언트가 소켓에 연결하면 스레드를 실행하여 새 채팅방을 만듭니다.
 *2. RoomModel 인스턴스를 사용하여 데이터베이스에서 스레드 쓰기 정보
 *3. 클라이언트가 새 채팅방에 들어갈 수 있도록 채팅 컨트롤러로 리디렉션
 *4. 작업 완료 후 연결 끊기
 */
public class NewRoomController {

    private ServerSocket serverSocket;

    public NewRoomController() {
        run();
    }

    public void run(){
        try{
            serverSocket = new ServerSocket(10002);
            System.out.println("New room protocol listening from port:10002");
            while(true){
                Socket socket = serverSocket.accept();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
