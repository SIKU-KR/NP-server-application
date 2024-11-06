package core;

import core.controller.ServerSocketController;
import core.runnable.MsgQueueConsumer;

/**
 * Start point of Server Application
 * 1. main thread - runs ServerSocketController
 * 2. runs MsgQueue Consumer class asynchronously
 */
public class ServerApplication {

    private static final int PORT = 10001;

    public static void main(String[] args) {
        new Thread(new MsgQueueConsumer()).start();
        ServerSocketController.getInstance(PORT).run();
    }

}