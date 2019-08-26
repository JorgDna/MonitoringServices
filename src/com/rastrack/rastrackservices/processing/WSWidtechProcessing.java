/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.configuration.WSServiceConfiguration;
import com.rastrack.rastrackservices.entities.TbpasswordRastrackplus;
import com.rastrack.rastrackservices.entities.Tbtravel;
import com.rastrack.rastrackservices.entities.Tbvehicle;
import com.rastrack.rastrackservices.log.LogHelper;
import com.rastrack.rastrackservices.persistence.IPersistence;
import com.rastrack.rastrackservices.persistence.Persistence;
import com.rastrack.rastrackservices.util.MessageLog;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author usuario
 */
public class WSWidtechProcessing implements IProcessing {

    private final WSServiceConfiguration config;

    private final Logger LOGGER = LogHelper.getLogger(WSWidtechProcessing.class.getName());

    public WSWidtechProcessing(WSServiceConfiguration config) {
        this.config = config;
    }

    @Override
    public void procces() throws SQLException, ClassNotFoundException, IOException {
        LOGGER.info("It starts to process In Web Service");
        IPersistence persistence = null;
        try {
            persistence = new Persistence();
            List<Tbtravel> allTravel = new ArrayList<>();
            List<TbpasswordRastrackplus> credentials = persistence.getPasswords(config.getId());
            if (credentials != null && !credentials.isEmpty()) {
                for (TbpasswordRastrackplus credential : credentials) {
                    try {
                        LOGGER.info("It process with credential: " + credential.getUspUser());
                        WSHelper helper = new WSHelper();
                        String url = config.getUrl().replace(":login", credential.getUspUser()).replace(":password", credential.getUspPassword());
                        List<Tbtravel> results = helper.retrieveAllLastLocationOfPlatesFromUser(url);
                        allTravel.addAll(results);
                    } catch (Exception ex) {
                        LOGGER.error("There was an exception trying to process the credentials for " + credential.getUspUser() + " ... " + ex.getMessage());
                        LOGGER.catching(ex);
                    }
                }
            }
            if (allTravel.isEmpty()) {
                LOGGER.info("There were not result for vehicles to proces ");
            } else {
                WSWidtechProcessingThread.setTravels(allTravel);
                for (int i = 0; i < config.getThreadsNumber(); i++) {
                    new WSWidtechProcessingThread(config.getNameService() + " Thread-" + (i + 1), config.getId(), config.isParameterized()).start();
                }
                do {
                    try {
                        Thread.sleep(config.getMillsToWait());
                    } catch (InterruptedException ex) {
                        LOGGER.debug(ex);
                    }
                } while (!ThreadProcessing.isStop() && WSWidtechProcessingThread.getThreadsNumber() != 0);
                LOGGER.info("All travels have been processed");
            }
        } catch (IOException | ClassNotFoundException | SQLException ex) {
            LOGGER.error("There was an exception when it tries to process the reports " + ex.getMessage());
            LOGGER.catching(ex);
        } catch (Throwable ex) {
            LOGGER.error("There was a exception when it tries to process the reports " + ex.getMessage());
            LOGGER.catching(ex);
        } finally {
            persistence = null;
        }
        LOGGER.info("WS process ended");
    }
}
