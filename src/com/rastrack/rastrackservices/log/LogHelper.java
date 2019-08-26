package com.rastrack.rastrackservices.log;

import com.rastrack.rastrackservices.configuration.AppConfiguration;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;

/**
 * Clase que se encarga de llevar el log de aplicación.
 *
 * @author jespinosap
 *
 */
public class LogHelper {

    private static boolean cargar = true;
    public static final String ORIGINAL_NAME = "RastrackServices.log";
    public static final String LOGGER_PROPERTIES = "log4j.properties";

    public static Logger getLogger(String name) {
        LoggerContext context = (LoggerContext) LogManager.getContext(false);
        if (cargar) {
            File fl = null; // Start logging system
            //context.initialize();
            File file = new File(LogHelper.class
                    .getProtectionDomain().getCodeSource().getLocation().getPath());
            if (file.isFile()) {
                fl = new File(file.getParent() + File.separator + LOGGER_PROPERTIES);
            } else {
                fl = new File(file.getPath() + File.separator + LOGGER_PROPERTIES);
            }
            context.setConfigLocation(fl.toURI());
            cargar = false;

        }
        // Get a reference for logger
        return context.getLogger(name);
    }

    public static Logger getLogger(@SuppressWarnings("rawtypes") Class c) {
        if (cargar) {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            try {
                context.setConfigLocation(LogHelper.class
                        .getResource("/log4j.properties").toURI());
            } catch (URISyntaxException ex) {
                java.util.logging.Logger.getLogger(LogHelper.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
            cargar = false;
        }
        Logger log = LogManager.getLogger(c);
        return log;
    }

    /**
     * Establece el nombre del archivo de log.
     *
     * @param c
     * @param path
     * @param fileName
     */
    /* public static void setFileName(@SuppressWarnings("rawtypes") Class c,
            String path, String fileName) {
        Calendar date = Calendar.getInstance();
        if (!fileName.equals(ORIGINAL_NAME)) {
            SimpleDateFormat formato = new SimpleDateFormat("yyyyMMddHHmmss");
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            fileName += formato.format(date.getTime()) + ".log";
        } else {
            SimpleDateFormat formato = new SimpleDateFormat("yyyyMM");
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            fileName += formato.format(date.getTime()) + ".log";
        }
        RollingFileAppender fa = RollingFileManager.getFileManager(fileName, true, true, true, true, path + File.separator + fileName, new PatternLayout("%d %-5p [%c{1}] %m%n"), 20, fileName, fileName, fileName, configuration).newBuilder().build();
        fa.setName(fileName);
        fa.setFile(path + File.separator + fileName);
        fa.setLayout(new PatternLayout("%d %-5p [%c{1}] %m%n"));
        fa.setThreshold(Level.ALL);
        fa.setAppend(true);
        fa.activateOptions();
        LogManager.getLogger(c).addAppender(fa);
    }*/
}
