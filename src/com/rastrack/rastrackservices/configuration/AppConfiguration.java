/*                                             
 *                            
 *Proyecto: 
 *Módulo:
 *Autor: Jorge Espinosa 
 *Fecha: 02/04/2019
 *Descripción: 
 *
 */
package com.rastrack.rastrackservices.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AppConfiguration {
    
    private static AppConfiguration config;
    public static boolean FIRTSTIME = true;
    
    public static AppConfiguration getInstance() {
        if (config == null) {
            config = new AppConfiguration();
        }
        return config;
    }
    public static final String URL = "url";
    public static final String DRIVER = "driver";
    public static final String USER = "user";
    public static final String PASSWORD = "password";
    public static final String ATTEMPTNUMBER = "attemptNumber";
    public static final String MILLS_TO_WAIT = "millsToWait";
    public static final String STOP_THREADS = "loadConfiguration";
    public static final String MILLS_TO_WAIT_MAIN = "millsToWaitMain";
    public static final String RUNSTATUS = "runStatus";
    public static final String PORT = "port";
    public static final String STOP_MAIN_THREAD = "stopMainThread";
    public static final String SHOWACTIVETHREADS = "showActiveThreads";
    public String properties = "configuration.properties";
    public static final String PARAMETERIZED_VEHICLES = "parameterizedVehicles";
    private Properties pro;
    
    private AppConfiguration() {
        
    }
    
    public synchronized void loadConfiguration() throws IOException {
        FileInputStream fl = null;
        try {
            File file = new File(AppConfiguration.class
                    .getProtectionDomain().getCodeSource().getLocation().getPath());
            if (file.isFile()) {
                fl = new FileInputStream(file.getParent() + File.separator + properties);
            } else {
                fl = new FileInputStream(file.getPath() + File.separator + properties);
            }
            pro = new Properties();
            pro.load(fl);
        } finally {
            if (fl != null) {
                fl.close();
            }
        }
    }
    
    public synchronized void saveProperty(String name, Object value) {
        pro.setProperty(name, value.toString());
    }
    
    public synchronized void saveProperties() {
        File fl = null;
        try {
            File file = new File(AppConfiguration.class
                    .getProtectionDomain().getCodeSource().getLocation().getPath());
            if (file.isFile()) {
                fl = new File(file.getParent() + File.separator + properties);
            } else {
                fl = new File(file.getPath() + File.separator + properties);
            }
            pro.store(new FileWriter(fl), "");
        } catch (IOException ex) {
            Logger.getLogger(AppConfiguration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public synchronized FTPServiceConfiguration getFtpConfigurationService(int service) {
        String ftpProperty = "ftp.service." + service + ".";
        if (getStringProperty(ftpProperty + FTPServiceConfiguration.NAME) != null) {
            FTPServiceConfiguration configftp = new FTPServiceConfiguration(getStringProperty(ftpProperty + FTPServiceConfiguration.PATH), getStringProperty(ftpProperty + FTPServiceConfiguration.OLD_PATH), getStringProperty(ftpProperty + FTPServiceConfiguration.REPORTS_PATH), getIntegerProperty(ftpProperty + FTPServiceConfiguration.FREQUENCY), getStringProperty(ftpProperty + FTPServiceConfiguration.NAME), getBooleanProperty(ftpProperty + FTPServiceConfiguration.STOP), getIntegerProperty(ftpProperty + FTPServiceConfiguration.THREADSNUMBER), getIntegerProperty(MILLS_TO_WAIT));
            configftp.setParameterized(getInstance().getBooleanProperty(PARAMETERIZED_VEHICLES));
            configftp.setFailed_path(getStringProperty(ftpProperty + FTPServiceConfiguration.FAILED_PATH));
            configftp.setId(getIntegerProperty(ftpProperty + FTPServiceConfiguration.ID));
            return configftp;
        }
        return null;
    }
    
    public synchronized DBServiceConfiguration getDBConfigurationService(int service) {
        String dbProperty = "db.service." + service + ".";
        if (getStringProperty(dbProperty + DBServiceConfiguration.NAME) != null) {
            DBServiceConfiguration configftp = new DBServiceConfiguration(getIntegerProperty(dbProperty + DBServiceConfiguration.FREQUENCY), getStringProperty(dbProperty + DBServiceConfiguration.NAME), getBooleanProperty(dbProperty + DBServiceConfiguration.STOP), getIntegerProperty(dbProperty + DBServiceConfiguration.THREADSNUMBER), getIntegerProperty(MILLS_TO_WAIT));
            configftp.setParameterized(getInstance().getBooleanProperty(PARAMETERIZED_VEHICLES));
            configftp.setId(getIntegerProperty(dbProperty + DBServiceConfiguration.ID));
            return configftp;
        }
        return null;
    }
    
    public synchronized WSServiceConfiguration getWSConfigurationService(int service) {
        String wsProperty = "ws.service." + service + ".";
        if (getStringProperty(wsProperty + WSServiceConfiguration.NAME) != null) {
            WSServiceConfiguration configftp = new WSServiceConfiguration(getStringProperty(wsProperty + WSServiceConfiguration.URL), getIntegerProperty(wsProperty + WSServiceConfiguration.ID), getBooleanProperty(wsProperty + WSServiceConfiguration.STOP), getIntegerProperty(wsProperty + WSServiceConfiguration.FREQUENCY), getStringProperty(wsProperty + WSServiceConfiguration.NAME), getIntegerProperty(wsProperty + WSServiceConfiguration.THREADSNUMBER), getIntegerProperty(MILLS_TO_WAIT));
            configftp.setGoogleURL(getStringProperty(wsProperty + WSServiceConfiguration.GOOGLE_URL));
            configftp.setParameterized(getInstance().getBooleanProperty(PARAMETERIZED_VEHICLES));
            return configftp;
        }
        return null;
    }
    
    public synchronized String getStringProperty(String name) {
        return pro.getProperty(name);
    }
    
    public synchronized int getIntegerProperty(String name) {
        try {
            return Integer.parseInt(pro.getProperty(name));
        } catch (Exception ex) {
            return 0;
        }
    }
    
    public synchronized boolean getBooleanProperty(String name) {
        return Boolean.parseBoolean(pro.getProperty(name));
    }
}
