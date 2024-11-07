package core.controller;

import core.controller.ChatThreadsController;
import core.runnable.ConnectChatConnectionThread;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChatThreadsControllerTest {

    private ChatThreadsController chatThreadsController;
    private ConnectChatConnectionThread mockThread1;
    private ConnectChatConnectionThread mockThread2;
    private Integer chatId = 1;

    @BeforeEach
    void setUp() {
        chatThreadsController = ChatThreadsController.getInstance();
        mockThread1 = mock(ConnectChatConnectionThread.class);
        mockThread2 = mock(ConnectChatConnectionThread.class);
    }

    @AfterEach
    void tearDown() {
        chatThreadsController.removeThread(1, mockThread1);
        chatThreadsController.removeThread(1, mockThread2);
    }

    @Test
    void testAddThread() {
        chatThreadsController.addThread(chatId, mockThread1);
        List<ConnectChatConnectionThread> threads = chatThreadsController.getThreads(chatId);

        assertEquals(1, threads.size());
        assertTrue(threads.contains(mockThread1));
    }

    @Test
    void testGetThreadsWhenNoThreadsAdded() {
        List<ConnectChatConnectionThread> threads = chatThreadsController.getThreads(chatId);

        assertTrue(threads.isEmpty(), "Expected an empty list when no threads have been added");
    }

    @Test
    void testRemoveThread() {
        chatThreadsController.addThread(chatId, mockThread1);
        chatThreadsController.addThread(chatId, mockThread2);

        // Remove one of the threads and check if it has been removed
        chatThreadsController.removeThread(chatId, mockThread1);
        List<ConnectChatConnectionThread> threads = chatThreadsController.getThreads(chatId);

        assertEquals(1, threads.size());
        assertFalse(threads.contains(mockThread1));
        assertTrue(threads.contains(mockThread2));
    }

    @Test
    void testRemoveThreadFromNonExistingChatId() {
        // Removing a thread from a chatId that doesn't exist should not cause errors
        chatThreadsController.removeThread(999, mockThread1);
        assertTrue(chatThreadsController.getThreads(999).isEmpty());
    }

    @Test
    void testSingletonInstance() {
        ChatThreadsController instance1 = ChatThreadsController.getInstance();
        ChatThreadsController instance2 = ChatThreadsController.getInstance();

        assertSame(instance1, instance2, "Expected both instances to be the same (singleton)");
    }
}