package com.trinisoft.libraries;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PropertyHelper {

    static final Logger logger = Logger.getLogger(PropertyHelper.class.getName());

    public Properties getProperties(String path) throws IOException {
        InputStream is = null;
        if (path == null) {
            return new Properties();
        } else {
            Properties properties = new Properties();
            logger.log(Level.INFO, "trying to load from current dir");
            try {
                is = new FileInputStream(new File(".\\" + path));
            } catch (FileNotFoundException fne) {
                logger.log(Level.SEVERE, "", fne.getMessage());
            }

            if (is == null) {
                logger.log(Level.INFO, "trying to load from path as specified");
                try {
                    is = new FileInputStream(new File(path));
                } catch (FileNotFoundException fne) {
                    logger.log(Level.SEVERE, "", fne.getMessage());
                }
            }

            if (is == null) {
                logger.log(Level.INFO, "trying to load from user home directory");
                try {
                    is = new FileInputStream(new File(System.getProperty("user.home") + File.separatorChar + path));
                } catch (FileNotFoundException fnfe) {
                    logger.log(Level.SEVERE, "", fnfe.getMessage());
                }
            }

            if (is == null) {
                logger.log(Level.INFO, "trying to load from jar file");
                try {
                    is = getClass().getClassLoader().getResourceAsStream(path);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "", e.getMessage());
                }
            }

            if(is == null) {
                logger.log(Level.INFO, "trying to load from /etc");
                try {
                    is = new FileInputStream(new File("/etc/" + path));
                } catch(FileNotFoundException fnfe) {
                    fnfe.getStackTrace();
                }
            }
            if (is == null) {
                return properties;
            } else {
                properties.load(is);
                return properties;
            }
        }
    }
}
