package core.view;

import com.google.gson.JsonSyntaxException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InStreamViewTest {

    private Socket mockSocket;

    static class TestMessage {
        private String message;
        private int userId;

        // Getters and setters
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        // Override equals and hashCode for object comparison
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestMessage that = (TestMessage) o;
            return userId == that.userId && message.equals(that.message);
        }

        @Override
        public int hashCode() {
            return Objects.hash(message, userId);
        }
    }

    @BeforeEach
    void setUp() {
        mockSocket = mock(Socket.class);
    }

    @Test
    void testReadValidJson() throws IOException, JsonSyntaxException {
        String jsonInput = "{\"message\":\"Hello, World!\",\"userId\":42}";

        // Set up a PipedInputStream to simulate socket input
        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(inputStream);
        outputStream.write((jsonInput + "\n").getBytes());
        outputStream.flush();
        outputStream.close();

        when(mockSocket.getInputStream()).thenReturn(inputStream);

        InStreamView<TestMessage> inStreamView = new InStreamView<>(mockSocket, TestMessage.class);
        TestMessage result = inStreamView.read();

        TestMessage expectedMessage = new TestMessage();
        expectedMessage.setMessage("Hello, World!");
        expectedMessage.setUserId(42);

        assertEquals(expectedMessage, result);
    }

    @Test
    void testReadInvalidJson() throws IOException {
        String invalidJson = "{\"message\":\"Hello, World!\",\"userId\":\"notAnInteger\"}";

        // Set up a PipedInputStream to simulate socket input
        PipedInputStream inputStream = new PipedInputStream();
        PipedOutputStream outputStream = new PipedOutputStream(inputStream);
        outputStream.write((invalidJson + "\n").getBytes());
        outputStream.flush();
        outputStream.close();

        when(mockSocket.getInputStream()).thenReturn(inputStream);

        InStreamView<TestMessage> inStreamView = new InStreamView<>(mockSocket, TestMessage.class);

        assertThrows(JsonSyntaxException.class, inStreamView::read);
    }

    @Test
    void testReadIOException() throws IOException {
        when(mockSocket.getInputStream()).thenThrow(new IOException("Socket closed"));

        assertThrows(IOException.class, () -> new InStreamView<>(mockSocket, TestMessage.class));
    }
}