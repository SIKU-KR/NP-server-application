package core.runnable;

import core.common.RequestType;
import core.controller.ServerSocketController;
import core.dto.DTO;
import core.dto.response.ListChatRoom;
import core.view.InStreamView;
import core.view.OutStreamView;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ListRoomConnectionThreadTest {

    static int port = 8888;
    private ExecutorService serverExecutor;

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
        ServerSocketController.stopServer();

        // 서버 스레드 종료
        serverExecutor.shutdown();
        if (!serverExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
            serverExecutor.shutdownNow();
        }
    }

    @Test
    void RoomConnectionIntegrationTest() {
        DTO dto = new DTO(RequestType.ROOMLIST, null);

        try {
            Socket socket = new Socket("localhost", port);
            OutStreamView<DTO> out = new OutStreamView<>(socket);
            InStreamView<ListChatRoom> in = new InStreamView<>(socket, ListChatRoom.class);
            out.send(dto);
            ListChatRoom response = in.read();
            assertTrue(response.getChatRooms().size() == 5);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}