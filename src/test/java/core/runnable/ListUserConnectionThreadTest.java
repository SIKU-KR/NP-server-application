package core.runnable;

import core.common.RequestType;
import core.controller.ChatThreadsController;
import core.controller.MsgQueueController;
import core.controller.ServerSocketController;
import core.dto.DTO;
import core.dto.requestmsg.ChatConnection;
import core.dto.response.ListUser;
import core.view.InStreamView;
import core.view.OutStreamView;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListUserConnectionThreadTest {

    Integer chatId = 1;
    static int port = 8888;

    private Thread serverThread;
    private Thread consumerThread;

    ChatThreadsController chatThreadsController;

    ChatConnection chco1 = new ChatConnection("Alice", chatId);
    ChatConnection chco2 = new ChatConnection("Bob", chatId);
    ChatConnection chco3 = new ChatConnection("Charlie", chatId);

    @BeforeEach
    void setUp() throws InterruptedException, IOException {
        // Start ServerSocketController in its own thread
        serverThread = new Thread(() -> {
            ServerSocketController serverSocketController = ServerSocketController.getInstance(port);
            serverSocketController.run();
        });
        serverThread.start();
        Thread.sleep(100);

        // Start MsgQueueConsumer in its own thread
        consumerThread = new Thread(() -> {
            MsgQueueConsumer msgQueueConsumer = new MsgQueueConsumer();
            msgQueueConsumer.run();
        });
        consumerThread.start();
        Thread.sleep(100);

        // Prepare ChatThreadsController
        chatThreadsController = ChatThreadsController.getInstance();
        Thread.sleep(100);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        ServerSocketController.stopServer();
        consumerThread.interrupt();

        MsgQueueController.getInstance().clear();

        // Wait for server and consumer threads to finish
        if (serverThread.isAlive()) {
            serverThread.join(1000); // Wait for the server thread to finish
        }

        if (consumerThread.isAlive()) {
            consumerThread.join(1000); // Wait for the consumer thread to finish
        }

        chatThreadsController.getThreads(chatId).clear();
    }

    @Test
    public void 몇명접속했는지받아오는거테스트() throws Exception {
        try (Socket sc1 = new Socket("localhost", port);
             Socket sc2 = new Socket("localhost", port);
             Socket sc3 = new Socket("localhost", port);
             Socket sc4 = new Socket("localhost", port);) {

            // 채팅 연결 및 스트림 연결
            OutStreamView<DTO> out1 = new OutStreamView<>(sc1);
            out1.send(new DTO(RequestType.CONNECTCHAT, chco1));
            Thread.sleep(100);

            OutStreamView<DTO> out2 = new OutStreamView<>(sc2);
            out2.send(new DTO(RequestType.CONNECTCHAT, chco2));
            Thread.sleep(100);

            OutStreamView<DTO> out3 = new OutStreamView<>(sc3);
            out3.send(new DTO(RequestType.CONNECTCHAT, chco3));
            Thread.sleep(100);

            new DTO(RequestType.USERLIST, Integer.valueOf(chatId));
            OutStreamView<DTO> out4 = new OutStreamView<>(sc4);
            out4.send(new DTO(RequestType.USERLIST, chatId));
            InStreamView<ListUser> in = new InStreamView<>(sc4, ListUser.class);
            ListUser response = in.read();
            Assertions.assertTrue(response.getUsers().contains("Alice"));
            Assertions.assertTrue(response.getUsers().contains("Bob"));
            Assertions.assertTrue(response.getUsers().contains("Charlie"));
        }
    }


}