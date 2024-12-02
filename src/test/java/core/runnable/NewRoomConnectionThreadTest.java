package core.runnable;

import core.common.RequestType;
import core.controller.ServerSocketController;
import core.dto.DTO;
import core.dto.requestmsg.NewRoom;
import core.dto.response.ChatRoom;
import core.dto.response.ListChatRoom;
import core.model.RoomModel;
import core.view.InStreamView;
import core.view.OutStreamView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class NewRoomConnectionThreadTest {

    // 테스트 셋업
    RoomModel roomModel = new RoomModel();
    static int port = 8888;
    private ExecutorService serverExecutor;

    // 테스트 데이터
    NewRoom newRoom = new NewRoom("bumshik", "테스트게임방");
    DTO dto = new DTO(RequestType.NEWROOM, newRoom);

    @BeforeEach
    void setUp() throws InterruptedException {
        // 서버 실행을 위한 스레드 풀 생성
        serverExecutor = Executors.newSingleThreadExecutor();

        // 서버 실행
        serverExecutor.submit(() -> {
            ServerSocketController serverSocketController = ServerSocketController.getInstance(port);
            serverSocketController.run();
        });

        // 서버가 준비될 때까지 잠시 대기
        Thread.sleep(100);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        // 테스트 데이터 삭제
        roomModel.removeRoomForTest("테스트게임방");

        // 서버 스레드 종료
        ServerSocketController.stopServer();
        serverExecutor.shutdown();
        if (!serverExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
            serverExecutor.shutdownNow();
        }

    }

    @Test
    void NewRoomConnectionIntegrationTest() {
        try (Socket socket = new Socket("localhost", port)
        ) {
            OutStreamView<DTO> out = new OutStreamView<>(socket);
            out.send(dto);
            InStreamView<ChatRoom> in = new InStreamView<>(socket, ChatRoom.class);
            ChatRoom response = in.read();
            assertEquals("테스트게임방", response.getName());
            assertEquals("bumshik", response.getCreator());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void NewRoomConnectionIntegrationTest2() {
        // data input
        try (Socket socket = new Socket("localhost", port)
        ) {
            OutStreamView<DTO> out = new OutStreamView<>(socket);
            out.send(dto);
            InStreamView<ChatRoom> in = new InStreamView<>(socket, ChatRoom.class);
            ChatRoom response = in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // check is test ok
        DTO dtoForListRoom = new DTO(RequestType.ROOMLIST, null);
        try (Socket socket = new Socket("localhost", port)
        ) {
            OutStreamView<DTO> out = new OutStreamView<>(socket);
            out.send(dtoForListRoom);
            InStreamView<ListChatRoom> in = new InStreamView<>(socket, ListChatRoom.class);
            ListChatRoom response = in.read();
            boolean hasTestGameRoom = response.getChatRooms().stream()
                    .anyMatch(chatRoom -> "테스트게임방".equals(chatRoom.getName()));
            assertTrue(hasTestGameRoom);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}