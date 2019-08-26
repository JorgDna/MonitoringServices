package com.rastrack.rastrackservices.main;

import com.rastrack.rastrackservices.configuration.AppConfiguration;
import com.rastrack.rastrackservices.configuration.DBServiceConfiguration;
import com.rastrack.rastrackservices.configuration.FTPServiceConfiguration;
import com.rastrack.rastrackservices.configuration.WSServiceConfiguration;
import com.rastrack.rastrackservices.log.LogHelper;
import com.rastrack.rastrackservices.processing.DBProcessing;
import org.apache.logging.log4j.Logger;
import com.rastrack.rastrackservices.processing.ThreadProcessing;
import com.rastrack.rastrackservices.processing.FTPProcessing;
import com.rastrack.rastrackservices.processing.WSProcessingFactory;
import java.io.IOException;
import java.util.Set;
import java.util.Timer;

/**
 *
 * @author Jorge Espinosa
 * @version 1.1.0
 */
public class RastrackServices {

    public static int wait = 2000;

    private static Logger LOGGER = LogHelper.getLogger(RastrackServices.class.getName());

    /**
     * Entrada del programa
     *
     * @param args argumentos de la línea de comandos. No serán utilizados.
     */
    public static void main(String[] args) {
        AppConfiguration config = null;
        try {
            do {
                config = AppConfiguration.getInstance();
                try {
                    config.loadConfiguration();
                    LOGGER.info("Configuration Loaded");
                } catch (IOException ex) {
                    throw ex;
                }
                if (config.getBooleanProperty(AppConfiguration.STOP_MAIN_THREAD)) {
                    break;
                }
                if (AppConfiguration.FIRTSTIME || (config.getBooleanProperty(AppConfiguration.STOP_THREADS))) {
                    
                    stopThreads(wait);

                    boolean parameterized = config.getBooleanProperty(AppConfiguration.PARAMETERIZED_VEHICLES);
                    
                    // FTP Services 
                    FTPServiceConfiguration configFtp1 = config.getFtpConfigurationService(1);
                    if (configFtp1 == null) {
                        LOGGER.info("There were not FTP services to process ...");
                    } else {
                        if (!configFtp1.isStop()) {
                            LOGGER.info("There is a FTP services to process ...");
                            ThreadProcessing proc = new ThreadProcessing(new FTPProcessing(configFtp1), configFtp1.getFrequency(), configFtp1.getNameService(), configFtp1.isStop());                           
                            proc.start();
                        }
                    }

                    // DB services
                    DBServiceConfiguration configDB = config.getDBConfigurationService(1);
                    if (configDB == null) {
                        LOGGER.info("There were not DB services to process ...");
                    } else {
                        if (!configDB.isStop()) {
                            LOGGER.info("There is a DB services to process ...");
                            ThreadProcessing proc = new ThreadProcessing(new DBProcessing(configDB), configDB.getFrequency(), configDB.getNameService(), configDB.isStop());
                            proc.start();
                        }
                    }
                    WSServiceConfiguration configWs = null;
                    for (int i = 1; (configWs = config.getWSConfigurationService(i)) != null; i++) {
                        if (!configWs.isStop()) {
                            LOGGER.info("There is a WS services to process ... number " + i + "... " + configWs.getNameService());
                            ThreadProcessing proc = new ThreadProcessing(WSProcessingFactory.getWSThreadProcessing(configWs, i), configWs.getFrequency(), configWs.getNameService(), configWs.isStop());
                            proc.start();
                        }
                    }
                    config.saveProperty(AppConfiguration.STOP_THREADS, false);
                }
                LOGGER.info("The Main Thread starts to wait......");
                AppConfiguration.FIRTSTIME = false;

                if (config.getBooleanProperty(AppConfiguration.SHOWACTIVETHREADS)) {
                    LOGGER.info("Threads Active :" + Thread.activeCount());
                    Set<Thread> threads = Thread.getAllStackTraces().keySet();
                    for (Thread t : threads) {
                        LOGGER.info("Thread Active Name :" + t.getName());
                    }
                }
                Thread.sleep(config.getIntegerProperty(AppConfiguration.MILLS_TO_WAIT_MAIN));
                LOGGER.info("The Main Thread asks for Stoping Threads");
            } while (!config.getBooleanProperty(AppConfiguration.STOP_THREADS) && !config.getBooleanProperty(AppConfiguration.STOP_MAIN_THREAD));
            LOGGER.info("The Main Thread starts to wait for stoping all threads......");
        } catch (IOException | InterruptedException ex) {
            LOGGER.error(ex.getMessage(), ex);
        } finally {
            stopThreads(wait);
        }
        LOGGER.info("The application ended correctly......");
        System.console().readLine();
    }

    public static void stopThreads(int millToWait) {
        ThreadProcessing.setAllStop(true);
        while (ThreadProcessing.getThreadActive() != 0) {
            try {
                Thread.sleep(millToWait);
            } catch (InterruptedException ex) {
                LOGGER.error(ex.getMessage(), ex);
            }
        }
        ThreadProcessing.setAllStop(false);
    }
}
