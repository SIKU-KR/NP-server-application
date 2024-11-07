package core.view;

import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class OutStreamViewTest {

    private Socket mockSocket;

    static class TestMessage {
        private String message;
        private int userId;

        // Constructor, getters, setters, equals, and hashCode
        public TestMessage(String message, int userId) {
            this.message = message;
            this.userId = userId;
        }

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
    void testSendValidJson() throws IOException {
        // Set up Piped streams to capture the PrintWriter output
        PipedOutputStream outputStream = new PipedOutputStream();
        PipedInputStream inputStream = new PipedInputStream(outputStream);

        when(mockSocket.getOutputStream()).thenReturn(outputStream);

        OutStreamView<TestMessage> outStreamView = new OutStreamView<>(mockSocket);

        // Create a TestMessage object and send it
        TestMessage testMessage = new TestMessage("Hello, World!", 42);
        outStreamView.send(testMessage);

        // Capture the output as a string
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String jsonOutput = reader.readLine();

        // Create the expected JSON string using Gson
        Gson gson = new Gson();
        String expectedJson = gson.toJson(testMessage);

        // Verify that the output JSON matches the expected JSON
        assertEquals(expectedJson, jsonOutput);
    }
}