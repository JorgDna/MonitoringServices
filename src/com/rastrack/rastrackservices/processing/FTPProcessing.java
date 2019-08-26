/*                                             
 *                            
 *Proyecto: 
 *Módulo:
 *Autor: Jorge Espinosa 
 *Fecha: 01/04/2019
 *Descripción: 
 *
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.configuration.FTPServiceConfiguration;
import com.rastrack.rastrackservices.log.LogHelper;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;

public class FTPProcessing implements IProcessing {

    private final FTPServiceConfiguration config;

    private final Logger LOGGER = LogHelper.getLogger(FTPProcessing.class.getName());

    public FTPProcessing(FTPServiceConfiguration config) {
        this.config = config;
    }

    @Override
    public void procces() throws SQLException, ClassNotFoundException, IOException {
        LOGGER.info("Processing starts reading FTP files at " + config.getPath());
        File dir = new File(config.getPath());
        File[] filesList = dir.listFiles();
        if (filesList != null && filesList.length > 0) {
            FTPProcessingThread.setFilesToProcess(filesList);
            for (int i = 0; i < config.getThreadsNumber(); i++) {
                new FTPProcessingThread(config.getNameService() + " Thread-" + (i + 1), config.getReportsPath(), config.getOldPath(), config.getFailed_path(), config.getId(), config.isParameterized()).start();
            }
            do {
                try {
                    LOGGER.info("Starts to wait for " + (config.getMillsToWait() / 60000) + " minutes");
                    Thread.sleep(config.getMillsToWait());
                } catch (InterruptedException ex) {
                    LOGGER.debug(ex);
                }
            } while (!ThreadProcessing.isStop() && FTPProcessingThread.getThreadsNumber() != 0);
        } else {
            LOGGER.info("There were not files to process");
        }
        LOGGER.info("FTP process has ended");
    }

}
