/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.trinisoft.libraries;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import java.util.logging.StreamHandler;

/**
 *
 * @author leapsoft
 */
public class LoggingUtils {
/**
 *
 * @param fileName
 * @param parent if this is null, then the log file is put in current_directory/logs
 * @param className if this is null, then we return an anonymous logger
 * @return
 */
    public static Logger getStreamLogger(String fileName, String parent, String className) throws FileNotFoundException, IOException {
        Logger logger = null;
        if(className == null) {
            logger = Logger.getAnonymousLogger();
        } else {
            logger = Logger.getLogger(className);
        }

        fileName += ".log";

        File file = null;
        if(parent == null) {
            file = new File("logs", fileName);
        } else {
            file = new File(parent, fileName);
        }
        if(!file.exists()) {
            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        OutputStream outputStream = new FileOutputStream(file, true);
        StreamHandler streamHandler = new StreamHandler(outputStream, new SimpleFormatter());
        logger.addHandler(streamHandler);
        return logger;
    }

    /**
     *
     * @param className if this is null, then we return an anonymous logger
     * @return
     */
    public static Logger getConsoleLogger(String className) {
        Logger logger = null;

        if(className == null) {
            logger = Logger.getAnonymousLogger();
        } else {
            logger = Logger.getLogger(className);
        }

        logger.addHandler(new ConsoleHandler());
        return logger;
    }

    public static StreamHandler createStreamHandler(File file) throws FileNotFoundException {
        OutputStream outputStream = new FileOutputStream(file);
        StreamHandler streamHandler = new StreamHandler(outputStream, new SimpleFormatter());
        return streamHandler;
    }

    public static void addHandler(Logger logger, Handler handler) {
        logger.addHandler(handler);
    }
}
