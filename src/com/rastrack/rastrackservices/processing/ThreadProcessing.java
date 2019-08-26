/*                                             
 *                            AXURE
 *Proyecto: AXURE
 *Módulo:
 *Autor: Jorge Espinosa <analista_seis@rastrack.com.co>
 *Fecha: 23/05/2013
 *Descripción: 
 *
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.log.LogHelper;
import java.io.IOException;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;

public class ThreadProcessing extends Thread {

    private static int threadActive = 0;
    private static boolean allStop = false;
    private IProcessing proccesing;
    private int millsToWait;
    private boolean stop;
    private static Logger LOGGER = LogHelper.getLogger(ThreadProcessing.class.getName());

    public ThreadProcessing(IProcessing proccesing, int millsToWait, String name, boolean stop) {
        this.proccesing = proccesing;
        this.millsToWait = millsToWait;
        this.stop = stop;
        super.setName(name);
        super.setDaemon(true);
        addThreadActive();
    }

    @Override
    public void run() {
        try {
            while (!stop && !allStop) {
                try {
                    proccesing.procces();
                } catch (Throwable ex) {
                    LOGGER.error(ex);
                    LOGGER.catching(ex);
                }
                LOGGER.info("Starts to wait for " + (millsToWait / 60000) + " minutes");
                Thread.sleep(millsToWait);
                LOGGER.info("Starts to process again after waiting");
            }
            LOGGER.info("The thread " + this.getName() + " is stopped....");
        } catch (InterruptedException ex) {
            LOGGER.error(ex);
            LOGGER.catching(ex);
        }
        removeThreadActive();
    }

    /**
     * @return the proccesing
     */
    public IProcessing getProccesing() {
        return proccesing;
    }

    /**
     * @param proccesing the proccesing to set
     */
    public void setProccesing(IProcessing proccesing) {
        this.proccesing = proccesing;
    }

    /**
     * @return the allStop
     */
    public static boolean isStop() {
        return allStop;
    }

    /**
     * @param allStop the allStop to set
     */
    public static void setAllStop(boolean allStop) {
        ThreadProcessing.allStop = allStop;
    }

    /**
     * @return the millsToWait
     */
    public int getMillsToWait() {
        return millsToWait;
    }

    /**
     * @param millsToWait the millsToWait to set
     */
    public void setMillsToWait(int millsToWait) {
        this.millsToWait = millsToWait;
    }

    /**
     * @return the threadActive
     */
    public synchronized static int getThreadActive() {
        return threadActive;
    }

    /**
     * @param aThreadActive the threadActive to set
     */
    public synchronized static void addThreadActive() {
        threadActive++;
    }

    public synchronized static void removeThreadActive() {
        threadActive--;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (super.getName() != null ? super.getName().hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object proc) {
        return super.getName().equals(((ThreadProcessing) proc).getName());
    }

    /**
     * @param stop the stop to set
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }
}
