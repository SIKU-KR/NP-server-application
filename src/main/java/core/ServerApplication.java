package core;

import core.common.DBConnection;
import core.controller.ServerSocketController;
import core.runnable.MsgQueueConsumer;

import java.sql.SQLException;

/**
 * Start point of Server Application
 * 1. main thread - runs ServerSocketController
 * 2. runs MsgQueue Consumer class asynchronously
 */
public class ServerApplication {

    private static final int PORT = 10001;

    public static void main(String[] args) throws SQLException {
        DBConnection.getConnection();
        new Thread(new MsgQueueConsumer()).start();
        ServerSocketController.getInstance(PORT).run();
    }

}