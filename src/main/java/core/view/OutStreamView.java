package core.view;


import com.google.gson.Gson;
import core.common.GsonProvider;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class OutStreamView<T> {
    private final PrintWriter out;
    private final Gson gson;

    public OutStreamView(Socket socket) throws IOException {
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.gson = GsonProvider.getGson();
    }

    public void send(T message){
        String json = gson.toJson(message);
        out.println(json);
    }
}
