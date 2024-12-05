package core;

import core.common.RequestType;
import core.dto.DTO;
import core.dto.requestmsg.ChatConnection;
import core.view.OutStreamView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.Socket;

public class testClient {

    Integer chatId = 1;
    ChatConnection chco1 = new ChatConnection("Alice", chatId);
    ChatConnection chco2 = new ChatConnection("Bob", chatId);

    @Test
    public void test() {
        try(Socket sc1 = new Socket("43.203.212.19", 10001);
            Socket sc2 = new Socket("43.203.212.19", 10001); ){

            OutStreamView<DTO> out1 = new OutStreamView<>(sc1);
            out1.send(new DTO(RequestType.CONNECTCHAT, chco1));
            Thread.sleep(100);
            OutStreamView<DTO> out2 = new OutStreamView<>(sc2);
            out2.send(new DTO(RequestType.CONNECTCHAT, chco2));
            Thread.sleep(100);

            Assertions.assertTrue(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
