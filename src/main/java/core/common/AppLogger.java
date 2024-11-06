package core.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Log-writer class using Log4j
 * 1. Supports 4-level logging (info, error, debug, warn)
 */
public class AppLogger {

    private static final Logger logger = LogManager.getLogger(AppLogger.class);

    public static void info(String message) {
        logger.info(message);
    }

    public static void error(String message) {
        logger.error(message);
    }

    public static void debug(String message) {
        logger.debug(message);
    }

    public static void warn(String message) {
        logger.warn(message);
    }

}