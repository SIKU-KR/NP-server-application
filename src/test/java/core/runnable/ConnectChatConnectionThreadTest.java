package core.runnable;

import core.controller.MsgQueueController;
import core.common.RequestType;
import core.controller.ChatThreadsController;
import core.controller.ServerSocketController;
import core.dto.DTO;
import core.dto.Message;
import core.dto.requestmsg.ChatConnection;
import core.model.ChatModel;
import core.model.InMemoryChatCounts;
import core.model.InMemoryLastSenders;
import core.view.InStreamView;
import core.view.OutStreamView;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.Socket;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;

class ConnectChatConnectionThreadTest {

    Integer chatId = 1;
    static int port = 8888;

    private Thread serverThread;
    private Thread consumerThread;

    ChatModel chatModel = new ChatModel();
    ChatThreadsController chatThreadsController;

    InMemoryChatCounts inMemoryChatCounts = InMemoryChatCounts.getInstance();
    InMemoryLastSenders inMemoryLastSenders = InMemoryLastSenders.getInstance();

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
        chatModel.removeMsgsForTest(chatId);
        inMemoryChatCounts.clearKey(chatId);
        inMemoryLastSenders.clearkey(chatId);
    }

    @Test
    @DisplayName("2개 생성해서 스레드 리스트에 잘 등록되는지 테스트")
    void 채팅테스트1() throws IOException, InterruptedException {
        try (Socket sc1 = new Socket("localhost", port);
             Socket sc2 = new Socket("localhost", port)) {

            // 채팅 연결 및 스트림 연결
            OutStreamView<DTO> out1 = new OutStreamView<>(sc1);
            out1.send(new DTO(RequestType.CONNECTCHAT, chco1));

            OutStreamView<DTO> out2 = new OutStreamView<>(sc2);
            out2.send(new DTO(RequestType.CONNECTCHAT, chco2));

            Thread.sleep(100);

            List<ConnectChatConnectionThread> list = chatThreadsController.getThreads(chatId);
            assertEquals(2, list.size());
        }
    }

    @Test
    @DisplayName("채팅스레드 열었다가 삭제(종료)하는 기능 테스트")
    void 채팅테스트2() throws IOException, InterruptedException {
        try (Socket sc1 = new Socket("localhost", port)) {
            OutStreamView<DTO> out1 = new OutStreamView<>(sc1);
            out1.send(new DTO(RequestType.CONNECTCHAT, chco1));
            Thread.sleep(100);
            assertEquals(1, chatThreadsController.getThreads(chatId).size());

            ConnectChatConnectionThread th = chatThreadsController.getThreads(chatId).get(0);
            th.stop();
            Thread.sleep(100);
            assertEquals(0, chatThreadsController.getThreads(chatId).size());
            assertTrue(th.getSocket().isClosed());
            assertTrue(th.getSocket().isConnected());
        }
    }

    @Test
    @DisplayName("서버에서 메시지를 보냈을 때 받는지 확인")
    void 채팅테스트3() throws IOException, InterruptedException {
        try (Socket sc1 = new Socket("localhost", port)) {
            // 채팅 연결 및 스트림 연결
            OutStreamView<DTO> out1 = new OutStreamView<>(sc1);
            out1.send(new DTO(RequestType.CONNECTCHAT, chco1));
            Thread.sleep(100);

            // 메시지 보내는 쪽
            ConnectChatConnectionThread th1 = chatThreadsController.getThreads(chatId).get(0);
            th1.sendMessage(new Message(chco1.getChatId(), chco1.getUsername(), "Test message"));
            Thread.sleep(100);

            // 메시지 읽는쪽
            InStreamView<Message> in1 = new InStreamView<>(sc1, Message.class);
            Message msg = in1.read();

            // 테스트 케이스
            assertEquals(chco1.getChatId(), msg.getChatId());
            assertEquals(chco1.getUsername(), msg.getUsername());
            assertEquals("Test message", msg.getMessage());
        }
    }

    @Test
    @DisplayName("쓰레드 A가 보냈을 떄 B가 받는지 확인")
    void 채팅테스트4() throws IOException, InterruptedException {
        try (Socket sc1 = new Socket("localhost", port);
             Socket sc2 = new Socket("localhost", port)) {

            // 채팅 연결 및 스트림 연결
            OutStreamView<DTO> out1 = new OutStreamView<>(sc1);
            out1.send(new DTO(RequestType.CONNECTCHAT, chco1));
            Thread.sleep(100);

            OutStreamView<DTO> out2 = new OutStreamView<>(sc2);
            out2.send(new DTO(RequestType.CONNECTCHAT, chco2));
            Thread.sleep(100);

            // 메시지 보내기
            OutStreamView<Message> msgOut = new OutStreamView<>(sc1);
            msgOut.send(new Message(chco1.getChatId(), chco1.getUsername(), "Test message"));
            Thread.sleep(100);

            // 메시지 수신하기
            InStreamView<Message> in1 = new InStreamView<>(sc2, Message.class);
            Message msg = in1.read();
            assertEquals(chco1.getChatId(), msg.getChatId());
            assertEquals(chco1.getUsername(), msg.getUsername());
            assertEquals("Test message", msg.getMessage());
            assertEquals("Alice", inMemoryLastSenders.getValue(chatId));
            assertEquals(1, inMemoryChatCounts.getValue(chatId));
        }
    }

    @Test
    @DisplayName("쓰레드 A가 보냈을 떄 B, C가 받는지 확인 (단체 채팅)")
    void 채팅테스트5() throws IOException, InterruptedException {
        try (Socket sc1 = new Socket("localhost", port);
             Socket sc2 = new Socket("localhost", port);
             Socket sc3 = new Socket("localhost", port);) {

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

            // 메시지 보내기
            OutStreamView<Message> msgOut = new OutStreamView<>(sc1);
            msgOut.send(new Message(chco1.getChatId(), chco1.getUsername(), "Test message"));
            Thread.sleep(100);

            // 메시지 수신하기
            InStreamView<Message> in1 = new InStreamView<>(sc2, Message.class);
            Message msg = in1.read();
            assertEquals(chco1.getChatId(), msg.getChatId());
            assertEquals(chco1.getUsername(), msg.getUsername());
            assertEquals("Test message", msg.getMessage());

            // 메시지 수신하기
            InStreamView<Message> in2 = new InStreamView<>(sc3, Message.class);
            Message msg2 = in2.read();
            assertEquals(chco1.getChatId(), msg2.getChatId());
            assertEquals(chco1.getUsername(), msg2.getUsername());
            assertEquals("Test message", msg2.getMessage());
            assertEquals("Alice", inMemoryLastSenders.getValue(chatId));
            assertEquals(1, inMemoryChatCounts.getValue(chatId));
        }
    }

    @Test
    @DisplayName("명령어 확인해보기 - 출제")
    void 채팅테스트6() throws IOException, InterruptedException {
        try (Socket sc1 = new Socket("localhost", port);
             Socket sc2 = new Socket("localhost", port);) {
            OutStreamView<DTO> out1 = new OutStreamView<>(sc1);
            out1.send(new DTO(RequestType.CONNECTCHAT, chco1));
            Thread.sleep(100);
            OutStreamView<DTO> out2 = new OutStreamView<>(sc2);
            out2.send(new DTO(RequestType.CONNECTCHAT, chco2));
            Thread.sleep(100);

            OutStreamView<Message> msgOut = new OutStreamView<>(sc1);
            msgOut.send(new Message(chco1.getChatId(), chco1.getUsername(), "/문제 홍길동"));
            Thread.sleep(100);

            InStreamView<Message> in1 = new InStreamView<>(sc1, Message.class);
            InStreamView<Message> in2 = new InStreamView<>(sc2, Message.class);

            Message msg1 = in1.read();
            assertEquals("admin", msg1.getUsername());
            assertTrue(msg1.getMessage().contains("게임을 새로"));
            Message msg2 = in2.read();
            assertEquals("admin", msg2.getUsername());
            assertTrue(msg2.getMessage().contains("게임을 새로"));
        }
    }

    @Test
    @DisplayName("명령어 확인해보기 - 네")
    void 채팅테스트7() throws IOException, InterruptedException {
        try (Socket sc1 = new Socket("localhost", port);
             Socket sc2 = new Socket("localhost", port);) {
            OutStreamView<DTO> out1 = new OutStreamView<>(sc1);
            out1.send(new DTO(RequestType.CONNECTCHAT, chco1));
            Thread.sleep(100);
            OutStreamView<DTO> out2 = new OutStreamView<>(sc2);
            out2.send(new DTO(RequestType.CONNECTCHAT, chco2));
            Thread.sleep(100);

            OutStreamView<Message> msgOut = new OutStreamView<>(sc1);
            msgOut.send(new Message(chco1.getChatId(), chco1.getUsername(), "/네"));
            Thread.sleep(100);

            InStreamView<Message> in1 = new InStreamView<>(sc1, Message.class);
            InStreamView<Message> in2 = new InStreamView<>(sc2, Message.class);

            Message msg2 = in2.read();
            assertEquals(chco1.getUsername(), msg2.getUsername());
            assertTrue(msg2.getMessage().contains("네"));
        }
    }

    @Test
    @DisplayName("명령어 확인해보기 - 정답")
    void 채팅테스트8() throws IOException, InterruptedException {
        try (Socket sc1 = new Socket("localhost", port);
             Socket sc2 = new Socket("localhost", port);) {
            OutStreamView<DTO> out1 = new OutStreamView<>(sc1);
            out1.send(new DTO(RequestType.CONNECTCHAT, chco1));
            Thread.sleep(100);
            OutStreamView<DTO> out2 = new OutStreamView<>(sc2);
            out2.send(new DTO(RequestType.CONNECTCHAT, chco2));
            Thread.sleep(100);

            inMemoryLastSenders.storeValue(chatId, chco1.getUsername());
            OutStreamView<Message> msgOut = new OutStreamView<>(sc1);
            msgOut.send(new Message(chco1.getChatId(), chco1.getUsername(), "/정답"));
            Thread.sleep(100);

            InStreamView<Message> in1 = new InStreamView<>(sc1, Message.class);
            InStreamView<Message> in2 = new InStreamView<>(sc2, Message.class);

            Message msg1 = in1.read();
            assertEquals("admin", msg1.getUsername());
            assertTrue(msg1.getMessage().contains(chco1.getUsername()));
            Message msg2 = in2.read();
            assertEquals("admin", msg2.getUsername());
            assertTrue(msg2.getMessage().contains(chco1.getUsername()));
        }
    }

}