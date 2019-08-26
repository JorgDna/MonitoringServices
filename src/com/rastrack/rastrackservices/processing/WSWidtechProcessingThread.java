/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.entities.GlobalUnittypeReports;
import com.rastrack.rastrackservices.entities.Tbtravel;
import com.rastrack.rastrackservices.entities.Tbvehicle;
import com.rastrack.rastrackservices.log.LogHelper;
import com.rastrack.rastrackservices.persistence.IPersistence;
import com.rastrack.rastrackservices.persistence.Persistence;
import static com.rastrack.rastrackservices.processing.DBProcessingThread.addThread;
import static com.rastrack.rastrackservices.processing.DBProcessingThread.removeThread;
import com.rastrack.rastrackservices.util.MessageLog;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author usuario
 */
public class WSWidtechProcessingThread extends Thread {

    private static List<Tbtravel> travels;

    private static int count = 0;
    private final Logger LOGGER = LogHelper.getLogger(WSWidtechProcessingThread.class.getName());

    private static int threadsNumber = 0;

    private boolean parameterized;
    private int servId;

    public WSWidtechProcessingThread(String name, int servId, boolean parameterized) {
        super(name);
        this.parameterized = parameterized;
        this.servId = servId;
        addThread();
    }

    @Override
    public void run() {
        Tbtravel travel = null;
        while ((travel = getTravel()) != null) {
            IPersistence persistence = null;
            try {
                persistence = new Persistence();
                Tbvehicle vehicle = persistence.fetchVehicleByAliasOrPlate(travel.getPlate(), servId, parameterized);
                if (vehicle != null) {
                    System.out.println(travel.getPlate());
                    travel.setVhcPlaca(vehicle);
                    WSProcessingResult processing = new WSProcessingResult(persistence);
                    processing.processResultsFromWidetech(travel);
                    processing.updateVehicleLocationWithTravelData(travel);
                    LOGGER.info(new MessageLog(vehicle.getVhcPlaca(), "OK", new StringBuilder("The Vehicle Location was updated and processed.")));
                } else {
                    LOGGER.info(new MessageLog(travel.getPlate(), "FAILED", new StringBuilder("Plate " + travel.getPlate() + " NOT FOUND in DB Or NOT PARAMETERIZED.")));
                }
            } catch (Throwable ex) {
                LOGGER.debug("There was an exception trying to process the location for " + travel.getPlate() + " ... " + ex.getMessage());
                LOGGER.debug(ex);
                LOGGER.info(new MessageLog(travel.getPlate(), "FAILED", new StringBuilder("There was an exception trying to process the location for " + travel.getPlate()), ex));
            } finally {
                persistence = null;
            }
        }
        removeThread();
        if (threadsNumber <= 0) {
            LOGGER.info("All of Threads for WS Widtech service have ended");
        }
    }

    private synchronized static Tbtravel getTravel() {
        if (!ThreadProcessing.isStop() && count < travels.size()) {
            return travels.get(count++);
        }
        return null;
    }

    public static void addThread() {
        ThreadProcessing.addThreadActive();
        threadsNumber++;
    }

    public static void removeThread() {
        ThreadProcessing.removeThreadActive();
        threadsNumber--;
    }

    /**
     * @return the travels
     */
    public static List<Tbtravel> getTravels() {
        return travels;
    }

    /**
     * @param atravels the travels to set
     */
    public static void setTravels(List<Tbtravel> atravels) {
        travels = atravels;
        count = 0;
    }

    /**
     * @return the count
     */
    public static int getCount() {
        return count;
    }

    /**
     * @param aCount the count to set
     */
    public static void setCount(int aCount) {
        count = aCount;
    }

    /**
     * @return the threadsNumber
     */
    public static int getThreadsNumber() {
        return threadsNumber;
    }

    /**
     * @param aThreadsNumber the threadsNumber to set
     */
    public static void setThreadsNumber(int aThreadsNumber) {
        threadsNumber = aThreadsNumber;
    }

}
