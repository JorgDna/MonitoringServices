/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.rastrack.rastrackservices.configuration;

/**
 *
 * @author usuario
 */
public class DBServiceConfiguration {

    public static final String FREQUENCY = "frequency";
    public static final String NAME = "nameService";
    public static final String STOP = "stop";
    public static final String THREADSNUMBER = "threadsnumber";
    public static final String ID = "id";

    private final int frequency;
    private final String nameService;
    private boolean stop;
    private int threadsNumber;
    private int millsToWait;
    private boolean parameterized;
    private int id;

    public DBServiceConfiguration(int frequency, String nameService, boolean stop, int threadsNumber, int millsToWait) {
        this.frequency = frequency;
        this.nameService = nameService;
        this.stop = stop;
        this.threadsNumber = threadsNumber;
        this.millsToWait = millsToWait;
    }

    /**
     * @return the frequency
     */
    public int getFrequency() {
        return frequency;
    }

    /**
     * @return the nameService
     */
    public String getNameService() {
        return nameService;
    }

    /**
     * @return the stop
     */
    public boolean isStop() {
        return stop;
    }

    /**
     * @param stop the stop to set
     */
    public void setStop(boolean stop) {
        this.stop = stop;
    }

    /**
     * @return the threadsNumber
     */
    public int getThreadsNumber() {
        return threadsNumber;
    }

    /**
     * @param threadsNumber the threadsNumber to set
     */
    public void setThreadsNumber(int threadsNumber) {
        this.threadsNumber = threadsNumber;
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
     * @return the parameterized
     */
    public boolean isParameterized() {
        return parameterized;
    }

    /**
     * @param parameterized the parameterized to set
     */
    public void setParameterized(boolean parameterized) {
        this.parameterized = parameterized;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

}
