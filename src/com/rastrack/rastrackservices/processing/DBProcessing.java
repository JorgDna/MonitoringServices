/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.configuration.DBServiceConfiguration;
import com.rastrack.rastrackservices.entities.GlobalUnittypeReports;
import com.rastrack.rastrackservices.log.LogHelper;
import com.rastrack.rastrackservices.persistence.IPersistence;
import com.rastrack.rastrackservices.persistence.Persistence;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author usuario
 */
public class DBProcessing implements IProcessing {

    private final DBServiceConfiguration config;

    private final Logger LOGGER = LogHelper.getLogger(DBProcessing.class.getName());

    public DBProcessing(DBServiceConfiguration config) {
        this.config = config;
    }

    @Override
    public void procces() throws SQLException, ClassNotFoundException, IOException {
        LOGGER.info("It starts to process In Data Base");
        IPersistence persistence = null;
        try {
            persistence = new Persistence();
            List<GlobalUnittypeReports> reportsToProcess = persistence.getGlobalUnittypeReports();
            LOGGER.debug("It searched reports to process ...");
            if (reportsToProcess != null && !reportsToProcess.isEmpty()) {
                DBProcessingThread.setReports(reportsToProcess);
                for (int i = 0; i < config.getThreadsNumber(); i++) {
                    new DBProcessingThread(config.getNameService() + " Thread-" + (i + 1), config.isParameterized(), config.getId()).start();
                }
                do {
                    try {
                        Thread.sleep(config.getMillsToWait());
                    } catch (InterruptedException ex) {
                        LOGGER.debug(ex);
                    }
                } while (!ThreadProcessing.isStop() && DBProcessingThread.getThreadsNumber() != 0);
                LOGGER.info("All reports have been processed");
            } else {
                LOGGER.info("There were not reports to process In Data Base");
            }
        } catch (Throwable ex) {
            LOGGER.info("There was a exception when it tries to process the reports " + ex.getMessage());
            LOGGER.catching(ex);
        } finally {
            persistence = null;
        }
        LOGGER.info("it ended to process in DataBase");
    }

}
