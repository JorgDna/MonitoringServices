/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.entities.Tbtravel;
import com.rastrack.rastrackservices.log.LogHelper;
import com.rastrack.rastrackservices.persistence.IPersistence;
import static com.rastrack.rastrackservices.processing.WSSkyWaveProcessingThread.addThread;
import static com.rastrack.rastrackservices.processing.WSSkyWaveProcessingThread.removeThread;
import java.util.List;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author usuario
 */
public class WSInReachProcessingThread extends Thread {

    private static List<Tbtravel> travels;

    private static int count = 0;
    private final Logger LOGGER = LogHelper.getLogger(WSInReachProcessingThread.class.getName());

    private static int threadsNumber = 0;

    public WSInReachProcessingThread(String name) {
        super(name);
        addThread();
    }

    @Override
    public void run() {
        Tbtravel travel = null;
        while ((travel = getTravel()) != null) {
            IPersistence persistence = null;
        }
        removeThread();
        if (threadsNumber <= 0) {
            LOGGER.info("All of Threads for WS Inreach service have ended");
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

}
