/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.processing;

import com.rastrack.rastrackservices.entities.GlobalUnittypeReports;
import com.rastrack.rastrackservices.log.LogHelper;
import com.rastrack.rastrackservices.util.MessageLog;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;

/**
 *
 * @author usuario
 */
public class FTPProcessingThread extends Thread {

    private static File[] filesToProcess;

    private static int count = 0;
    private final Logger LOGGER = LogHelper.getLogger(FTPProcessingThread.class.getName());

    private String reportsPath;
    private String oldPath;
    private String failedPath;

    private static int threadsNumber = 0;
    private boolean parameterized;
    private int servId;

    public FTPProcessingThread(String name, String reportsPath, String oldPath, String failedPath, int servId, boolean parameterized) {
        this.reportsPath = reportsPath;
        this.oldPath = oldPath;
        this.failedPath = failedPath;
        super.setName(name);
        this.parameterized = parameterized;
        this.servId = servId;
        addThread();
    }

    @Override
    public void run() {
        File file = null;
        while ((file = getFileFromFilesToProcess()) != null) {
            try {
                if (file.isFile() && file.getName().startsWith("StuMessages") && file.getName().endsWith(".xml")) {
                    String decode = new String(Files.readAllBytes(Paths.get(file.getCanonicalPath())));
                    MessageLog lg = new MessageLog();
                    List<GlobalUnittypeReports> reports = FTPHelper.decodeXMLContent(decode, servId, parameterized);
                    if (reports != null && !reports.isEmpty()) {
                        try {
                            lg.setObjecToProcces(file.getName());
                            GlobalUnittypeReports report = reports.get(reports.size() - 1);
                            lg.addResultMessage("The report was processed");
                            boolean isToReportThroughFTP = report.getVehicle().isCreateTextFile();
                            if (isToReportThroughFTP) {
                                java.nio.file.Files.copy(Paths.get(file.getCanonicalPath()), Paths.get(reportsPath).resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                                lg.addResultMessage("It was reported through FTP and was copied to " + reportsPath);
                                LOGGER.debug("The file " + file.getName() + " was reported through FTP and is copied to " + reportsPath);
                            }
                            java.nio.file.Files.move(Paths.get(file.getCanonicalPath()), Paths.get(oldPath).resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                            LOGGER.debug("the file " + file.getName() + " was moved to " + oldPath);
                            lg.addResultMessage("It was moved to " + oldPath);
                            lg.setState("OK");
                        } catch (IOException ex2) {
                            lg.setState("FAILED");
                            lg.addResultMessage("Error in reading Global messages");
                            LOGGER.debug("Error in reading Global messages: " + ex2.getMessage());
                            LOGGER.debug(ex2);
                            lg.setException(ex2);
                        }
                        LOGGER.info(lg);
                    } else {
                        if (!file.isDirectory()) {
                            throw new Exception("Processing the report ended up to NULL or Empty for File " + file.getName());
                        }
                    }

                } else {
                    if (!file.isDirectory()) {
                        throw new Exception("File " + file.getName() + " is not a valid file to process");
                    }
                }
            } catch (Exception ex) {
                try {
                    LOGGER.info("Error in reading Global file " + file.getName() + " -- " + ex.getMessage());
                    LOGGER.info(ex);
                    java.nio.file.Files.move(Paths.get(file.getCanonicalPath()), Paths.get(failedPath).resolve(file.getName()), StandardCopyOption.REPLACE_EXISTING);
                    LOGGER.info("File " + file.getName() + " was moved to " + failedPath);
                } catch (IOException ex1) {
                    LOGGER.info("File " + file.getName() + " couldn't moved to " + failedPath);
                    LOGGER.catching(ex1);
                }
            }
        }
        removeThread();
        if (threadsNumber <= 0) {
            LOGGER.info("All of Threads for FTP process have ended");
        }
    }

    private synchronized static File getFileFromFilesToProcess() {
        if (!ThreadProcessing.isStop() && count < filesToProcess.length) {
            return filesToProcess[count++];
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
     * @return the filesToProcess
     */
    public static File[] getFilesToProcess() {
        return filesToProcess;
    }

    /**
     * @param aFilesToProcess the filesToProcess to set
     */
    public static void setFilesToProcess(File[] aFilesToProcess) {
        filesToProcess = aFilesToProcess;
        count = 0;
    }

    /**
     * @return the reportsPath
     */
    public String getReportsPath() {
        return reportsPath;
    }

    /**
     * @param reportsPath the reportsPath to set
     */
    public void setReportsPath(String reportsPath) {
        this.reportsPath = reportsPath;
    }

    /**
     * @return the oldPath
     */
    public String getOldPath() {
        return oldPath;
    }

    /**
     * @param oldPath the oldPath to set
     */
    public void setOldPath(String oldPath) {
        this.oldPath = oldPath;
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
     * @return the failedPath
     */
    public String getFailedPath() {
        return failedPath;
    }

    /**
     * @param failedPath the failedPath to set
     */
    public void setFailedPath(String failedPath) {
        this.failedPath = failedPath;
    }
}
