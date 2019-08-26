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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author usuario
 */
public class WSSkyWaveProcessing implements IProcessing {

    private final WSServiceConfiguration config;

    private final Logger LOGGER = LogHelper.getLogger(WSWidtechProcessing.class.getName());

    public WSSkyWaveProcessing(WSServiceConfiguration config) {
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
                        if (credential.getLastMessageId() == null || credential.getLastMessageId().isEmpty()) {
                            Calendar cal = Calendar.getInstance();
                            cal.add(Calendar.HOUR_OF_DAY, -3);
                            DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            url += "&start_utc=" + format.format(cal.getTime());
                        } else {
                            url += "&from_id=" + credential.getLastMessageId();
                        }
                        String key = persistence.selectGoogleKeybyClient(credential.getCliId());
                        List<Tbtravel> results = helper.loadLastLocationFromSkywaveInPlateList(url, config.getGoogleURL(), key, credential);
                        if (credential.getLastMessageId() == null || credential.getLastMessageId().isEmpty() || credential.getLastMessageId().equals("-1")) {
                            int lastMessageId = 0;
                            if (results != null && !results.isEmpty()) {
                                Tbtravel result = results.get(results.size() - 1);
                                lastMessageId = result.getHst() != null ? (lastMessageId < result.getHst() ? result.getHst() : lastMessageId) : 0;
                            }
                            if (lastMessageId > 0) {
                                credential.setLastMessageId(String.valueOf(lastMessageId));
                            } else {
                                credential.setLastMessageId(null);
                            }
                        }
                        persistence.updateCredentials(credential);
                        LOGGER.info("The credetial was updated... ");
                        allTravel.addAll(results);
                    } catch (Throwable ex) {
                        LOGGER.error("There was an exception trying to process the credentials for " + credential.getUspUser() + " ... " + ex.getMessage());
                        LOGGER.catching(ex);
                    }
                }
            }
            if (!allTravel.isEmpty()) {
                WSSkyWaveProcessingThread.setTravels(allTravel);
                for (int i = 0; i < config.getThreadsNumber(); i++) {
                    new WSSkyWaveProcessingThread(config.getNameService() + " Thread-" + (i + 1), config.getId(), config.isParameterized()).start();
                }
                do {
                    try {
                        Thread.sleep(config.getMillsToWait());
                    } catch (InterruptedException ex) {
                        LOGGER.debug(ex);
                    }
                } while (!ThreadProcessing.isStop() && WSSkyWaveProcessingThread.getThreadsNumber() != 0);
                LOGGER.info("All Travels have been processed");
            } else {
                LOGGER.info("There were not result for vehicles to process ");
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
