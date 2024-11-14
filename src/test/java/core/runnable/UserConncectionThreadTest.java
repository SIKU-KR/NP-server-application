package core.runnable;

import core.common.RequestType;
import core.controller.ServerSocketController;
import core.dto.DTO;
import core.dto.requestmsg.UserLogin;
import core.dto.response.User;
import core.model.UserModel;
import core.view.InStreamView;
import core.view.OutStreamView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class UserConncectionThreadTest {

    UserModel userModel = new UserModel();
    static int port = 8888;
    private ExecutorService serverExecutor;

    UserLogin userLogin1 = new UserLogin("Charlie");
    UserLogin userLogin2 = new UserLogin("박범식");

    @BeforeEach
    void setUp() throws Exception {
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
    void tearDown() throws Exception {
        userModel.deleteUser("박범식");
        ServerSocketController.stopServer();
        serverExecutor.shutdown();
        if (!serverExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
            serverExecutor.shutdownNow();
        }
    }

    @Test
    void 원래있는유저로그인() throws Exception {
        try(Socket socket = new Socket("localhost", port)){
            OutStreamView<DTO> outStreamView = new OutStreamView<>(socket);
            DTO dto = new DTO(RequestType.LOGIN, userLogin1);
            outStreamView.send(dto);
            InStreamView<User> in = new InStreamView<>(socket, User.class);
            User input = in.read();
            assertEquals(userLogin1.getUsername(), input.getUsername());
            assertEquals(input.getScore(), 1100);
        }
    }

    @Test
    void 새로운유저로그인() throws Exception {
        try(Socket socket = new Socket("localhost", port)){
            OutStreamView<DTO> outStreamView = new OutStreamView<>(socket);
            DTO dto = new DTO(RequestType.LOGIN, userLogin2);
            outStreamView.send(dto);
            InStreamView<User> in = new InStreamView<>(socket, User.class);
            User input = in.read();
            assertEquals(userLogin2.getUsername(), input.getUsername());
            assertEquals(input.getScore(), 0);
        }
    }


}