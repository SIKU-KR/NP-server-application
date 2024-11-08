package core.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Log-writer class using Log4j
 * 1. Supports 4-level logging (info, error, debug, warn)
 */
public class AppLogger {

    private static final Logger logger = LogManager.getLogger(AppLogger.class);

    public static void info(String message) {
        logger.info(message);
    }

    public static void formalInfo(ServerSocket socket, String type, String message) {
        String host = socket.getInetAddress().getHostName();
        int port = socket.getLocalPort();
        String merged = "[" + host + ":" + port + "] - " + type + " - " + message;
        logger.info(merged);
    }

    public static void formalInfo(Socket socket, String type, String message) {
        String host = socket.getInetAddress().getHostName();
        int port = socket.getPort();
        String merged = "[" + host + ":" + port + "] - " + type + " - " + message;
        logger.info(merged);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void formalError(Socket socket, Exception e) {
        String host = socket.getInetAddress().getHostAddress();
        int port = socket.getPort();
        String type = e.getClass().getTypeName();
        String message = e.getMessage();

        StackTraceElement element = e.getStackTrace()[0];
        String className = element.getClassName();
        int lineNumber = element.getLineNumber();

        String merged = String.format("[%s:%d] - %s - %s at %s:%d", host, port, type, message, className, lineNumber);
        logger.error(merged);
    }

    public static void formalError(String host, int port, String type, String message) {
        String merged = "[" + host + ":" + port + "] - " + type + " - " + message;
        logger.error(merged);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

}